//
//  main.c
//  NS3-Lab1
//
//  Created by Tom Wallis (matric no. 2025138)
//  Copyright (c) 2015 Tom Wallis. All rights reserved.
//
//  Of note: development was helped by the guide at http://beej.us/guide/bgnet/output/html/singlepage/bgnet.html
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
#include <sys/wait.h>
#include <arpa/inet.h>
#include <sys/stat.h>
#include <fcntl.h>

// Defining constants...
#define BUFFERLEN 65536
#define PORT "8080"
#define BACKLOG 10
#define TESTMSG "Chunkiest of baconbaconbacon!"



void sigchld_handler(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}

void process_request(struct sockaddr_storage client_addr, int connfd) {
    if (!fork()) { // This is a child process
        char request[BUFFERLEN];
        memset(&request, '\0', BUFFERLEN);
        int recv_response = recv(connfd, request, BUFFERLEN, 0);
        if (recv_response == -1) {
            int errno_saved = errno;
            fprintf(stderr, "Error reading! Errno: %d.\n", errno_saved);
        }
        if (send(connfd, &recv_response, strlen(TESTMSG), 0) == -1) {
            fprintf(stderr, "Error sending to socket with file descriptor %d.\n", connfd);
        }
    }
}

int acceptConnections(int serverfd) {
    // Now that we have a socket, let's accept a connection to it.
    while(1) {
        int connfd;
        struct sockaddr_storage client_addr;
        socklen_t client_addr_len = sizeof client_addr;
        connfd = accept(serverfd, (struct sockaddr *)&client_addr, &client_addr_len);
        if (connfd == -1) {
            int errno_saved = errno;
            fprintf(stderr, "Having trouble accepting the connection! Errno: %d\n", errno_saved);
            return -1;
        } else
            printf("Connection made!\n");
        process_request(client_addr, connfd);
        close(connfd);
        return 0;
    }
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
    
    // Please, god, let this work.
    struct sigaction sa;
    sa.sa_handler = sigchld_handler; // reap all dead processes
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
        fprintf(stderr,"sigaction");
        return 1;
    }
    
    // Loop for accepting.
    while(1){
        if (acceptConnections(serverfd) == -1) {
            fprintf(stderr, "We got ourselves an error accepting connections! Aborting all plans.");
            break;
        }
    }
    close(serverfd);
    
    return 0;
    
    
}


