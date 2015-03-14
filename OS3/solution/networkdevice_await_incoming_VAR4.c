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

	while (!rnd->pd_filled)	{

	}
	rnd->pd_registered = 0;

	rnd->thread_waiting--;
    }
   
    pthread_mutex_unlock(&rnd->in_lock);
}

/*------------------------------------------------------------------------*/

