#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <string.h>
#include "connectdb.h"
#define BUFF_SIZE 1024
//Remember to use -pthread when compiling this server's source code
int checkopcode(char *a)
{
  char opcode[11];
  int i=0;
  while (1)
  {
	if(i == 11){break;}
	if (a[i]==':')
	{
		opcode[i] = '\0';
		break;
	}
	opcode[i]=a[i];
	i++;
  }
  if (strcmp(opcode,"OP_CODE_0")==0)
  {
	return 5;
  }
  
  if (strcmp(opcode,"OP_CODE_1")==0)
  {
	return 1;
  }
  if (strcmp(opcode,"OP_CODE_2")==0)
  {
	return 2;
  }
  if (strcmp(opcode,"OP_CODE_3")==0)
  {
	return 3;
  }
  if(strcmp(opcode,"OP_CODE_4")==0){return 4;}
}

void splitlogin(char *a,char *usr,char *psw){
  int i=0,j=0;
  int count = 0;
  while (1)
  {
	if (a[i]==0)
	{
	 psw[j]=0;
	 break;
	}
	if (count == 0 && a[i]!=',')
	{
	 usr[j]=a[i];
	 j++;i++;
	}
    if (count == 1)
	{
		psw[j]=a[i];
		j++;i++;
	}
	if (a[i]==',')
	{
		usr[j]=0;
		j = 0;i++;count = 1;
	}
  }
}
void splitdateplace(char *a,char *place,char *date)
{
  int i=0;
  int k=0;
  int count=0;
  while (1)
  {
	if (a[i]==':'){i++;break;}
    i++;
  }
  while (1)
  {
    if (a[i]=='\0'){ date[k]='\0';break;}
    if (a[i]=='|'){ place[k]='\0';k=0;i++;count++;}
    if (count == 0){place[k]=a[i];k++;i++;}
    if (count == 1){ date[k]= a[i];k++;i++;}
  }
}
void splitopcode3(char *a,char *s){
	int i=0,k=0;
	while (1)
    {
	if (a[i]==':'){i++;break;}
    i++;
    }
	while (1){
	  if (a[i]==0){s[k-1]='\0';break;}
	  s[k]=a[i];k++;i++;
	}
}
void *connection_handler(void *);
int main()
{
    char server_message[100] = "Hello from Server!!\n";
    int server_socket;
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
	
    if (server_socket==-1){
	perror("Socket initialisation failed");
	exit(EXIT_FAILURE);
	}
    else
	printf("Server socket created successfully\n");

    struct sockaddr_in server_addr;
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(9999);
    server_addr.sin_addr.s_addr = INADDR_ANY;
    
    //bind the socket to the specified IP addr and port
    if (bind(server_socket, (struct sockaddr*) &server_addr, sizeof(server_addr))!=0){
	printf("socket bind failed...\n"); 
        exit(0);
	}
    else
	printf("Socket successfully binded..\n"); 
    
    if (listen(server_socket, 3)!=0){
		printf("Listen failed...\n"); 
        exit(0); 
    } 
    else
        printf("Server listening..\n"); 
    
    int no_threads=0;
    pthread_t threads[8];
    while (no_threads<8){
	printf("Listening...\n");
	int client_socket = accept(server_socket, NULL, NULL);
	puts("Connection accepted");
	if( pthread_create( &threads[no_threads], NULL ,  connection_handler , &client_socket) < 0){
	perror("Could not create thread");
	return 1;}
    	if (client_socket < 0) { 
        	printf("server acccept failed...\n"); 
        	exit(0); 
    		} 
    	else
        	printf("Server acccept the client...\n");
	puts("Handler assigned");
	no_threads++;
	}
	int k=0;
    for (k=0;k<5;k++){
	pthread_join(threads[k],NULL);
}

    //int send_status;
    //send_status=send(client_socket, server_message, sizeof(server_message), 0);
    close(server_socket);
    
    return 0;
}


void *connection_handler(void *client_socket){
	int socket = *(int*) client_socket;
	char user[25],psw[25];
	char place[30],date[15];
	char s[100];
	int bytes_received;
	int bytes_sent;
	int statuslogin = 0;
	int signup = 0;
	char userinfor[100];
	char recv_data[BUFF_SIZE];
    while (1)
	{
	  memset(recv_data,'\0',BUFF_SIZE);
	  bytes_received= recv(socket, recv_data, BUFF_SIZE-1, 0); //blocking
	  if (bytes_received <= 0){
				printf("\nConnection closed");
				break;
	  }
	  else{
		recv_data[bytes_received-1] = '\0';
		printf("\nReceive: %s ", recv_data);
		if(checkopcode(recv_data)==5){
			signup = 1;
			splitopcode3(recv_data,userinfor);
			printf("%s",userinfor);
			if(connection()==1){signupuser(userinfor);}
		}
		else{
         splitlogin(recv_data,user,psw);
		 if(connection()==1)
		 {
		   statuslogin = checkaccount(user,psw);
		   printf("%d\n",statuslogin);
		 }
		}
        
	  }
      if (statuslogin==1)
	  {
		char msg[7]="ACCEPT";
		strcat(msg,"\n");
		bytes_sent = send(socket,msg,7,0); /* send to the client welcome message */
		if (bytes_sent <= 0){
		printf("\nConnection closed");
		break;
		}
		statuslogin = 1;
		break;
	   }
	   else{
		   if(signup==1){
			bytes_sent = send(socket,"Sign up success\n",17,0);
			signup = 0;
		   }
		   else{
            char msg[7]="DENY";strcat(msg,"\n");
		   bytes_sent = send(socket,msg,7,0); /* send to the client welcome message */
		   if (bytes_sent <= 0){
		    printf("\nConnection closed");
		    break;
		   }
		   }
           
		}
	}
	if (statuslogin == 1)
	{
		
	    int read_len;
	    char server_message[100]="Hello from server\n";
	    int send_status;
		char status[3],newstatus[3];
    	//send_status=send(socket, server_message, sizeof(server_message), 0);
	    char client_message[BUFF_SIZE];
	    while( (read_len=recv(socket,client_message, 100,0))>0)
	    {
		//end of string marker
		client_message[read_len-1] = '\0';
		printf("%s\n",client_message);
		if(strcmp(client_message,"exit")==0){break;}
		deleteschedule(user);
		updatesql(user);
		updatesatatus(user);
		reportstatus(user,status);
		//Send the message back to client
		//printf("%d\n",checkopcode(client_message));
		if (checkopcode(client_message)==1)
		{
		  char mes[30];
		  if (strcmp(status,"X")==0)
		  {
			strcpy(mes,"You are not F0 or F1\n");
		  }
		  if (strcmp(status,"F0")==0)
		  {
			strcpy(mes,"You are F0\n");
		  }
		  if (strcmp(status,"F1")==0)
		  {
			strcpy(mes,"You are F1\n");
		  }
		  send_status=send(socket ,mes,strlen(mes),0);
		}
		if (checkopcode(client_message)==2)
		{
		  splitdateplace(client_message,place,date);
		  if (checkF0place(place,date) == 1){ updateF1(user); }
		  schedule(user,place,date);
		  send_status=send(socket ,"Scan complete! Check your status\n",34,0);
		}
		if (checkopcode(client_message)==3)
		{
          splitopcode3(client_message,s);
		  if (strcmp(status,"F0")==0)
		  {
			editpro5(user,s);
			reportstatus(user,newstatus);
			if (strcmp(newstatus,"X")==0)
			{
				updateF0schedule(user);
			}
		  }
		  else{
           editpro5(user,s);
		  }
		  send_status=send(socket ,"Your profile has been changed\n", 31,0);	
		}
		if(checkopcode(client_message)==4)
		{
			char msg[256];
			usrinfor(user,msg);
			strcat(msg,"\n");
			send_status=send(socket,msg,strlen(msg),0);
		}
	    }
	}
	return 0;
}