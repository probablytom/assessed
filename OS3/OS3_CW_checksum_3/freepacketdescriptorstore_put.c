/*
 * Author:    Peter Dickman
 * Version:   1.0
 * Last edit: 2003-02-18
 * Modified: 2011-02-22 by J Sventek
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
 * An unbounded store for Packet Descriptors
 * which is safe for concurrent access using PThreads....
 */

/* here's the spec we must satisfy */
#include "freepacketdescriptorstore__full.h"


#include <pthread.h>
#include <stdlib.h>

#include "diagnostics.h"
#include "generic_queue.h"

#include "packetdescriptor.h"


/*----------- Put Methods ---------------*/

static void put_pd(FreePacketDescriptorStore fpds, PacketDescriptor pd) {
   int response = 0;
   real_fpds rfpds = (real_fpds) fpds;

   pthread_mutex_lock(&rfpds->lock);
   response = gqueue_enqueue(rfpds->basic_store, (GQueueElement) pd);
   if (response) {
       rfpds->current_length++;
       pthread_cond_signal(&rfpds->non_empty);
   } else
       DIAGNOSTICS("BUG: Failed to insert element into FPDS\n");
   pthread_mutex_unlock(&rfpds->lock);
}

void blocking_put_pd(FreePacketDescriptorStore fpds, PacketDescriptor pd) {
  put_pd(fpds, pd);
}

int nonblocking_put_pd(FreePacketDescriptorStore fpds, PacketDescriptor pd) {
  put_pd(fpds, pd);
  return 1;
}

/*---------------------------------------*/
