#ifndef NETWORK_DRIVER_HDR
#define NETWORK_DRIVER_HDR

/*
 * Author:    Peter Dickman
 * Version:   1.4
 * Last edit: 2003-02-25
 *
 * This file is a component of the test harness and or sample
 * solution to the NetworkDriver exercise (re)developed in February 2003
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


#include "packetdescriptor.h"
#include "destination.h"
#include "pid.h"
#include "freepacketdescriptorstore.h"

#include "networkdevice.h"
#include "BoundedBuffer.h"

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sched.h>

/*
 * Use a few static data structures, if made into an ADT these would
 * be instance variables.....
 */

#define SEND_BUFFER_LENGTH      10
#define RECEIVER_POOL_LENGTH	 4
#define RECEIVED_BUFFER_LENGTH   2


struct receiver_arg_t {
    FreePacketDescriptorStore fpds;
    NetworkDevice nd;
    struct receiver_array_t *receiver_array;
    BoundedBuffer receiver_pool;
};

struct send_arg_t {
   FreePacketDescriptorStore fpds;
   NetworkDevice nd;
   BoundedBuffer pending_sends;
   BoundedBuffer receiver_pool;
};

struct receiver_array_t {
   BoundedBuffer pending_receives[MAX_PID+1];
};

static BoundedBuffer pending_sends;	/* queue of pending send PDs */
static BoundedBuffer receiver_pool;	/* queue of PD free pool for receives */

static struct receiver_array_t receiver_array;	/* array of pending receives */

static pthread_t sender_thread;		/* the sending thread */
static pthread_t receiver_thread;	/* the receiving thread */
static struct send_arg_t sender_args;
static struct receiver_arg_t receiver_args;


#if 1
 int nb_get_pd(BoundedBuffer pool, FreePacketDescriptorStore fpds,
	             PacketDescriptor *pd) ;

 int nb_put_pd(BoundedBuffer pool, FreePacketDescriptorStore fpds,
	             PacketDescriptor pd) ;
#endif
void *sender_fn (void *args);
void *receiver_fn (void *args);


void blocking_send_packet(PacketDescriptor);
int  nonblocking_send_packet(PacketDescriptor);
/* These calls hand in a PacketDescriptor for dispatching */
/* The nonblocking call must return promptly, indicating whether or */
/* not the indicated packet has been accepted by your code          */
/* (it might not be if your internal buffer is full) 1=OK, 0=not OK */
/* The blocking call will usually return promptly, but there may be */
/* a delay while it waits for space in your buffers.                */
/* Neither call should delay until the packet is actually sent      */

void blocking_get_packet(PacketDescriptor*, PID);
int  nonblocking_get_packet(PacketDescriptor*, PID);
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

void init_network_driver(NetworkDevice               nd, 
                         void *                      mem_start, 
                         unsigned long               mem_length,
                         FreePacketDescriptorStore * fpds_ptr);
/* Called before any other methods, to allow you to initialise */
/* data structures and start any internal threads.             */ 
/* Arguments:                                                  */
/*   nd: the NetworkDevice that you must drive,                */
/*   mem_start, mem_length: some memory for PacketDescriptors  */
/*   fpds_ptr: You hand back a FreePacketDescriptorStore into  */
/*             which you have put the divided up memory        */
/* Hint: just divide the memory up into pieces of the right size */
/*       passing in pointers to each of them                     */ 

#endif
