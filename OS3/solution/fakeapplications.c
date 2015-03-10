/*
 * Author:    Peter Dickman
 * Version:   1.2
 * Last edit: 2003-02-25
 *
 * This file is a component of the test harness and or sample
 * solution to the NetworkDriver exercise for use in assessing:
 *    the OS3 module in undergraduate Computing Science at dcs.gla.ac.uk
 *    utilised February 2003
 *
 * It tests the ability to develop a small but complex software system
 * using PThreads to provide concurrency in C.
 *
 *
 * Code quality:
 * Quick hack - no guarantees or liability accepted.
 *
 * Copyright:
 * (c) 2003 University of Glasgow and Dr Peter Dickman
 *
 * Licencing: 
 * this software may not be used except with the author's permission.
 *
 */


#include <time.h>

#include <pthread.h>
#include <stdlib.h>
#include "diagnostics.h"

#include <unistd.h>

#include "destination.h"
#include "pid.h"
#include "packetdescriptor.h"
#include "freepacketdescriptorstore.h"
#include "networkdriver.h"

pthread_t senders[MAX_PID+1];
pthread_t receivers[MAX_PID+1];


struct args { FreePacketDescriptorStore fpds; int i; };


void pause_briefly(void)
{
    /* Pause for a while */

    struct timespec pause_time;

    pause_time.tv_nsec = 0;
    pause_time.tv_sec = 1 + (rand() % 3);
    nanosleep(&pause_time, NULL);
}


void * make_args(FreePacketDescriptorStore fpds, int i)
{
    struct args * args = (struct args *) malloc (sizeof(struct args));
    args->fpds = fpds;
    args->i = i;
    return (void *) args;
}

static void * fa_sender_fn(void * args)
{
    struct args *             myargs  = (struct args *) args;
    FreePacketDescriptorStore fpds    = myargs->fpds;
    int                       myindex = myargs->i;

    PacketDescriptor pd;
    Destination d = NULL;  /* Warning: deliberately never initialised */

    free(args);

    DIAGNOSTICS("Info: sender for application %d is running.\n", myindex);

    for(;;)
    {	 
	pause_briefly();

	/* Now do some work */

	blocking_get_pd(fpds, &pd);
	DIAGNOSTICS("Info: application %d has acquired a packet descriptor.\n",
		    myindex);
	init_packet_descriptor(&pd);
	DIAGNOSTICS("Info: application %d is sending a packet descriptor.\n",
		    myindex);
	packet_descriptor_set_destination(&pd, d);
	packet_descriptor_set_pid(&pd, myindex);

	if (rand()%2)
	{
	    blocking_send_packet(pd);
	    DIAGNOSTICS("Info: application %d has sent a packet descriptor (blocking).\n",
			myindex);
	}
	else if (nonblocking_send_packet(pd))
	{
	    DIAGNOSTICS("Info: application %d has sent a packet descriptor (nonblocking).\n",
			myindex);
	}
	else
	{
	    DIAGNOSTICS("Info: application %d failed to send a packet descriptor (nonblocking).\n",
			myindex);
	    blocking_put_pd(fpds, pd);
	    DIAGNOSTICS("Info: application %d (sender) has released a packet descriptor.\n",
			myindex);
	}
    }

    return NULL;
}

static void * fa_receiver_fn(void * args)
{
    struct args *             myargs  = (struct args *) args;
    FreePacketDescriptorStore fpds    = myargs->fpds;
    int                       myindex = myargs->i;

    PacketDescriptor pd;
    free(args);
    DIAGNOSTICS("Info: receiver for application %d is running.\n", myindex);

    for(;;)
    {
	blocking_get_packet(&pd, myindex);
	DIAGNOSTICS("Info: application %d has received a packet.\n", 
		    myindex);

	blocking_put_pd(fpds, pd);
	DIAGNOSTICS("Info: application %d has released a packet descriptor.\n",
		    myindex);
    }

    return NULL;
}




void construct_fake_applications(FreePacketDescriptorStore fpds)
{
    /* Spawn off lots of threads, one sender & one receiver per pid */
    int i;

    for (i=0; i <= MAX_PID; i++)
    {
	pthread_create(&senders[i], NULL, fa_sender_fn, make_args(fpds,i));
	pthread_create(&receivers[i], NULL, fa_receiver_fn, make_args(fpds,i));
    }
}

