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
//
//

#include <stdio.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/socket.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <stdlib.h>
#include <strings.h>



// Defining constants...
#define BUFFERLEN 65536
#define MAXPATHLENGTH 1024
#define MAXHOSTLENGTH 1024
#define PORT "8068"
#define BACKLOG 10
#define PACKETSIZE 512 // Packet size in bytes.


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


int send_file_not_found(int connfd) {
    if(send(connfd, "HTTP/1.0 404 FILE NOT FOUND\r\n\r\n404 FILE NOT FOUND", 27,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}



long get_file_contents(char *filepath, char *file_contents) {
    // Check the file actually exists.
    struct stat file_data;
    if (stat(filepath, &file_data) == 0) {
        FILE  *fd = fopen(filepath, "r");
        long file_length = 0;

        int char_read = getc(fd);
        while( char_read != EOF ) {
            *(file_contents+file_length) = char_read;
            char_read = getc(fd);
            file_length++;
        }
        return file_length;
    } else {
        // File did not exist, return a 404.
        return 0;
    }

}

int send_400_response(int connfd) {
    if(send(connfd, "HTTP/1.1 400 BAD REQUEST\r\n\r\n400 BAD REQUEST", 27,0 ) == -1) {
        // Error here too!
        return -1;
    }
    return 0;
}


int send_packets(int connfd, char *file_contents, char* filepath) {
    printf("sending packets\n");
    int offset;
    long bytes_to_send = get_file_contents(filepath, file_contents);
    char *message = (char *) malloc(BUFFERLEN);
    message = "HTTP/1.1 200 OK \r\n\r\n";
    send(connfd, message, strlen(message), 0);

    if (bytes_to_send == 0) {
        send_file_not_found(connfd);
    }

    long to_send = 0;

    for (offset = 0; offset < bytes_to_send; offset += PACKETSIZE) {
        to_send = PACKETSIZE;
        if (offset + PACKETSIZE > bytes_to_send) {
            to_send = bytes_to_send % PACKETSIZE;
        }
        long amount_sent = send(connfd, file_contents+offset, (size_t)to_send, 0);
        if (amount_sent == -1) {
            char *error_message = "Error sending to socket with file "\
                        "descriptor %d.\n";
            report_errno(error_message);
            return -1;
        }
    }

    return 0; // Everything went smoothly!

}


int check_request(char request[BUFFERLEN]) {
    if (request[0] == 'G' && request[1] == 'E' && request[2] == 'T') return 200;
    else return 400;
}

// Returns the length of the path in the request, puts the path in `char *filepath`.
int get_request_path(char request[BUFFERLEN], char *filepath) {
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


// Loop through the request until we get to a sequence that looks like "\nHost:".
char *get_request_hostname(char request[BUFFERLEN]) {

    // Initial setup.
    char temp[6];
    char *hostname = malloc(1024);
    int index = 0;
    int inner_index;
    for (; index < 6; index++) {
        temp[index] = request[index];
    }

    // Actual looping.
    for (index = 6; index < BUFFERLEN; index++) {
        for (inner_index = 0; inner_index < 5; inner_index++) {
            temp[inner_index] = temp[inner_index + 1];
        }
        temp[5] = request[index];

        // If we have the host, get the hostname and return it.
        if (strcmp(&temp[0], "\nHost:") == 0) {

            // Move index from the chars between the "host" and the hostname.
            index++;
            if (request[index] == ' ') index++;

            // Loop to get the hostname, and return it.
            for (inner_index = 0; inner_index < 1024; inner_index++){
                // We're at the end of the hostname at ':', '\r' or '\n'.
                if (request[index] == ':' || request[index] == '\r' || request[index] == '\n') {
                    return hostname;
                }
                *(hostname + inner_index) = request[index];
                index++;
            }

        }
    } // Either we reached the end, or the hostname wasn't here.
    return NULL;
}

int send_200_request(int connfd, char request[BUFFERLEN]) {
    printf("Sending 200 response.\n");
    char *filepath = (char *) malloc(sizeof(char) * MAXPATHLENGTH);
    int path_length = get_request_path(request, filepath);
    if (path_length == -1) {
        report_errno("Error getting filepath.");
        send_server_error(connfd);
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
        
    }
    return 0;
}

int process_request(int connfd, char *hostname) {
    if (!fork()) { // This is a child process

        char request[BUFFERLEN];
        memset(&request, ' ', BUFFERLEN);
        long recv_response = recv(connfd, request, BUFFERLEN, 0);

        // If we have a bad hostname on this request, we can immediately exit.
        char *request_hostname = get_request_hostname(request);
        printf("%s\n%s\n", request_hostname, hostname);
        if (strcmp(hostname, request_hostname) == 0) {
            send_400_response(connfd);
            return 0; // We sent a 400, but we dealt with it correctly, so no actual errors.
        }

        if (recv_response == -1) {
            char *message = "Error reading! Errno: %d.\n";
            report_errno(message);
            return -1;
        }
        if(check_request(request) == 200) {
            send_200_request(connfd, request);
        } else {
            send_400_response(connfd);
        }

    }

    // The request's been processed now, so we no longer need our connection.
    printf("Closing connection %d.\n", connfd);
    close(connfd);
    return 0;
}

int acceptConnections(int serverfd, char *hostname) {
    // Now that we have a socket, let's accept a connection to it.
    int connfd;
    struct sockaddr_storage client_addr;
    socklen_t client_addr_len = sizeof client_addr;
    while(1) {
        connfd = accept(serverfd, (struct sockaddr *)&client_addr, &client_addr_len);
        if (connfd == -1) {
            report_errno("Error accepting connection.");
            break;
        } else {
            printf("Connection made!\n");
        
            // Process the request found at the connection, and return a server error if something goes wrong.
            if (process_request(connfd, hostname) == -1) {
                printf("Error processing request, sending server error message.\n");
                send_server_error(connfd);
            }
        }
        close(connfd);
    }
    // If we've got this, there's been an error accepting the connection.
    return -1;
}


int main(int argc, const char * argv[]) {

    // Get address information.
    struct addrinfo hints, *server_info_results, *server_info;
    int serverfd = -1;
    char *hostname = (char *)malloc(1024);
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
            continue;
        }

        int set_sock_opt = 1;
        if (setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR, &set_sock_opt, sizeof(int)) == -1) {
            // We hit an error setting options on this socket.
            continue;
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
    printf("We're on the other side...");

    gethostname(hostname, MAXHOSTLENGTH);
    struct hostent hostname_data = *gethostbyname(hostname);
    hostname = hostname_data.h_name;
printf("Hostname: ");
    printf("%s\n", hostname);

    // We've finished with out server_info struct, so let's free it!
    freeaddrinfo(server_info);
    
    // Sockets are at their most useful when they are being listened to.
    int listen_result = listen(serverfd, BACKLOG);
    if (listen_result == -1) {
        char *message = "Had an error listening! Errno code: %d.\n";
        report_errno(message);
    }

    
    // Loop for accepting.
    while(1) {
        if (acceptConnections(serverfd, hostname) == -1) {
            fprintf(stderr, "We got ourselves an error accepting "\
                    "connections! Aborting all plans.\n");
            break;  // Error, occurred, bomb out.
        }
    }

    // Something went wrong. We'll kill the server here.
    close(serverfd);
    return 1;  //Error.

}
