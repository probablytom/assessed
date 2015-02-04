/*
 *
 * Tom Wallis, 2015
 * Lab1, Networked Systems
 * Notes and code for NS Labs
 *
 */

/*
 *
 * Headers to include to use Berkley sockets:
 * #include <sys/types.h>
 * #include <sys/socket.h>
 *
*/

/*
 *
 * We can get a "file descriptor" from the function `socket`:
 * int fd = socket(AF_INET, SOCK_STREAM, 0);
 * if (fd == -1) {
 *   // An error has occurred.
 * }
 *
 * AF_INET: this indicates the Inernet Address Family to be used. 
 *   AF_INET6 would make the socket IPv6; AF_INET is IPv4.
 *
 * SOCK_STREAM: Indicates that a TCP stream socket is desired. 
 *   If somebody wanted a UDP socket, we would use SOCK_DGRAM. 
 *
 * socket() will return -1 if an error has occured. If this is the case, the
 *   global variable `errno` will be set to indicate the type of error. 
 *   `socket()`'s man page lists the various possible errors. 
 *
 */

/*
 *
 * When making a socket into a TCP server, we should first mind that socket to 
 *   the appropriate port. We can do this using `bind()`, specifying:
 *
 *   	* A file descriptor
 *   	* An address
 *   	* A port to listen on for connections.
 *
 * if ( bind(fd, (struct sockaddr *) &addr, sizeof(addr)) == -1 ) {
 *   // an error occurred, we matched -1.
 * }
 *
 * As with `socket()`, -1 is returned upon an error and `errno` is set with the
 *   indicator of the error found. 
 *
 * The `addr` parameter for the `bind()` function is a pointer to a 
 *   `struct sockaddr`. This is defined as:
 *
 * struct sockaddr {
 *   uint8_t        sa_len;        // Address size
 *   sa_family_t    sa_family;     // Address type
 *   char           sa_data[22];   // Address itself
 * }
 *
 * `struct sockaddr` is intended to be generic. The sa_len and sa_family fields
 *   hold the size and type of the address, while the sa_data field is large 
 *   enough to hold any type of address. 
 * The `struct sockaddr` is not intended for direct use. Rather, if something 
 *   uses an IPv4 address, they would use a `struct sockaddr_in`. For an IPv6 
 *   address, it would use a `struct sockaddr_in6`. These can be found in the 
 *   handout these notes are from, and are defined in <netinet/in.h> header.
 * 
 */


/*
 * Code begins.
 */

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

// Create our file descriptor.
int fileDescriptor = socket(AF_INET, SOCK_STEAM, 0);
if (fileDescriptor == -1) {
	// An error occurred, deal with this.
}

// Some configuring (?)


// Bind to a socket.
if ( bind(fileDescriptor, (struct sockaddr *) &addr, sizeof(addr)) == -1 ) {
	// We hit an error, deal with this.
}


