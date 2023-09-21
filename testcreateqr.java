package com.covidclient;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar; 
import com.covidclient.genqrcode;
import com.covidclient.ReadQRCode;
import java.io.IOException;
public class testcreateqr {
    public static void spiltstring(String s)
    {
        
        String[] arrOfStr = s.split(";");
        System.out.println(arrOfStr[0]+arrOfStr[1]);
        /*for (String a : arrOfStr){
            System.out.println(a);
        }*/
    }
    public static void main(String args[]){
        try {
            /*genqrcode create =new genqrcode();
            create.QRcode("hust","/mnt/d/java/project/h.png");*/
            String str = "hust;hust123;HUST;F0;0987654321;hust@example.com;DH BKHN;";
            spiltstring(str);
        } catch (Exception e) {
           System.out.println(e);
        }
    }
}
