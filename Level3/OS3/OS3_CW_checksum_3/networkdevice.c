/*
 * Author:    Peter Dickman
 * Version:   1.0
 * Last edit: 2003-02-25
 *
 * This file is a component of the test harness and or sample
 * solution to the NetworkDriver exercise developed in February 2003
 * for use in assessing:
 *    the OS3 module in undergraduate Computing Science at dcs.gla.ac.uk
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

#include "networkdevice__full.h"

#include <pthread.h>
#include <stdlib.h>

#include <sys/time.h>
#include <unistd.h>

#include "diagnostics.h"
#include "packetdescriptor.h"
#include "destination.h"
#include "pid.h"


/*---------------------- Hidden Implementation Details -------------------*/
#if 0
static unsigned long errcount = 0;

struct networkdevice {
    pthread_mutex_t    out_lock;
    pthread_cond_t     send_timer; /* for timing only, never signalled */
    int                sending;

    PacketDescriptor * pending_packet_descriptor;
    int                pd_registered;
    int                pd_filled;

    pthread_mutex_t    in_lock;
    pthread_cond_t     rcv_timer; /* for timing only, never signalled */
    pthread_cond_t     packet_received;
    PID                target;
    int                thread_waiting;
};

typedef struct networkdevice * real_nd;
#endif

/*
 * pause calling thread for number of seconds `s' and nano seconds `ns'
 */
static void pause_thread(unsigned long s, unsigned long ns) {
   struct timespec pause_time;

   pause_time.tv_sec  = s;
   pause_time.tv_nsec = ns;
   nanosleep(&pause_time, NULL);
}

void short_out_delay(void)  /* delays for fraction of a second */
{
#define OUT_PAUSE_SECS     0
#define OUT_PAUSE_NANOSECS 700000000

   pause_thread(OUT_PAUSE_SECS, OUT_PAUSE_NANOSECS);
}

void startup_delay(void)  /* delays for a few of seconds */
{
#define STARTUP_PAUSE_SECS     5
#define STARTUP_PAUSE_NANOSECS 0

   pause_thread(STARTUP_PAUSE_SECS, STARTUP_PAUSE_NANOSECS);
}

void short_in_delay(void)  /* delays for fraction of a second */
{
#define IN_PAUSE_SECS       0
#define IN_PAUSE_NANOSECS_0 100000000
#define IN_PAUSE_NANOSECS_1 300000000
#define IN_PAUSE_NANOSECS_2 700000000

   switch (rand() % 3)
   {
   case 0: 
       pause_thread(IN_PAUSE_SECS, IN_PAUSE_NANOSECS_0);
       break;

   case 1:
       pause_thread(IN_PAUSE_SECS, IN_PAUSE_NANOSECS_1);
       break;

   case 2:
       pause_thread(IN_PAUSE_SECS, IN_PAUSE_NANOSECS_2);
       break;

   default:
       DIAGNOSTICS("BUG: Illegal branch inside short_in_delay\n");
   }
}

float get_rand_float(void)
{
    return (0.0 + (rand() % 1000))/1000.0;
}

int random_send_success(void)
{
#define SEND_AFTER_OK   0.95
#define SEND_AFTER_FAIL 0.4

    static int last_send_ok = 1;

    last_send_ok = last_send_ok ? (get_rand_float() < SEND_AFTER_OK) 
	                        : (get_rand_float() < SEND_AFTER_FAIL) ;
    return last_send_ok;
}


int run_device_out(real_nd rnd, PacketDescriptor pd)
/* Function called as needed from send..... */
{
    void *dummy;	 /* subterfuge to remove unused parameter warning */
    int response = 0;

    dummy = (void *)pd; /* subterfuge to remove unused parameter warning */
    pthread_mutex_lock(&rnd->out_lock);
    if (rnd->sending)
    {
	DIAGNOSTICS("BUG: Two or more parallel calls to send a packet\n");
	DIAGNOSTICS((char*)dummy);
	errcount++;
        pthread_mutex_unlock(&rnd->out_lock);
	return 0;
    }

    rnd->sending++;
    pthread_mutex_unlock(&rnd->out_lock);

    short_out_delay();	/* give student a chance to call us again
                           triggering the above DIAGNOSTIC */
    response = random_send_success();

    if (response)
	DIAGNOSTICS("[Device> packet successfully sent.\n");
    else
	DIAGNOSTICS("[Device> attempt to send packet failed.\n");

    pthread_mutex_lock(&rnd->out_lock);
    rnd->sending--;
    pthread_mutex_unlock(&rnd->out_lock);

    return response;
}

PID random_pid(void)
{
    return (rand() % (MAX_PID+1));
}


void * run_device_in(void * args)
/* A separate thread drives this, generating inputs from time to time */
{
    real_nd rnd = (real_nd) args;

    DIAGNOSTICS("[Device> Info: network device ready, pausing prior to input...\n");
    startup_delay();
    DIAGNOSTICS("[Device> Info: network input starting...\n");

    for(;;)
    {
	short_in_delay();
        pthread_mutex_lock(&rnd->in_lock);

	/* Want to deliver a packet at this point, are they ready? */

	if (!rnd->pd_registered)
	{
	    DIAGNOSTICS("[Device> Error: Dropping data. No receiver packet.\n");
	    errcount++;
	    goto unlock;
	}
	else if (rnd->pd_filled)
	{
	    DIAGNOSTICS("[Device> Error: Dropping data. Receiver packet is full.\n");
	    errcount++;
	    goto unlock;
	}
	else
	    rnd->pd_filled = 1;

	rnd->target = random_pid();
        packet_descriptor_set_pid(rnd->pending_packet_descriptor, rnd->target);

	DIAGNOSTICS("[Device> Packet received for PID %d\n", rnd->target);

	if (!rnd->thread_waiting)
	    DIAGNOSTICS("[Device> Warning: Data received but no thread waiting.\n");

	pthread_cond_signal(&rnd->packet_received);
unlock:
        pthread_mutex_unlock(&rnd->in_lock);

    }	

    return (void *) &errcount;
}
#if 0
static pthread_t device_indriver_thread;

NetworkDevice construct_network_device(void)
{
    real_nd rnd = (real_nd) malloc(sizeof(struct networkdevice));

    rnd->sending        = 0;
    rnd->pd_registered  = 0;
    rnd->pd_filled      = 0;
    rnd->thread_waiting = 0;

    pthread_mutex_init(&rnd->out_lock, NULL);
    pthread_cond_init(&rnd->send_timer, NULL);
    pthread_mutex_init(&rnd->in_lock, NULL);
    pthread_cond_init(&rnd->packet_received, NULL);

    pthread_create(&device_indriver_thread, NULL, 
		   &run_device_in, (void *) rnd);

    return (NetworkDevice) rnd;
}
#endif
/*---------------- Student May Call These --------------------------------*/

int send_packet(NetworkDevice nd, PacketDescriptor pd)
/* Returns 1 if successful, 0 if unsuccessful */
/* May take a substantial time to return      */
/* If unsuccessful you can try again, but if you fail repeatedly give */
/* up and just accept that this packet cannot be sent for some reason */
{
  return run_device_out((real_nd) nd, pd);
}

#if 0
void register_receiving_packetdescriptor(NetworkDevice nd, 
					 PacketDescriptor* pd_ptr)
/* tell the network device to use the indicated PacketDescriptor     */
/* for next incoming data packet; once a descriptor is used it won't */
/* be reused for a further incoming data packet; you must register   */
/* a PacketDescriptor before the next packet arrives                 */
{
    real_nd rnd = (real_nd) nd;

    pthread_mutex_lock(&rnd->in_lock);
    if (!rnd->pd_registered)
    {
	rnd->pd_registered = 1;
	rnd->pd_filled = 0;
	rnd->pending_packet_descriptor = pd_ptr;
    }
    else
    {
	DIAGNOSTICS("[Device> Error: Attempting to register another packet descriptor.\n");
	DIAGNOSTICS("[Device> Error (contd): New descriptor ignored.\n");
	errcount++;
    }
    pthread_mutex_unlock(&rnd->in_lock);
}

void await_incoming_packet(NetworkDevice nd)
/* The thread blocks until the registered PacketDescriptor has been   */
/* filled with an incoming data packet. The PId argument is set to    */
/* indicate the local application process which should be given the   */
/* PacketDescriptor that was just filled.                             */
/* This should be called as soon as possible after the previous       */
/* thread to wait for a packet returns. Only 1 thread may be waiting. */
{
    real_nd rnd = (real_nd) nd;

    pthread_mutex_lock(&rnd->in_lock);

    if (rnd->thread_waiting)
    {
	DIAGNOSTICS("[Device> Error: more than one waiting thread\n");
	errcount++;
    }
    else if (!rnd->pd_registered)
    {
	DIAGNOSTICS("[Device> Error: awaiting packet but no registered descriptor.\n");
	errcount++;
    }
    else
    {
	rnd->thread_waiting++;

	while (!rnd->pd_filled)
	{
	    pthread_cond_wait(&rnd->packet_received, &rnd->in_lock);
	}
	rnd->pd_registered = 0;

	rnd->thread_waiting--;
    }
   
    pthread_mutex_unlock(&rnd->in_lock);
}
#endif
/*------------------------------------------------------------------------*/

