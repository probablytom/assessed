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

// Defining constants...
#define BUFFERLEN 65536
#define PORT 8080

int main(int argc, const char * argv[]) {
    // Set up recieving socket address...
    int portToServe = PORT;
    struct sockaddr_in addr;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(portToServe);
    
    // Setting up variables to recieve data into
    char listeningBuffer[BUFFERLEN];
    ssize_t listeningBytesRead;
    
    // TODO: GETADDRINFO() HERE! GETADDRINFO() HERE! GETADDRINFO() HERE! GETADDRINFO() HERE! GETADDRINFO() HERE!
    
    // Create socket
    int fd = socket(AF_INET, SOCK_STREAM, 0);
    if (fd==-1) {
        printf("The server attempted to create a socket, but failed to do so, encountering error code: %d\n", fd);
        return 1;
    } else {
        // Bind socket to address...
        if (bind(fd, (struct sockaddr*) &addr, sizeof(addr)) == -1) {
            printf("Unable to bind. Server error. \nAborting. \n");
            return 1;
        } else {
            printf("Now listening on port: %d\n", portToServe);
        }
        
        
    }
    
    // Listen for connections!
    int backlog = 10; // This should be extended later...
    if (listen(fd, backlog) == -1) {
        perror("error listening.");
        exit(1);
    }
    
    listeningBytesRead = read(fd, listeningBuffer, BUFFERLEN);
    if (listeningBytesRead == -1) {
        // An error occurred listening.
        printf("An error occurred listening to an incoming data stream.\n");
        return 1;
    } else {
        for (int index = 0; index < BUFFERLEN && listeningBuffer[index] != '\0'; index++) {
            printf("%c", listeningBuffer[index]);
        }
        printf("\n");
    }
    
    //Accept new connections.
    struct sockaddr_in cliaddr;
    socklen_t cliaddr_len = sizeof(cliaddr);
    int connfd = accept(fd, (struct sockaddr *) &cliaddr, &cliaddr_len);
    if (connfd == -1) {
        int errno_saved = errno;
        printf("The server attempted to accept a connection, but failed to do so, encountering error code:  %d\n", connfd);
        printf("Socket failure error number:  %d\n", errno_saved);
        exit(1);
    }
    
    if (send(connfd, "Hello, world!", 13, 0) == -1)
        perror("send");
    
}
