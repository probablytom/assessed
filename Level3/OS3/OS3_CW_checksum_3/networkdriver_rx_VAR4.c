/*
 * Author:    Peter Dickman
 * Version:   1.0
 * Last edit: 2003-02-18
 * Modified: 2011-02-22 by J Sventek
 * Modified: 2015-02-24 by W Vanderbauwhede for select/complete approach
 *
 * This file is a component of the test harness of the NetworkDriver exercise
 * for use in assessing:
 *    the OS3 module in undergraduate Computing Science at dcs.gla.ac.uk
 *
 * It tests the ability to develop a small but complex software system
 * using PThreads to provide concurrency in C.
 *
 *
 *
 * Copyright:
 * (c) 2003 University of Glasgow and Dr Peter Dickman
 *
 * Licencing:
 * this software may not be used except with the author's permission.
 *
 */

/*
 * General design:
 * 1. all packets to send to the network device are queued in a Bounded
 *    Buffer of size SEND_BUFFER_LENGTH
 * 2. a sender thread is created during initialization to retrieve the
 *    first element from this queue and send it to the network device
 * 3. all packets from the network device to an application thread are queued
 *    in an array of Bounded Buffers, each of size RECEIVED_BUFFER_LENGTH;
 *    the array is indexed by the process id to which the message is targeted
 * 4. a receiver thread is created during initialization to retrieve the
 *    next element from the network device and to queue it to the appropriate
 *    receiver queue
 * 5. to minimize the loss of packets received from the network, another
 *    Bounded Buffer, of size RECEIVER_POOL_LENGTH, is created and filled
 *    during initialization; the sender and receiver threads attempt to
 *    keep this bounded buffer full of packet descriptors
 */

/* This is the spec we must satisfy */
#include "networkdriver.h"

/* These are what we use */
#include "freepacketdescriptorstore__full.h"
#include "packetdescriptorcreator.h"
#include "BoundedBuffer.h"

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sched.h>


/************/


/*
 * the following internal function is used to retrieve a free packet descriptor
 * 1. first attempts to retrieve it from the BoundedBuffer pool
 * 2. if that fails, performs a blocking call to retrieve one from the fpds
 */


void *receiver_fn (void) {
    struct receiver_arg_t myargs = (struct receiver_arg_t *)args;
    // myargs: nd, fpds, receiver_array, receiver_pool
    PacketDescriptor current_pd, filled_pd;
    PID ppid;

    blocking_get_pd(myargs->fpds, &current_pd);
    init_packet_descriptor(&current_pd);
    register_receiving_packetdescriptor(myargs->nd, &current_pd);
    for(;;) {
    	// Wait for incoming packets

        filled_pd = current_pd;
	if (nb_get_pd(myargs->receiver_pool, myargs->fpds, &current_pd)) {
	    init_packet_descriptor(&current_pd);
	    register_receiving_packetdescriptor(myargs->nd, &current_pd);
            ppid = packet_descriptor_get_pid(&filled_pd);
	    if (!nonblockingWriteBB(myargs->receiver_array->pending_receives[ppid], (BufferedItem)filled_pd)) {
		printf("[DRIVER> Warning: Application(%u) Packet Store full, discarding data.\n", ppid);
		if (!nb_put_pd(myargs->receiver_pool, myargs->fpds, filled_pd))
		    printf("[DRIVER> Error? Cannot return Packet Descriptor to store\n");
	    }
	} else {
	    printf("[DRIVER> Warning: No replacement Packet Descriptor, discarding data.\n");
	    current_pd = filled_pd;
            init_packet_descriptor(&current_pd);
            register_receiving_packetdescriptor(myargs->nd, &current_pd);
	}
    }
    return NULL;
}
