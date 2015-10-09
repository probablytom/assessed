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

#include "networkdevice__full.h"
#include "fakeapplications.h"

#include "packetdescriptor.h"
#include "freepacketdescriptorstore.h"
#include "networkdriver.h"

#include "diagnostics.h"
#include <pthread.h>
#include <stdlib.h>

void waitforever(void)
{
    pthread_mutex_t forever_lock;
    pthread_cond_t  forever_condition_variable;

    pthread_mutex_init(&forever_lock, NULL);
    pthread_cond_init(&forever_condition_variable, NULL);

    pthread_mutex_lock(&forever_lock);
    pthread_cond_wait(&forever_condition_variable, &forever_lock);

    DIAGNOSTICS("BUG: Returning from waitforever() call!\n");
}

#define UNUSED __attribute__ ((unused))

int main(UNUSED int argc, UNUSED char * argv[])
{
    FreePacketDescriptorStore fpds;
    NetworkDevice             nd;

    unsigned long mem_length = 30*SIZEOF_PacketDescriptor_target;
    void *        mem_start  = malloc(mem_length);
    DIAGNOSTICS("Info: Allocated memory for packet descriptor contents\n");

    DIAGNOSTICS("Info: Constructing network device...\n");
    nd = construct_network_device();
    DIAGNOSTICS("Info: Constructed network device.\n");
    
    DIAGNOSTICS("Info: Initialising network driver...\n");
    init_network_driver(nd, mem_start, mem_length, &fpds);
    DIAGNOSTICS("Info: Initialised network driver.\n");

    DIAGNOSTICS("Info: Constructing fake applications...\n");
    construct_fake_applications(fpds);
    DIAGNOSTICS("Info: Constructed fake applications.\n");

    waitforever();

    exit(EXIT_SUCCESS);    
}
