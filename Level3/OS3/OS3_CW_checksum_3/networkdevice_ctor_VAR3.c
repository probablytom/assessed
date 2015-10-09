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

static pthread_t device_indriver_thread;

NetworkDevice construct_network_device(void)
{
    real_nd rnd = (real_nd) malloc(sizeof(struct networkdevice));

    rnd->sending        = 0;
    rnd->pd_registered  = 0;
    rnd->pd_filled      = 0;
    rnd->thread_waiting = 0;

    pthread_mutex_init(&rnd->out_lock, NULL);
    pthread_cond_init(&rnd->send_timer, NULL);
    pthread_mutex_init(&rnd->in_lock, NULL);
    pthread_cond_init(&rnd->packet_received, NULL);



    return NULL;
}
