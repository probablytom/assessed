#include <stdlib.h>
#include "mentry.h"

// Define constants we will find useful, like the number of buckets we have.
#define NO_OF_BUCKETS 20
#define INITIAL_BUCKET_SIZE 20

int bucket_size = INITIAL_BUCKET_SIZE; // To be used later for resizing.
int ml_verbose; // As defined by specifications.

typedef struct mlist {
  int size;
  MEntry *bucket;
} MList;

MList *ml_create(void) {
  
  MList *ml = (MList *)malloc(sizeof(MList) * NO_OF_BUCKETS);
  
  // Populate the hashtable with MLists, and initialise their values. 
  int index = 0;
  for (; index < NO_OF_BUCKETS; index++) {

    // I have come to suspect that this line is, infact, unneccesary. 
    // Keeping it incase I need to add it back in and make it work, but the malloc above should do it...?
    //*(ml + index) = malloc(sizeof(MList));
    (ml + index)->size = bucket_size;
    (ml + index)->bucket = (MEntry *)malloc(sizeof(MEntry) * bucket_size);
    
  }

  return ml;
  
}

/* We're just going to add the MEntry to the hashtable, EVEN IF IT'S A DUPLICATE. 
   finddupl.c uses ml_lookup to detect duplicates, so we don't have to (and won't) worry about it here.  */
int ml_add(MList **ml, MEntry *me) {
  
  MList *hashtable = *ml;
  int bucket_id = me_hash(me, bucket_size);
  MList bucket_to_append = *(hashtable + bucket_id);
  // Cycle around ml_add to find the next empty space. Assume resizing will be implemented later. 
  // Note that ml_lookup is DELIBERATELY not used so we know where the most recent empty space in the bucket is. 
  // I think this is more efficient than running ml_lookup and traversing the array first before finding the place a second time. 
  int index = 0;
  while ((bucket_to_append.bucket + index) != (MEntry *)NULL) {
    if (me_compare((bucket_to_append.bucket + index), me) == 0) { 
      
      return 1; // As per specifications.
      
    }
    index++;
  } // We now have the index of the first empty space in this bucket. Put `*(me)` here.   
  
  *(bucket_to_append.bucket + index) = *me;
  
  // Done. 
  return 1;

}

MEntry *ml_lookup(MList *ml, MEntry *me) {
  
  // Cycle through the bucket from *ml and compare each element to *me. If it's the same, we're done.
  int index = 0;
  while((ml->bucket  + index) != (MEntry *)NULL) {
    if (me_compare((ml->bucket + index), me) == 0) { // If *me exists at this point in the bucket, we've found it.
      return (ml->bucket + index);
    } 
    index++; // Increment so we're looking at the next item in the table.
  }

  // If we got here, we didn't find it. Return NULL.
  return NULL;
  
}

// Destroy a MList and remove it from memory. 
void ml_destroy(MList *ml) {
  
  // traverse the list and delete MEntries as we find them.
  int index;
  for (index = 0; index < ml->size; index++) {
    me_destroy(ml->bucket + index);
  }
  
  // Destroy the ml itself.
  free(ml);

}
