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
 * An unbounded store for Packet Descriptors
 * which is safe for concurrent access using PThreads....
 */

/* here's the spec we must satisfy */
#include "freepacketdescriptorstore__full.h"


//#include <pthread.h>
//#include <stdlib.h>

#include "diagnostics.h"
//#include "generic_queue.h"

#include "packetdescriptor.h"



/*----------- Management Methods ---------------*/

FreePacketDescriptorStore create_fpds(void) {
   real_fpds rfpds = (real_fpds) malloc(sizeof(struct fpds_header));

   rfpds->basic_store = create_gqueue();
   rfpds->current_length = 0;
   pthread_mutex_init(&rfpds->lock, NULL);
   pthread_cond_init(&rfpds->non_empty, NULL);
   return (FreePacketDescriptorStore) rfpds;
}

void destroy_fpds(FreePacketDescriptorStore fpds) {
   real_fpds rfpds = (real_fpds) fpds;

   destroy_gqueue(rfpds->basic_store);
   free((void *) rfpds);
}

/*----------- Get Methods ---------------*/
// See separate files

/*----------- Put Methods ---------------*/
// WV: This is the function you need to select/adapt
static void put_pd(FreePacketDescriptorStore fpds, PacketDescriptor pd) {
   int response = 0;
   real_fpds rfpds = (real_fpds) fpds;

   pthread_mutex_lock(&rfpds->lock);
   response = gqueue_enqueue(rfpds->basic_store, (GQueueElement) pd);
   if (response) {
       rfpds->current_length++;

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
