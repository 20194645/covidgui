package com.covidclient;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Exception; 
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar; 
import com.covidclient.genqrcode;
import com.covidclient.ReadQRCode;
public class getplace  extends JFrame {
    static JFrame f;
    private JLabel statusLabel;
    private JLabel linkJLabel, placeJLabel, dateJLabel;
    private JTextField linktext,placetext,datetext;
    static JButton b, b1;
    public Socket client;
    String sendmsg,recmsg;
    final ReadQRCode read = new ReadQRCode();
    public static String datereport(){  
     Date date = new Date();  
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
     String strDate  = formatter.format(date);  
     //System.out.println("Date Format with MM/dd/yyyy : "+strDate);   
     return strDate;
    }
    public getplace(Socket socket)
    {
        this.client = socket;
        prepareGUI();
    }
    public void prepareGUI(){
        setTitle("getplace");
        setSize(1480, 150);
        statusLabel = new JLabel(" ");
        linkJLabel = new JLabel("Link",JLabel.LEFT);
        linktext = new JTextField(50);
        linktext.setSize(50,5);
        b = new JButton("Send");
       
        placeJLabel = new JLabel("Place",JLabel.LEFT);
        placetext = new JTextField(30);
        placetext.setSize(50,5);
        dateJLabel = new JLabel("Date",JLabel.LEFT);
        datetext = new JTextField(20);
        datetext.setSize(50,5);
        b1= new JButton("Send");

        setLayout(new FlowLayout());
        
        add(linkJLabel);
        add(linktext);
        add(b);
        add(placeJLabel);
        add(placetext);
        add(dateJLabel);
        add(datetext);
        add(b1);
        add(statusLabel, BorderLayout.SOUTH);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    String s = datereport();
                    String link = linktext.getText();
                    sendmsg = read.Qrread(link);pw.println("OP_CODE_2:"+sendmsg+"|"+s);
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    recmsg = br.readLine();
                    statusLabel.setText(recmsg);
                }catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String place = placetext.getText();
                    String date = datetext.getText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate changedate = LocalDate.parse(date, formatter);
                    pw.println("OP_CODE_2:"+place+"|"+date);
                    recmsg = br.readLine();
                    statusLabel.setText(recmsg);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }

}
