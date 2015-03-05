//
//  main.c
//  NS3-Lab1
//
//  Created by Tom Wallis (matric no. 2025138)
//  Copyright (c) 2015 Tom Wallis. All rights reserved.
//
//  Of note: development was helped by the guide at http://beej.us/guide/bgnet/output/html/singlepage/bgnet.html
//
//
//  TODO: Catch all buffer overflows.
//
//

#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <signal.h>
#include <arpa/inet.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
//#include <sys/malloc.h>
#include <stdlib.h>
#include <stdio.h>



// Defining constants...
#define BUFFERLEN 65536
#define MAXPATHLENGTH 1024
#define PORT "8025"
#define BACKLOG 10
#define TESTMSG "Chunkiest of baconbaconbacon!"

// Because there's no reason not to use true/false...
typedef enum { false, true } bool;


int send_server_error(int connfd) {
    if(send(connfd, "HTTP/1.0 500 INTERNAL SERVER ERROR", 34,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}

int send_file_not_found(int connfd) {
    if(send(connfd, "HTTP/1.0 404 FILE NOT FOUND", 27,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}

void prevent_interrupts(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}

long get_file_contents(char* filepath, int path_length, char* source) {
	printf("opening...\n");
    FILE *fp = fopen(strcat("./", filepath), "r");
	printf("open.\n");
    long amount_read;
    size_t newLen;
    if (fp != NULL) {
        newLen = fread(source, sizeof(char), BUFFERLEN, fp);
        if (newLen == 0) {
            fputs("Error reading file", stderr);
            return -2;
        } else {
            source[++newLen] = '\0'; 
            amount_read = newLen;
        }
        
        fclose(fp);
    } else {
        return -2; // here, -2 specifically means that the file wasn't found or there was an error reading it.
    }
    return amount_read;
}


int check_request(char request[BUFFERLEN]) {
    if (request[0] == 'G' && request[1] == 'E' && request[2] == 'T') return 200;
    return -1;
}

int get_request_path(char request[BUFFERLEN], char *filepath, long filepath_length) {
    char *toReturn[MAXPATHLENGTH];
    memset(&toReturn, ' ', MAXPATHLENGTH);
    int index;
    for (index = 0; index < MAXPATHLENGTH; index++) {
        //printf("index: %d\tChar: %c\treqChar: %c\n", index, *(filepath+index), request[index+5]);
        if (request[index+5] == ' ') {
            return index;
        } else
        *(filepath+index) = request[index+5];
    }
    return -1; //  WE MAXED OUT THE BUFFER AND SHOULD DEAL WITH THIS ERROR!
}

int send_200_request(int connfd, char request[BUFFERLEN]) {
    printf("Sending 200.\n");
    char *filepath = (char *) malloc(sizeof(char) * MAXPATHLENGTH);
    int path_length = get_request_path(request, filepath, MAXPATHLENGTH);
    if (path_length == -1) {
        fprintf(stderr, "Error getting file path %d.\n", connfd);
        return -1;
    } else {
        printf("Got file path.\n");
        char *file_contents = (char *) malloc(BUFFERLEN);
        //long file_length = get_file_contents("./index.html", path_length, file_contents);
        //printf("Got file contents.\n");
        //if (file_length == -2) printf("The file could not be read.\n");
        //printf("%s\n", file_contents);
        //long amountSent = send(connfd, file_contents, file_length, 0);
        //printf("%ld\n", amountSent);
		int errno_saved;
		errno_saved = errno; printf("%d\n", errno_saved);
        int filefd = open("index.html", O_RDONLY);
		errno_saved = errno; printf("%d\n", errno_saved);
		printf("%d\n", filefd);
		int file_length = read(filefd, file_contents, BUFFERLEN);
		errno_saved = errno; printf("%d\n", errno_saved);
        printf("Sending...\n");
		long amount_sent = send(connfd, file_contents, file_length, 0);
		errno_saved = errno; printf("%d\n", errno_saved);
		//int offset = 0;
		//for (;offset<;offset += 1024) {}
        //long amountSent = sendfile(filefd, connfd, 0, 0, NULL, 0);
        printf("Sent?\n");
        if (amount_sent == -1) {
            fprintf(stderr, "Error sending to socket with file descriptor %d.\n", connfd);
            return -1;
        } else {
            printf("Sent file...?\n");
        }
    }
    
    return 0;
}

int process_request(struct sockaddr_storage client_addr, int connfd) {
    if (!fork()) { // This is a child process
        char request[BUFFERLEN];
        memset(&request, ' ', BUFFERLEN);
        long recv_response = recv(connfd, request, BUFFERLEN, 0);
        if (recv_response == -1) {
            int errno_saved = errno;
            fprintf(stderr, "Error reading! Errno: %d.\n", errno_saved);
            return -1;
        }
        if(check_request(request) == 200) {
            send_200_request(connfd, request);
        } else {
            printf("%s\n", request);
            send_server_error(connfd);
        }
        
    }
    return 0;
}

int acceptConnections(int serverfd) {
    // Now that we have a socket, let's accept a connection to it.
    int connfd;
    struct sockaddr_storage client_addr;
    socklen_t client_addr_len = sizeof client_addr;
    while(1) {
        connfd = accept(serverfd, (struct sockaddr *)&client_addr, &client_addr_len);
        if (connfd == -1) {
            printf("Error accepting connection; continuing out of stubbornness.\n");
            continue;
        } else {
            printf("Connection made!\n");
        
            // Process the request found at the connection, and return a server error if something goes wrong.
            if (process_request(client_addr, connfd) == -1) {
                printf("Error processing request, sending server error message.\n");
                send_server_error(connfd);
            }
        }
    }
    // Clean up. We only hit this if there's an error.
    int errno_saved = errno;
    fprintf(stderr, "Having trouble accepting the connection! Errno: %d\n", errno_saved);
    close(connfd);
    return -1;

}


int main(int argc, const char * argv[]) {
    
    // Get address information.
    struct addrinfo hints, *server_info_results, *server_info;
    int serverfd = -1;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // Fill in the local IP.
    int getinfo_result = getaddrinfo(NULL, PORT, &hints, &server_info_results);
    if (getinfo_result == -1) {
        int errno_saved = errno;
        fprintf(stderr, "Error getting local server information. Errno: %d", errno_saved);
    }
    
    
    
    for(server_info = server_info_results; server_info != NULL; server_info = server_info->ai_next) {
        if ((serverfd = socket(server_info->ai_family, server_info->ai_socktype,
                               server_info->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }
        
        // reuse addresses
        int set_sock_opt = 1;
        if (setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR, &set_sock_opt, sizeof(int)) == -1) {
            perror("setsockopt");
            return 1;
        }
        
        if (bind(serverfd, server_info->ai_addr, server_info->ai_addrlen) == -1) {
            fprintf(stderr, "Error binding!\n");
            continue;
        }
        
        break;
    }
    
    fprintf(stdout, "Current serverfd: %d.\n", serverfd);
    
    // Check to see if we bind()ed successfully or ran out of options.
    if (server_info == NULL) {
        printf("We found no valid socket to bind to.\n");
        return 1; // Error.
    }
    
    // Sockets are at their most useful when they are being listened to.
    int listen_result = listen(serverfd, BACKLOG);
    if (listen_result == -1) {
        int errno_saved = errno;
        printf("Had an error listening! Errno code: %d.\n", errno_saved);
    }
    
    // This appears to be entirely unncessesary.
    
    // Making sure connections don't get interrupted later (this segment from http://beej.us/guide/bgnet/output/html/singlepage/bgnet.html -- credit where it's due.)
    struct sigaction sa;
    sa.sa_handler = prevent_interrupts; // reap all dead processes
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
        perror("sigaction");
        return 1; // Error...
    }
    
    
    // Loop for accepting.
    while(1){
        if (acceptConnections(serverfd) == -1) {
            fprintf(stderr, "We got ourselves an error accepting connections! Aborting all plans.\n");
            return 1; // Error, but we might not have a connfd, so let's not send a 500, eh?
            break;
        }
    }
    close(serverfd);
    
    return 0;
    
}
