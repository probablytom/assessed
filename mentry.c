/* Code by Tom Wallis from here on. */

#include <stdio.h>
#include <string.h>
#include <string.h>
#include <stdlib.h>
#include "mentry.h"


//MAYBE?!?!?!?!
/*typedef struct mentry {
	char *surname;
	int house_number;
	char *postcode;
	char *full_address;
  char *full_name; // We need to print this later, so... 
} MEntry;*/

#define LINELEN 80

/* A helper function for me_get(), and used in MList also to create MEntries with malloc'd space */
MEntry *me_create(void) {

  MEntry *newMEntry = (MEntry *)malloc(sizeof(MEntry));
  // We need to initialise all of the values in newMEntry.
  newMEntry->full_name = (char *)malloc(81 * sizeof(char)); // Say a full name can be at most 80 chars. Leave room for end of string char. 
  newMEntry->postcode = (char *)malloc(9 * sizeof(char)); // A postcode can be at most 8 characters.Leave room for end of string char. 
  newMEntry->house_number = 0; // An int. 
  newMEntry->surname = (char *)malloc(41 * sizeof(char)); // Say a surname can be at most 40 chars. Leave room for end of string char. 
  newMEntry->full_address = (char *)malloc(81 * sizeof(char)); // Say a full address can be at most 80 chars. Leave room for end of string char.
  
  // Return the malloc'd MEntry.
  return newMEntry;
  
}


/* A function to return a MEntry from some input file */
MEntry *me_get(FILE *fd) {
  
  if(fd != NULL) {
    MEntry *newMEntry = me_create();
    /*// We need to initialise all of the values in newMEntry.
    newMEntry->full_name = (char *)malloc(81 * sizeof(char)); // Say a full name can be at most 80 chars. Leave room for end of string char. 
    newMEntry->postcode = (char *)malloc(9 * sizeof(char)); // A postcode can be at most 8 characters.Leave room for end of string char. 
    newMEntry->house_number = 0; // An int. 
    newMEntry->surname = (char *)malloc(41 * sizeof(char)); // Say a surname can be at most 40 chars. Leave room for end of string char. 
    newMEntry->full_address = (char *)malloc(81 * sizeof(char)); // Say a full address can be at most 80 chars. Leave room for end of string char. */


    // Begin getting the first line. 

    char *temp = (char *)malloc(81 * sizeof(char));
    
    //Get the next line and store it in newMEntry->full_name, because that's what it is.  (TODO: fix formatting.)
    fgets(newMEntry->full_name, LINELEN, fd);

    /* Get the surname. */
    
    // For each character of the surname, copy it to a 
    int index = 0;
    while (*(newMEntry->full_name + index) != ',') {
      *(newMEntry->surname + index) = *(newMEntry->full_name + index);
      index++;
    }

    /* Surname got! */

    index = 0;

    /* Get and process the second line. */
    temp = (char *)malloc(81 * sizeof(char));
    fgets(newMEntry->full_address, LINELEN, fd);

    char *house_number_representation = (char *)malloc(sizeof(char) * 5); // Assuming that no house number can be greater than 5 decimal places long.

    /* Get the house number from the beginning of the full address. */
    while(*(newMEntry->full_address + index) != ' '){
      // Record each character of the house number in a seperate place so we can pass a pointer to it to `atoi`
      *(temp + index) = *(newMEntry->full_address + index);
      index++;
    }

    newMEntry->house_number = atoi(temp);

    // We're not going to use temp again, so let's get rid of it now. 
    free(temp);

    
    index = 0; // Reset index for next time. 


    /* Get the postcode. */
    fgets(newMEntry->postcode, 8, fd);
    /* Got the postcode! That was easy. */
    
    return newMEntry; // This is the modified thing. We're done. Woo. 
}

}

/* Prints the given MEntry to a file. */
void me_print(MEntry *me, FILE *fd) {
  fprintf(fd, "%s%s%s\n", me->full_name, me->full_address, me->postcode);
}

// A helper function for hashing.
unsigned long me_hash_nonpretty(MEntry *me) {
  
  /* The accumulator we will return. 
     This will, incidentally, have the house number added to it soon, so we do that now. */
  unsigned long hash_value = me->house_number;

  /* For indexing. */
  int index = 0;

  /* We find the other values in the strings, and add their ascii values. */
  while(me->surname[index])
    hash_value += me->surname[index++];
  index = 0;
  while(me->postcode[index])
    hash_value += me->postcode[index++];

  return hash_value;
  
}


/** 
/* A Hash function for MEntrys. */
/*INCOMPLETE INCOMPLETE INCOMPLETE INCOMPLETE INCOMPLETE*/
unsigned long me_hash(MEntry *me, unsigned long size) {

  unsigned long hash_value = me_hash_nonpretty(me);

  /* hash_value now contains the values from each character, and the postcode.
     This has to identify a MEntry, but no more, so we use the criteria for ...
      ...identifying a duplicate of a MEntry to do this. This is what hash_value now contains.
     HOWEVER. One must note that the hash value is far larger than the size it ought to be!
     We must therefore return it within the size constraints me_hash has been called with. */
  return hash_value % size;

}


/* Compare two MEntries. 
   ...so far as one can see, no criteria for MEntry ordering is actually given.
   Therefore, a standard is laid out HERE. The code should conform to this. 
   compare by surname first, If surnames are equal, compare by postcode. If postcodes are equal, compare by house number. If all three are equal, the MEntries are equal. */
int me_compare(MEntry *me1, MEntry *me2) {
  
  /* Check by surname first. */
  if (me1->surname > me2->surname) {
    return 1; // me1 is greater than me2 by specification above.
  } else if (me1->surname < me2->surname) {
    return -1; // me2 is greater than me1 s per speficication above. 
  } else {

    /* Now, let's check the postcodes. Surnames are equal. */
    if (me1->postcode > me2->postcode) {
      return 1; // Conditions laid out similarly to previous level of nesting, see specs. 
    } else if (me2->postcode > me1->postcode) {
      return -1; 
    } else {

      // The surname AND postcode are the same! Let's test the house numbers.       
      if (me1->house_number > me2->house_number) {
        return 1;
      } else if (me2->house_number > me1->house_number) {
        return -1;
      } else {

        return 0; // me1 and me2 are equal. 
      
      }
    }
  }
	
  // We've caught all possible conditions. End. 
}



/* me_destroy needs to go here! Read the specs, Tom! */
void me_destroy(MEntry *me) {
  
  // Destroy all of the things stored within the MEntry. 
  free(me->surname);
  free(me->full_name);
  free(&(me->house_number));
  free(me->postcode);
  free(me->full_address);

  // Free the MEntry itself. 
  free(me);
  
  //Done. 
  
}
