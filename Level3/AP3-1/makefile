finddupl: finddupl.o mlist.o mentry.o
	gcc finddupl.o mlist.o mentry.o -o finddupl

finddupl.o: finddupl.c mentry.h mlist.h
	gcc -c finddupl.c

mentry.o: mentry.c mentry.h
	gcc -c mentry.c

mlist.o: mlist.c mlist.h
	gcc -c mlist.c

clean:
	rm *.o finddupl test a.out
