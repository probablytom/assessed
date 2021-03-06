#ifndef _MENTRY_INCLUDED_
#define _MENTRY_INCLUDED_

#include <stdio.h>
#include <string.h>

typedef struct mentry {
	char *surname;
	int house_number;
	char *postcode;
	char *full_address;
  char *full_name; /* We need to print this later, so... */
} MEntry;

/* me_get returns the next file entry, or NULL if end of file*/
MEntry *me_get(FILE *fd);

/* me_hash computes a hash of the MEntry, mod size */
unsigned long me_hash(MEntry *me, unsigned long size);

/* me_print prints the full address on fd */
void me_print(MEntry *me, FILE *fd);

/* me_compare compares two mail entries, returning <0, 0, >0 if
 * me1<me2, me1==me2, me1>me2
 */
int me_compare(MEntry *me1, MEntry *me2);

#endif /* _MENTRY_INCLUDED_ */
