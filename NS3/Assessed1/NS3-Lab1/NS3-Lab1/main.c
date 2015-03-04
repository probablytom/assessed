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

// Defining constants...
#define BUFFERLEN 65536
#define PORT "8080"
#define BACKLOG 10

int serverfd;
/*
void continuallyAccept(){
    while(1) {
        
        //Accept new connections.
        struct sockaddr_in cliaddr;
        socklen_t cliaddr_len = sizeof(cliaddr);
        int connfd = accept(serverfd, (struct sockaddr *) &cliaddr, &cliaddr_len);
        if (connfd == -1) {
            int errno_saved = errno;
            printf("The server attempted to accept a connection, but failed to do so, encountering error code: '%d' in System Variable `errno`.\n", errno_saved);
            return;
        }
        
        // Listen for data.
        // Setting up variables to recieve data into
        char listeningBuffer[BUFFERLEN];
        ssize_t listeningBytesRead;
        
        listeningBytesRead = read(connfd, listeningBuffer, BUFFERLEN);
        if (listeningBytesRead == -1) {
            // An error occurred listening.
            printf("An error occurred listening to an incoming data stream.\n");
        } else {
            for (int index = 0; index < BUFFERLEN && listeningBuffer[index] != '\0'; index++) {
                printf("%c", listeningBuffer[index]);
            }
            printf("\n");
        }
        
        printf("Closing connection at File Descriptor %d", connfd);
        close(connfd);

    }
}

int main(int argc, const char * argv[]) {
    // Set up recieving socket address...
    int portToServe = PORT;
    struct sockaddr_in addr;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(portToServe);
    
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
        printf("error listening.");
        return 1;
    }
    
    
    serverfd = socket(PF_INET, SOCK_STREAM, 0);
    
    continuallyAccept();
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
    
 
    if (send(connfd, "Hello, world!", 13, 0) == -1)
        perror("send");
    
}*/


void sigchld_handler(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}
// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }
    
    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(int argc, const char * argv[]) {
    
    // Get address information.
    struct addrinfo hints, *server_info_results, *server_info;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // Fill in the local IP.
    int getinfo_result = getaddrinfo(NULL, PORT, &hints, &server_info_results);
    if (getinfo_result == -1) {
        int errno_saved = errno;
        fprintf(stderr, "Error getting local server information. Errno: %d", errno_saved);
    }
    
    
    // Cycle through available getaddrinfo() results and bind wherever we can.
    for(server_info = server_info_results; server_info != NULL; server_info = server_info->ai_next) {
        
        // Set up the socket.
        int serverfd = socket(server_info->ai_family, server_info->ai_socktype, server_info->ai_protocol);
        if (serverfd == -1) {
            // Error creating socket here.
            int errno_saved = errno;
            fprintf(stderr, "Error creating socket.\nErrno: %d\n", errno_saved);
            continue;
        }
        
        // Set options on the socket -- is it already in use?
        int sockopt_value = 1;
        if ((setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR, &sockopt_value, sizeof(int))) == -1) {
            // Error: address already in use.
            int errno_saved = errno;
            fprintf(stderr, "Error! Socket already in use.\nErrno: %d\n", errno_saved);
            return 1; // Bomb out with an error.
        }
        
        // Let's try and bind to this existing, valid socket.
        if (bind(serverfd, server_info->ai_addr, server_info->ai_addrlen) == -1) {
            close(serverfd);
            printf("We tried to bind to a socket, but we were unsuccessful...\n");
            continue;
        }
        
        // If we got here, we've sucessfully bound to a socket, so we can break out of this loop!
        break;

    }
    
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
    while(1) {
        // Now that we have a socket, let's accept a connection to it.
        int connfd;
        struct sockaddr_storage client_addr;
        socklen_t client_addr_len = sizeof client_addr;
        connfd = accept(serverfd, (struct sockaddr *)&client_addr, &client_addr_len);
        if (connfd == -1) {
            int errno_saved = errno;
            fprintf(stderr, "Having trouble accepting the connection! Errno: %d", errno_saved);
            continue;
        } else {
            printf("Connection made! Exiting now.\n");
            return 0;
        }
    }
    return 0;

    
}
    
    /*
    ** server.c -- a stream socket server demo
    */
/*
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>

#define PORT "3490"  // the port users will be connecting to

#define BACKLOG 10     // how many pending connections queue will hold

void sigchld_handler(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}

// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }
    
    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(void)
{
    int sockfd, new_fd;  // listen on sock_fd, new connection on new_fd
    struct addrinfo hints, *servinfo, *p;
    struct sockaddr_storage their_addr; // connector's address information
    socklen_t sin_size;
    struct sigaction sa;
    int yes=1;
    char s[INET6_ADDRSTRLEN];
    int rv;
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    
    if ((rv = getaddrinfo(NULL, PORT, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }
    
    // loop through all the results and bind to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sockfd = socket(p->ai_family, p->ai_socktype,
                             p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }
        
        if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
                       sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }
        
        if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
            close(sockfd);
            perror("server: bind");
            continue;
        }
        
        break;
    }
    
    if (p == NULL)  {
        fprintf(stderr, "server: failed to bind\n");
        return 2;
    }
    
    freeaddrinfo(servinfo); // all done with this structure
    
    if (listen(sockfd, BACKLOG) == -1) {
        perror("listen");
        exit(1);
    }
    
    sa.sa_handler = sigchld_handler; // reap all dead processes
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
        perror("sigaction");
        exit(1);
    }
    
    printf("server: waiting for connections...\n");
    
    while(1) {  // main accept() loop
        sin_size = sizeof their_addr;
        new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
        if (new_fd == -1) {
            perror("accept");
            continue;
        }
        
        inet_ntop(their_addr.ss_family,
                  get_in_addr((struct sockaddr *)&their_addr),
                  s, sizeof s);
        printf("server: got connection from %s\n", s);
        
        if (!fork()) { // this is the child process
            close(sockfd); // child doesn't need the listener
            if (send(new_fd, "Hello, world!", 13, 0) == -1)
                perror("send");
            close(new_fd);
            exit(0);
        }
        close(new_fd);  // parent doesn't need this
    }
    
    return 0;
}*/
