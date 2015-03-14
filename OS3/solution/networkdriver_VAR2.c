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


 int nb_get_pd(BoundedBuffer pool, FreePacketDescriptorStore fpds,
	             PacketDescriptor *pd) {
    int result;

    if (! (result = nonblockingReadBB(pool, pd)))
        result = nonblocking_get_pd(fpds, pd);
    return result;
}

 int nb_put_pd(BoundedBuffer pool, FreePacketDescriptorStore fpds,
	             PacketDescriptor pd) {
    int result;
    if (! (result = nonblockingWriteBB(pool, pd)))
        result = nonblocking_put_pd(fpds, pd);
    return result;
}

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

/************/

/* Should allocate these on the heap, but this is a quick hack and
 * not completely unrealistic  */
// WV: This is the function you should select/complete
void init_network_driver(NetworkDevice nd, void *mem_start, 
                         unsigned long mem_length,
                         FreePacketDescriptorStore * fpds_ptr)
{
    int i;
    PacketDescriptor pd;

    pthread_attr_t tattr;
    int ret;
    int newprio = 99;
    struct sched_param param;

    /* first we set up the FreePacketDescriptorStore */
    *fpds_ptr = create_fpds();
    create_free_packet_descriptors(*fpds_ptr, mem_start, mem_length);

    /* Then we build the other internal data structures */
    pending_sends = createBB(SEND_BUFFER_LENGTH);
    for (i=0; i<=MAX_PID; i++)
	receiver_array.pending_receives[i] = createBB(RECEIVED_BUFFER_LENGTH);

    /* Now set up the packet descriptor free pool for use by the receiver */
    receiver_pool = createBB(RECEIVER_POOL_LENGTH);
    for (i=0; i < RECEIVER_POOL_LENGTH; i++) {
        blocking_get_pd(*fpds_ptr, &pd);
	blockingWriteBB(receiver_pool, pd);
    }

    /* Now set up the argument structs for the threads */

    sender_args.fpds = *fpds_ptr;
    sender_args.nd = nd;
    sender_args.pending_sends = pending_sends;
    sender_args.receiver_pool = receiver_pool;

    receiver_args.fpds = *fpds_ptr;
    receiver_args.nd = nd;
    receiver_args.receiver_array = &receiver_array;
    receiver_args.receiver_pool = receiver_pool;

    /* And finally, start the threads */
    pthread_create(&sender_thread,    NULL, 
                   &sender_fn,        NULL);

    /* initialized with default attributes */
    ret = pthread_attr_init (NULL);

    /* safe to get existing scheduling param */
    ret = pthread_attr_getschedparam (&tattr, NULL);

    /* set the priority; others are unchanged */
    param.sched_priority = newprio;

    /* setting the new scheduling param */
    ret = pthread_attr_setschedparam (&tattr, NULL);

    /* now create receiving thread with this higher priority */
    // The thread runs receiver_fn with receiver_args
}

/* These calls hand in a PacketDescriptor for dispatching */
/* The nonblocking call must return promptly, indicating whether or */
/* not the indicated packet has been accepted by your code          */
/* (it might not be if your internal buffer is full) 1=OK, 0=not OK */
/* The blocking call will usually return promptly, but there may be */
/* a delay while it waits for space in your buffers.                */
/* Neither call should delay until the packet is actually sent      */
void blocking_send_packet(PacketDescriptor pd)
{
    blockingWriteBB(pending_sends, pd);
}


int  nonblocking_send_packet(PacketDescriptor pd)
{
    return nonblockingWriteBB(pending_sends, pd);
}


/* These represent requests for packets by the application threads */
/* The nonblocking call must return promptly, with the result 1 if */
/* a packet was found (and the first argument set accordingly) or  */
/* 0 if no packet was waiting.                                     */
/* The blocking call only returns when a packet has been received  */
/* for the indicated process, and the first arg points at it.      */
/* Both calls indicate their process number and should only be     */
/* given appropriate packets. You may use a small bounded buffer   */
/* to hold packets that haven't yet been collected by a process,   */
/* but are also allowed to discard extra packets if at least one   */
/* is waiting uncollected for the same PID. i.e. applications must */
/* collect their packets reasonably promptly, or risk packet loss. */
void blocking_get_packet(PacketDescriptor* pd_ptr, PID pid)
{
    *pd_ptr = (PacketDescriptor) blockingReadBB(receiver_array.pending_receives[pid]);
}

int  nonblocking_get_packet(PacketDescriptor* pd_ptr, PID pid)
{
    return nonblockingReadBB(receiver_array.pending_receives[pid], pd_ptr);
}
