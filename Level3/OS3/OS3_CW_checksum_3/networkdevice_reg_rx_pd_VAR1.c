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

#include "networkdevice__full.h"

#include <pthread.h>
#include <stdlib.h>

#include <sys/time.h>
#include <unistd.h>

#include "diagnostics.h"
#include "packetdescriptor.h"
#include "destination.h"
#include "pid.h"




void register_receiving_packetdescriptor(NetworkDevice nd, PacketDescriptor* pd_ptr)
/* tell the network device to use the indicated PacketDescriptor     */
/* for next incoming data packet; once a descriptor is used it won't */
/* be reused for a further incoming data packet; you must register   */
/* a PacketDescriptor before the next packet arrives                 */
{
    real_nd rnd = (real_nd) nd;

    pthread_mutex_lock(&rnd->in_lock);
    if (!rnd->pd_registered)
    {

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
