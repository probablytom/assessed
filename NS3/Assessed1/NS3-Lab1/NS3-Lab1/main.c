//
//  webserver.c
//  NS3-Lab1
//
//  Created by Tom Wallis (matric no. 2025138)
//  Copyright (c) 2015 Tom Wallis. All rights reserved.
//


#include <stdio.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/socket.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <stdlib.h>


// Defining constants...
#define BUFFERLEN 65536
#define MAXPATHLENGTH 1024
#define MAXHOSTLENGTH 1024
#define PORT "8068"
#define BACKLOG 10
#define PACKETSIZE 512 // Packet size in bytes.



int send_server_error(int connfd) {
    char *message_content = "<html><head><title>500 SERVER ERROR</title>"
            "<head/><body><p>500 SERVER ERROR</p></body>";
    char* message_unformatted = "HTTP/1.1 500 SERVER ERROR\r\nConnection: close\r\nContent-Type: text/html\r\nContent-Length: %ld\r\n\r\n%s";
    char *message = malloc(strlen(message_unformatted) + strlen(message_content));
    sprintf(message, message_unformatted, strlen(message_content), message_content);
    if(send(connfd, message, strlen(message),0 ) == -1) {
        // error here too!
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
    char *message_content = "<html><head><title>404 FILE NOT FOUND</title>"
            "<head/><body><p><404 FILE NOT FOUND/p></body>";
    char* message_unformatted = "HTTP/1.1 404 FILE NOT FOUND\r\nConnection: close\r\nContent-Type: text/html\r\nContent-Length: %ld\r\n\r\n%s";
    char *message = malloc(strlen(message_unformatted) + strlen(message_content));
    sprintf(message, message_unformatted, strlen(message_content), message_content);
    if(send(connfd, message, strlen(message),0 ) == -1) {
        // error here too!
        return -1;
    }
    return 0;
}


char *get_file_extension(char *filename) {
    // Cycle through each character, keeping track of the '.'s we find.
    // Return the string from the last '.' onward.
    int last_period = 0;
    unsigned int index = 0;
    for(;index < strlen(filename); index++) {
        if (*(filename+index) == '.') {
            last_period = index;
        }
    }

    // Return the string from the last '.' onward.
    return (filename + last_period);
}


char *get_content_type(char *filename) {
    char *file_extension = get_file_extension(filename);

    if (strcmp(file_extension,".txt") == 0) {
        return "text/plain";
    } else if (strcmp(file_extension, ".html") == 0 ||
            strcmp(file_extension, ".htm") == 0) {
        return "text/html";
    } else if (strcmp(file_extension, ".jpg") == 0 ||
            strcmp(file_extension, "jpeg") == 0) {
        return "image/jpeg";
    } else if (strcmp(file_extension, ".gif") == 0) {
        return "image/gif";
    } else {
        return "application/octet-stream";
    }

}



long get_file_contents(char *filepath, char *file_contents) {
    // check the file actually exists.
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
        // file did not exist, return a 404.
        return 0;
    }

}

int send_400_response(int connfd) {
    char *message_content = "<html><head><title>400 BAD REQUEST</title>"
            "<head/><body><p>400 BAD REQUEST</p></body>";
    char* message_unformatted = "HTTP/1.1 400 BAD REQUEST\r\nConnection: close\r\nContent-Type: text/html\r\nContent-Length: %ld\r\n\r\n%s";
    char *message = malloc(strlen(message_unformatted) + strlen(message_content));
    sprintf(message, message_unformatted, strlen(message_content), message_content);
    if(send(connfd, message, strlen(message),0 ) == -1) {
        // error here too!
        return -1;
    }
    return 0;
}


int send_packets(int connfd, char *file_contents, char* filepath) {
    int offset;
    long bytes_to_send = get_file_contents(filepath, file_contents);

    if (bytes_to_send == 0) {
        send_file_not_found(connfd);
        return -1; //  File wasn't available, but we dealt with it right, so
        // no errors should be returned.
    }

    char *message = (char *) malloc(BUFFERLEN);
    message = "HTTP/1.1 200 OK\r\nContent-Type: %s\r\nContent-Length: %ld"\
  "\r\nConnection: close\r\n\r\n%s";
    bytes_to_send += strlen(message);
    char temp[2*BUFFERLEN];
    sprintf(temp, message, get_content_type(filepath),
            bytes_to_send, file_contents);
    long to_send = 0;
    message = temp;

    for (offset = 0; offset < bytes_to_send; offset += PACKETSIZE) {
        to_send = PACKETSIZE;
        if (offset + PACKETSIZE > bytes_to_send) {
            to_send = bytes_to_send % PACKETSIZE;
        }
        long amount_sent = send(connfd, message+offset, (size_t)to_send, 0);
        if (amount_sent == -1) {
            char *error_message = "error sending to socket with file "\
	"descriptor %d.\n";
            report_errno(error_message);
            return -1;
        }
    }

    return 0; // everything went smoothly!

}


int check_request(char request[BUFFERLEN]) {
    if (request[0] == 'G' && request[1] == 'E' && request[2] == 'T')
        return 200;
    else
        return 400;
}

// returns the length of the path in the request, puts it in filepath.
int get_request_path(char request[BUFFERLEN], char *filepath) {
    char *toreturn[MAXPATHLENGTH];
    memset(&toreturn, ' ', MAXPATHLENGTH);
    int index;
    for (index = 0; index < MAXPATHLENGTH; index++) {
        if (request[index+5] == ' ') {
            return index;
        } else
            *(filepath+index) = request[index+5];
    }
    return -1; //  we maxed out the buffer and should deal with this error!
}


// loop through the request until we get to a sequence like "\nhost:".
char *get_request_hostname(char request[BUFFERLEN]) {

    // initial setup.
    char temp[6];
    char *hostname = malloc(1024);
    int index = 0;
    int inner_index;
    for (; index < 6; index++) {
        temp[index] = request[index];
    }

    // actual looping.
    for (index = 6; index < 120; index++) {
        for (inner_index = 0; inner_index < 5; inner_index++) {
            temp[inner_index] = temp[inner_index + 1];
        }
        temp[5] = request[index];

        // if we have the host, get the hostname and return it.
        if (strcmp(temp, "\nHost:") == 0) {
            break; // Get out of here! We've found the line with the hostname.

        }
    } // either we reached the end, or the hostname wasn't here.

    // move index from the chars between the "host" and the hostname.
    index++;
    if (request[index] == ' ') index++;

    // loop to get the hostname, and return it.
    for (inner_index = 0; inner_index < 1024; inner_index++) {
        // we're at the end of the hostname at ':', '\r' or '\n'.
        if (request[index+inner_index] == ':' || request[index+inner_index] == '\r'
                || request[index+inner_index] == '\n') {
            return hostname;
        }
        *(hostname + inner_index) = request[index+inner_index];
    }
    return NULL;
}

int send_200_request(int connfd, char request[BUFFERLEN]) {
    char *filepath = (char *) malloc(sizeof(char) * MAXPATHLENGTH);
    int path_length = get_request_path(request, filepath);
    if (path_length == -1) {
        report_errno("error getting filepath.");
        send_server_error(connfd);
        return -1;
    } else {
        if (path_length == 0) filepath = "index.html";
        char *file_contents = (char *) malloc(BUFFERLEN);
        memset(file_contents, ' ', BUFFERLEN);

        printf("Got message.");
        // send the packets, one packet at a time.
        if (send_packets(connfd, file_contents, filepath) == -1) {
            char *message = "had trouble sending packets. ";
            report_errno(message);
            return -1;
        };

    }
    return 0;
}

int process_request(int connfd, char *hostname) {
    if (!fork()) { // this is a child process

        char request[BUFFERLEN];
        memset(&request, ' ', BUFFERLEN);
        long recv_response = recv(connfd, request, BUFFERLEN, 0);


        char *request_hostname = get_request_hostname(request);
        // if we have a bad hostname on this request, we can immediately exit.
        if (strcasecmp(hostname, request_hostname) != 0) {
            send_400_response(connfd);
            return 0; //We sent a 400, but the request was fine, so no errors.
        }

        if (recv_response == -1) {
            char *message = "error reading! errno: %d.\n";
            report_errno(message);
            return -1;
        }
        if(check_request(request) == 200) {
            send_200_request(connfd, request);
        } else {
            send_400_response(connfd);
        }

    }

    // the request's been processed now, so we no longer need our connection.
    close(connfd);
    return 0;
}

int acceptConnections(int serverfd, char *hostname) {

    // now that we have a socket, let's accept a connection to it.
    int connfd;
    struct sockaddr_storage client_addr;
    socklen_t client_addr_len = sizeof client_addr;
    while(1) {
        connfd = accept(serverfd, (struct sockaddr *)&client_addr,
                &client_addr_len);
        if (connfd == -1) {
            report_errno("error accepting connection.");
            break;
        } else {


            // process the request found at the connection,
            if (process_request(connfd, hostname) == -1) {
                printf("error processing request, "\
			"sending server error message.\n");
                send_server_error(connfd);
            }
        }
        close(connfd);
    }
    // if we've got this, there's been an error accepting the connection.
    return -1;
}


int main() {

    // get address information.
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



    for(server_info = server_info_results; server_info != NULL;
        server_info = server_info->ai_next) {
        if ((serverfd = socket(server_info->ai_family, server_info->ai_socktype,
                server_info->ai_protocol)) == -1) {
            continue;
        }

        int set_sock_opt = 1;
        if (setsockopt(serverfd, SOL_SOCKET, SO_REUSEADDR, &set_sock_opt,
                sizeof(int)) == -1) {
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

    gethostname(hostname, MAXHOSTLENGTH);
    struct hostent hostname_data = *gethostbyname(hostname);
    hostname = hostname_data.h_name;

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
