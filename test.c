
#include "mentry.c"
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {

  MEntry *me = me_get(stdin);
  
  me_print(me, stdout);

  printf("%s\n", me->surname);
  printf("%d\n", me->house_number);
  
}
