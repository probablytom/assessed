#include <stdlib.h>
#include "generic_queue.h"

/*
 * Authors:    Peter Dickman and Joe Sventek
 *
 * Generic queue package (can add at rear and remove at front only)
 */

/*
 * those who use this package don't know how generic_queue
 * is implemented, they just see a GQueue *
 */

/*******************/
/* Data Structures */
/*******************/

typedef struct queue_component {
    struct queue_component *next;
    GQueueElement contents;
} QueueComponent;

struct gqueue { 
    QueueComponent *head;
    QueueComponent *tail;
    unsigned long length;
};

/********************************/
/* Constructors and Destructors */
/********************************/

/* can create and destroy empty GQueue */
/* create returns NULL if unsuccessful, GQueue * if successful
 * destroy returns 1 if successful, 0 if unsuccessful  */

GQueue *create_gqueue(void) {
  GQueue *answer = (GQueue *)malloc(sizeof(GQueue));

  if (answer != NULL) {
    answer->head = NULL;
    answer->tail = NULL;
    answer->length = 0;
  }
  return answer;
}

/*
 * only destroy queues that are empty; return false if non-empty
 */
int destroy_gqueue(GQueue *gq) {
  if (gq->length == 0) {
    free((void *)gq);
    return 1;
  } else
    return 0;
}

/***********/
/* Methods */
/***********/

/* Can interrogate list for length */

unsigned long gqueue_length (GQueue *gq) {
  return gq->length;
}

/* Can add to rear of list or remove from front */
/* in each case return true if successful, false if not   */

int gqueue_enqueue (GQueue *gq, GQueueElement gqe) {
  QueueComponent *p = (QueueComponent *)malloc(sizeof(QueueComponent));

  if (p == NULL)
    return 0;
  p->contents = gqe;
  p->next = NULL;

  /*
   * if length was non-zero must chain old tail onto new element
   * if length was zero must set head pointer too
   */
  (gq->length++)  ?  (gq->tail->next = p)  :  (gq->head = p);
  gq->tail = p;

  return 1;
}

int gqueue_dequeue (GQueue *gq, GQueueElement *gqep) {

  if (gq->length) {
    QueueComponent *old_head = gq->head;

    *gqep = gq->head->contents;
    gq->head = gq->head->next;
    free((void *) old_head);

    /* if we emptied the list fix the tail ptr too */
    if (!(--gq->length))
      gq->tail = NULL;
    return 1;
  } else
    return 0;
}


/* Can also discard queue contents */
/* WARNING: purge doesn't free the space indicated by */
/*          the pointers held for client! That would  */
/*          need use of map (as in purge_carefully)   */
/*          with a destructor first                   */

void gqueue_purge (GQueue *gq) {
  QueueComponent *old_head; 

  while ( gq->length-- > 0 ) {
    old_head = gq->head;
    gq->head = gq->head->next;
    free((void *) old_head);
  };

  gq->tail = NULL;
}

/********************/
/* Advanced Methods */
/********************/

/*---------------------------------------------------------*/
/* map applies the function provided as second arg to each */
/* value stored in the queue in turn, from head to tail.   */
/* Note that the function whose address is passed must     */
/* take a GQueueElement as argument and return nothing.    */

void gqueue_map(GQueue *gq, void (*fp)(GQueueElement)) {
  QueueComponent *target = gq->head;

  while (target != NULL) {
    (*fp)(target->contents);
    target = target->next;
  };
}

/*------------------------------------------------------------*/
/* discards queue contents having applied cleaner routine to   */
/* each of the stored items in turn; cleaner should free them */

void gqueue_purge_carefully(GQueue *gq, void (*cleaner)(GQueueElement gqe)) {
  QueueComponent *old_head; 

  while ( gq->length-- > 0 ) {
    old_head = gq->head;
    (*cleaner)(gq->head->contents);
    gq->head = gq->head->next;
    free((void *) old_head);
  };

  gq->tail = NULL;
}
