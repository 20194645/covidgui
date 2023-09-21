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
import com.covidclient.LoginFrame;

public class App 
{  

	public static void main(String args[]){
		Socket client;
		try{
            client = new Socket("localhost", 9999);
			LoginFrame loginFrame = new LoginFrame(client);
			loginFrame.setVisible(true);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
       
    }
}
