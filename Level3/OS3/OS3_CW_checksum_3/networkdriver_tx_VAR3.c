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


/* Could allow direct access to the static variables, but this is cleaner... */
/* We pass the arguments to each of our threads explicitly instead */

/************/



/*
 * the following internal function is used to return a free packet descriptor
 * 1. first attempts to return it to the BoundedBuffer pool
 * 2. if that fails, performs a nonblocking call to return it to the fpds
 *    NB: the first handout indicates that the packet descriptor store is
 *        an unbounded container, therefore, calling the nonblocking_put_pd()
 *        method should NEVER fail
 */


#define SEND_LIMIT 3

/*
 * for each pass through the infinite loop:
 * 1. fetch the next message to place on the network
 * 2. attempt to send it SEND_LIMIT times until successful
 * 3. log success/failure
 * 4. attempt to add packet descriptor to the receiver pool
 *    if that fails (it is full), place it in the FPDS
 */

void *sender_fn (void *args) {
    PacketDescriptor msg;
    int sendcount;
    int result;
    struct send_arg_t *myargs = (struct send_arg_t *)args;


	msg = (PacketDescriptor) blockingReadBB(myargs->pending_sends);
	for (sendcount = 0; sendcount < SEND_LIMIT; sendcount++) {

	    if (result)
	        break;
	}
	if (result)
	    printf("[DRIVER> Info: Sent a packet after %d tries\n",
		   ++sendcount);
	else
	    printf("[DRIVER> Info: Failed to send a packet after %d tries\n",
		   sendcount);
	if (! nb_put_pd(myargs->receiver_pool, myargs->fpds, msg))
            printf("[DRIVER> Error? failed to return a packet descriptor\n");

    return NULL;
}
