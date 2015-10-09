/*
 * BoundedBuffer.c
 *
 * Implementation file for a bounded buffer implemented using pthread
 * facilities to allow it to be driven by concurrent threads.
 *
 * Uses standard tricks to keep it very generic.
 * Uses heap allocated data structures.
 *
 * Author: Peter Dickman
 *
 * Created: 7-3-2000
 * Edited:  28-2-2001
 *
 * Version: 1.0
 *
 * WARNING: Does not compile on OSF1 3.2C (as of 7/3/2000)
 *          due to incompatible defn of pthread_*_init routines 
 *          used in createBB routine below
 */

#include "BoundedBuffer.h"
#include <pthread.h>
#include <stdlib.h>

typedef Length Index;

typedef int      Boolean;
#define False (0)
#define True  (!False)

struct RealBoundedBuffer
{
    pthread_mutex_t lock;       /* protects buffer from multiple writers */
    pthread_cond_t  non_empty;  /* for consumer to await more stuff */
    pthread_cond_t  non_full;   /* for producer to await space for stuff */

    Length   maxlength;   /* size of buffer */
    Index    readP;       /* slot before one to read from (if data exists) */
    Index    writeP;      /* slot before one to write to (if space exists) */
    Boolean  emptyF;      /* flag true => buffer is empty */

    BufferedItem  *buffer;     /* storage for the actual stuff held */
};
/* NB: if readP==writeP, buffer is empty or full, use emptyF to show which */

typedef struct RealBoundedBuffer * BBptr;


BoundedBuffer createBB(Length length)
{
  BBptr newBB   = (BBptr)          malloc(sizeof(struct RealBoundedBuffer));
  newBB->buffer = (BufferedItem *) malloc(length*sizeof(BufferedItem));

  newBB->maxlength = length;
  newBB->writeP = newBB->readP = length-1;
  newBB->emptyF = True;

  /* WARNING: 
   * The init calls below assume an up to date Pthread implementation.
   * This code did not compile on OSF1 3.2C (guernsey on 7/3/2000).
   * Could be fixed with appropriate #ifdef, but only at cost of messy code.
   */
  pthread_mutex_init(&newBB->lock, NULL);
  pthread_cond_init(&newBB->non_full, NULL);
  pthread_cond_init(&newBB->non_empty, NULL);

  return (BoundedBuffer) newBB;
}

void          destroyBB(BoundedBuffer abstractBB)
{
  BBptr bb = (BBptr) abstractBB;

  free(bb->buffer);
  free(bb);
}

void          blockingWriteBB(BoundedBuffer abstractBB, BufferedItem value)
{
  BBptr bb = (BBptr) abstractBB;

  pthread_mutex_lock(&bb->lock);

  /* await space to store the value in */
  while ((bb->writeP==bb->readP) && (!bb->emptyF))
      pthread_cond_wait(&bb->non_full, &bb->lock);

  /* advance the write index, cycling if necessary, and store value */
  bb->buffer[bb->writeP = (bb->writeP + 1) % bb->maxlength] = value;

  /* the buffer definitely isn't empty at this moment */
  bb->emptyF = False;
  pthread_cond_signal(&bb->non_empty);

  pthread_mutex_unlock(&bb->lock);
}


int    nonblockingWriteBB(BoundedBuffer abstractBB, BufferedItem value)
{
  int response = 0;
  BBptr bb = (BBptr) abstractBB;

  pthread_mutex_lock(&bb->lock);

  /* store datum only if space exists to store the value in */
  if ((bb->writeP!=bb->readP) || (bb->emptyF))
  {
      /* advance the write index, cycling if necessary, and store value */
      bb->buffer[bb->writeP = (bb->writeP + 1) % bb->maxlength] = value;

      /* the buffer definitely isn't empty at this moment */
      bb->emptyF = False;
      pthread_cond_signal(&bb->non_empty);
      response = 1;
  }

  pthread_mutex_unlock(&bb->lock);

  return response;
}


BufferedItem  blockingReadBB(BoundedBuffer abstractBB)
{
  BufferedItem value;
  BBptr bb = (BBptr) abstractBB;

  pthread_mutex_lock(&bb->lock);

  /* await a value to hand to our caller */
  while (bb->emptyF)
      pthread_cond_wait(&bb->non_empty, &bb->lock);

  /* advance the read index, cycling if necessary, and retrieve value */
  value = bb->buffer[bb->readP = (bb->readP + 1) % bb->maxlength];

  /* the buffer definitely isn't full at this moment */
  bb->emptyF = (bb->readP == bb->writeP);
  pthread_cond_signal(&bb->non_full);

  pthread_mutex_unlock(&bb->lock);

  return value;
}

int nonblockingReadBB(BoundedBuffer abstractBB, BufferedItem * bi_ptr)
{
  int response = 0;
  BBptr bb = (BBptr) abstractBB;

  pthread_mutex_lock(&bb->lock);

  /* check whether there is a value to hand to our caller */
  if (!bb->emptyF)
  {
      /* advance the read index, cycling if necessary, and retrieve value */
      *bi_ptr = bb->buffer[bb->readP = (bb->readP + 1) % bb->maxlength];

      /* the buffer definitely isn't full at this moment */
      bb->emptyF = (bb->readP == bb->writeP);
      pthread_cond_signal(&bb->non_full);

      response = 1;
  }
  pthread_mutex_unlock(&bb->lock);

  return response;
}

void          mapBB(BoundedBuffer abstractBB, 
                    void (*mapFunction)(BufferedItem))
{
  Index i;
  BBptr bb = (BBptr) abstractBB;

  pthread_mutex_lock(&bb->lock);

  if (!bb->emptyF)        /* so readP==writeP => buffer is full */ 
      for (i = bb->readP; 
           i != bb->writeP; 
           i = (i + 1) % bb->maxlength)
      {
	  (*mapFunction)(bb->buffer[i]);
      }

  pthread_mutex_unlock(&bb->lock);
}
