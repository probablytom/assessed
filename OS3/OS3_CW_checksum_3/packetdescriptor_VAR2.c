/*
 * Author:    Peter Dickman
 * Version:   1.0
 * Last edit: 2003-02-18
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

#include "packetdescriptor.h"

#include <stdio.h>

typedef struct packetdescriptor {

  Destination the_destination;
  PID         the_pid;

  char data[SIZEOF_PacketDescriptor_target  - sizeof(PID) - sizeof(Destination)];

} RealPacketDescriptor;


/* Resets the packet descriptor to be empty.        */
/* Should be used before registering a descriptor   */
/* with the NetworkDevice.                          */
// WV: This is the function you should select/complete
void init_packet_descriptor(PacketDescriptor pdp)
{
   if (sizeof(RealPacketDescriptor) != SIZEOF_PacketDescriptor_target) {
     fprintf(stderr, "Warning: datatype size mismatch in __FILE__ at line __LINE__");
   // Set pid to 0

   // Set destination to NULL
   packet_descriptor_set_destination(pdp, NULL);
   }

}


/* The next few functions are used to set and get the destination info     */
/* Packets are routed to a Destination and then to the indicated PID there */

void packet_descriptor_set_pid(PacketDescriptor * pdp, PID id)
{
    (*((RealPacketDescriptor **) pdp))->the_pid = id;
}

void packet_descriptor_set_destination(PacketDescriptor * pdp, Destination d)
{
    (*((RealPacketDescriptor **) pdp))->the_destination = d;
}

PID         packet_descriptor_get_pid(PacketDescriptor * pdp)
{
    return     (*((RealPacketDescriptor **) pdp))->the_pid;
}

Destination packet_descriptor_get_destination(PacketDescriptor * pdp)
{
    return     (*((RealPacketDescriptor **) pdp))->the_destination;
}


/* Routines for manipulating the actual data are omitted, we don't bother */
/* sending actual data in this testharness.........                       */

