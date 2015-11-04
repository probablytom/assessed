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
/*
 * Performs the construction of Packet Descriptors from raw memory
 * and inserts them into the indicated FreePacketDescriptorStore
 *
 */

#include "packetdescriptorcreator.h"

#include "freepacketdescriptorstore.h"
#include "packetdescriptor.h"

#include "diagnostics.h"

void create_free_packet_descriptors(FreePacketDescriptorStore fpds,
				    void *                    mem_start,
				    unsigned long             mem_length)
{
    int    count   = 0;
    char * cur_ptr = (char *) mem_start;

    while (mem_length >= SIZEOF_PacketDescriptor_target)
    {
	if (nonblocking_put_pd(fpds, (PacketDescriptor) cur_ptr))
            count++;
	else
	    DIAGNOSTICS("Warning: failed to insert newly created packet descriptor into store\n");

	cur_ptr    += SIZEOF_PacketDescriptor_target;
        mem_length -= SIZEOF_PacketDescriptor_target;
    }

    DIAGNOSTICS("Info: built %d descriptors (of length %d) from %lu bytes of memory\n",
		count, SIZEOF_PacketDescriptor_target, mem_length);
}
