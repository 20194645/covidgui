#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>
PGconn *conn;
PGresult *res;
PGresult *res1;
int connection()
{
 conn = PQconnectdb("host=localhost port=5432 dbname=mydb user=postgres password=mypassword");
 if (PQstatus(conn) != CONNECTION_OK) {
        fprintf(stderr, "Connection to database failed: %s", PQerrorMessage(conn));
        PQfinish(conn);
        //exit(1);
        return 0;
    }
  else return 1;
}
int checkaccount(char *user,char *psw)
{
 char sql[256];
 sprintf(sql,"SELECT * FROM account WHERE username='%s' and password = '%s'",user,psw);
 res = PQexec(conn, sql);
 if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        //exit(1);
        return 0;
     }
  else{
    int nFields = PQnfields(res);
    int nRows = PQntuples(res);
    if (nRows == 0)
    {
        return 0;
    }
    else{
        /*for (int i = 0; i < nRows; i++) {
        for (int j = 0; j < nFields; j++) {
            printf("%s\t", PQgetvalue(res, i, j));
        }
        printf("\n");
       }*/
      return 1;
    }
  }
}
int checkF0place(char *qrcode,char *date)
{
  char sql[256];
  sprintf(sql,"SELECT * FROM covid_cases WHERE name ='%s' and date_reported = '%s'",qrcode,date);
  res = PQexec(conn, sql);
 if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        //exit(1);
        return 0;
  }
  else{
    int nFields = PQnfields(res);
    int nRows = PQntuples(res);
    if (nRows == 0)
    {
        return 0;
    }
    else{
        /*for (int i = 0; i < nRows; i++) {
        for (int j = 0; j < nFields; j++) {
            printf("%s\t", PQgetvalue(res, i, j));
        }
        printf("\n");
       }*/
      return 1;
    }
  }
}
void reportstatus(char *user,char *status)
{
  char sql[256];
  sprintf(sql,"SELECT status FROM account WHERE username='%s';",user);
  res = PQexec(conn, sql);
  if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        //exit(1);
     }
  else{
     int nFields = PQnfields(res);
     int nRows = PQntuples(res);
     /*for (int i = 0; i < nRows; i++) {
        for (int j = 0; j < nFields; j++) {
            printf("%s\t", PQgetvalue(res, i, j));
        }
        printf("\n");
     }*/
    strcpy(status,PQgetvalue(res, 0, 0));
  }
}
void updateF1(char *usr)
{
  char sql[256];
  sprintf(sql,"UPDATE account SET status = 'F1' WHERE username='%s';",usr);
  res = PQexec(conn, sql);
}
void schedule(char *usr,char *place,char *date){
  char sql[256],sql1[256];
  sprintf(sql,"SELECT * FROM schedule WHERE place_name ='%s' and datereport = '%s' and username ='%s';",place,date,usr);
  res = PQexec(conn, sql);
  int nRows = PQntuples(res);
  if(nRows==0)
  {
   sprintf(sql1,"INSERT INTO schedule VALUES('%s','%s','%s');",usr,place,date);
   res = PQexec(conn, sql1);
   if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(conn));
        //exit(1);
     }
  }
}
void editpro5(char *usr,char *s){
  char sql[256];
  sprintf(sql,"UPDATE account SET %s WHERE username = '%s';",s,usr);
  res = PQexec(conn, sql);
}
void updatesql(char *usr)
{
 char status[3];
 char sql[256];
 reportstatus(usr,status);
 if (strcmp(status,"F0")==0)
 {
  sprintf(sql,"INSERT INTO covid_cases(name,date_reported) SELECT place_name,datereport FROM schedule where username ='%s';",usr);
  res = PQexec(conn, sql);
 }
 
}
void updateF0schedule(char *usr)
{
  char sql[256];
  sprintf(sql,"delete from schedule where username = '%s'",usr);
  res = PQexec(conn, sql);
}
void deleteschedule(char *usr)
{
 char sql[256];
 sprintf(sql,"delete from schedule where username = '%s' and age(current_date,datereport) > interval '14 days';",usr);
 res = PQexec(conn, sql);
}
void updatesatatus(char *usr)
{
  char sql[256],status[3];
  sprintf(sql," select * from schedule join covid_cases on covid_cases.name = schedule.place_name and covid_cases.date_reported= schedule.datereport where username = '%s';",usr);
  res = PQexec(conn, sql);
  int nRows = PQntuples(res);
  if(nRows == 0)
  {
    sprintf(sql,"UPDATE account SET status = 'X' WHERE username='%s';",usr);
    res = PQexec(conn, sql);
  }
  if (nRows > 0)
  {
    reportstatus(usr,status);
    if (strcmp(status,"X")==0)
    {
      sprintf(sql,"UPDATE account SET status = 'F1' WHERE username='%s';",usr);
      res = PQexec(conn, sql);
    }
  }
}
void usrinfor(char *usr,char *a)
{
  char sql[256];char infor[256]="";
  sprintf(sql,"select * from account where username ='%s';",usr);
  res = PQexec(conn, sql);
  int nFields = PQnfields(res);
    int nRows = PQntuples(res);
   for (int i = 0; i < nRows; i++) {
        for (int j = 0; j < nFields; j++) {
            strcat(infor, PQgetvalue(res, i, j));
            strcat(infor,";");
        }
     }
  int n = strlen(infor);
  infor[n-1]='\0';
  strcpy(a,infor);
}
void signupuser(char *s)
{
  char sql[256];
  sprintf(sql,"insert into account values(%s);",s);
  res = PQexec(conn, sql);
}