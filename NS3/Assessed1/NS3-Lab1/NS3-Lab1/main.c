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
#include <stdlib.h>
#include <stdio.h>



// Defining constants...
#define BUFFERLEN 65536
#define MAXPATHLENGTH 1024
#define PORT "8068"
#define BACKLOG 10
#define TESTMSG "Chunkiest of baconbaconbacon!"
#define PACKETSIZE 512 // Packet size in bytes.

// Because there's no reason not to use true/false...
typedef enum { false, true } bool;

// Getting horrible path problems, so saving the working directory here.
const char *abs_path;


int send_server_error(int connfd) {
    if(send(connfd, "HTTP/1.0 500 INTERNAL SERVER ERROR\r\n\r\nInternal Server Error.", 34,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}

void report_errno(char *message) {
    long length = strlen(message);
    int index = 0;
    int errno_saved = errno;
    fprintf(stderr, "\n");
    for (;index < length; index++) {
        fprintf(stderr, "%c", message[index]);
    }
    fprintf(stderr, "\nErrno was: %d\n", errno_saved);
}


long get_file_contents(char *filepath_in, char *file_contents) {
    char *filepath = (char *)malloc(strlen(filepath_in) + strlen(abs_path));
    filepath = strcat(abs_path, filepath_in);
    printf("Test.\n");
    printf("Filepath: %s\n\n", filepath);
    printf("Attempting open()...");
    int fd = open(filepath, O_RDONLY);
    report_errno("Error opening.");
    long read_status = read(fd, file_contents, BUFFERLEN);
    report_errno("Error reading.");
    printf("Through to the other side.\nFile contents: %s\n", file_contents);
    /*
    printf("%s\n.", filepath_in);
    FILE *fd = fopen(filepath_in, O_RDONLY);
    if (fd == NULL) {
        report_errno("Problem opening designated file.");
    }
    
    //printf("\n%d\n", filefd);
    long file_length = fread(fd, file_contents, BUFFERLEN, 0);
    printf("\n%ld\n", file_length);
    return file_length;*/
    return 0;
}


int send_file_not_found(int connfd) {
    if(send(connfd, "HTTP/1.0 404 FILE NOT FOUND", 27,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}



int send_packets(int connfd, char *file_contents, char* filepath) {
    printf("sending packets\n");
    int offset;
    long bytes_to_send = get_file_contents(filepath, file_contents);
    printf("got file contents\n%s\n", file_contents);
    char *message = (char *) malloc(BUFFERLEN);
    message = "HTTP/1.1 200 OK \r\n\r\n";
    send(connfd, message, strlen(message), 0);
    printf("hit\n");

    bytes_to_send = strlen(file_contents);
    printf("test\n");
    
    printf("\n\n%s\n\n", message);
    
    if (bytes_to_send == 0) {
        send_file_not_found(connfd);
    } else if (bytes_to_send == -1) {
        send_server_error(connfd);
    }
    
    for (offset = 0; offset < bytes_to_send; offset += PACKETSIZE) {
        int to_send = PACKETSIZE;
        if (offset + PACKETSIZE > bytes_to_send) {
            to_send = bytes_to_send % PACKETSIZE;
        }
        long amount_sent = send(connfd, file_contents, to_send, offset);
        if (amount_sent == -1) {
            char *error_message = "Error sending to socket with file descriptor %d.\n";
            report_errno(error_message);
            return -1;
        }
    }
    
    return 0; // Everything went smoothly!

}



void prevent_interrupts(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}




int check_request(char request[BUFFERLEN]) {
    if (request[0] == 'G' && request[1] == 'E' && request[2] == 'T') return 200;
    return -1;
}

// Returns the length of the path in the request, puts the path in `char *filepath`.
int get_request_path(char request[BUFFERLEN], char *filepath, long filepath_length) {
    char *toReturn[MAXPATHLENGTH];
    memset(&toReturn, ' ', MAXPATHLENGTH);
    int index;
    for (index = 0; index < MAXPATHLENGTH; index++) {
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
        if (path_length == 0) filepath = "index.html";
        char *file_contents = (char *) malloc(BUFFERLEN);
        memset(file_contents, ' ', BUFFERLEN);
        
        // Send the packets, one packet at a time.
        if (send_packets(connfd, file_contents, filepath) == -1) {
            char *message = "Had trouble sending packets. ";
            report_errno(message);
            return -1;
        };
        
        printf("Sent all packets!\n");
		
    }
    
    return 0;
}

int process_request(struct sockaddr_storage client_addr, int connfd) {
    if (!fork()) { // This is a child process
        char request[BUFFERLEN];
        memset(&request, ' ', BUFFERLEN);
        long recv_response = recv(connfd, request, BUFFERLEN, 0);
        if (recv_response == -1) {
            char *message = "Error reading! Errno: %d.\n";
            report_errno(message);
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
        close(connfd);
    }
}


int main(int argc, const char * argv[]) {
    
    // Before we do anything else, let's be aware of the path we're at.
    printf("%s\n", argv[0]);
    abs_path = (const char *)malloc(strlen(argv[0]) + 1);
    abs_path = strcat((char *)argv[0], "/");
    
    // Get address information.
    struct addrinfo hints, *server_info_results, *server_info;
    int serverfd = -1;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // Fill in the local IP.
    int getinfo_result = getaddrinfo(NULL, PORT, &hints, &server_info_results);
    if (getinfo_result == -1) {
        char *message = "Error getting local server information. Errno: %d";
        report_errno(message);
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
        char *message = "Had an error listening! Errno code: %d.\n";
        report_errno(message);
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
