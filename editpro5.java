package com.covidclient;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Exception; 
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar; 
import com.covidclient.genqrcode;
import com.covidclient.ReadQRCode;
import com.covidclient.getplace;

public class editpro5 extends JFrame{
    
    String mes="OP_CODE_3:";
    String name,phone,status,email,address;
    public JPanel controlPanel,buttonPanel; 
    JLabel nameLabel, phoneLabel, statusLabel, emailLabel, addressLabel;
    JTextField nameText, phoneText, statusText, emailText, addressText;
    JButton send;
    String recmsg;
    public Socket client;
    public editpro5(Socket socket) {
        client = socket;
       
        setTitle("Edit profile");
        setSize(500, 300);
        controlPanel = new JPanel(new GridLayout(0, 2));
        buttonPanel = new JPanel();
        nameLabel = new JLabel("Name",JLabel.LEFT);
        nameText = new JTextField(30);

        phoneLabel = new JLabel("Phone",JLabel.LEFT);
        phoneText = new JTextField(30);

        statusLabel = new JLabel("Status",JLabel.LEFT);
        statusText = new JTextField(30);

        emailLabel = new JLabel("Email",JLabel.LEFT);
        emailText = new JTextField(30);

        addressLabel = new JLabel("Address",JLabel.LEFT);
        addressText = new JTextField(30);
        send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String mes1="";
                    name = nameText.getText();
                    phone = phoneText.getText();
                    status = statusText.getText();
                    email = emailText.getText();
                    address = addressText.getText();
                    if(!name.isEmpty())
				    { mes1=mes1+"name='"+name+"',";}
                    if(!phone.isEmpty())
				    {mes1=mes1+"phone ='"+phone+"',";}
                    if(!status.isEmpty())
				    {mes1=mes1+"status='"+status+"',";}
				    if(!email.isEmpty())
				    { mes1=mes1+"email ='"+email+"',";}
				    if(!address.isEmpty())
				    { mes1=mes1+"address ='"+address+"',";}
                    mes = mes+mes1;
                    pw.println(mes);
                    recmsg = br.readLine();
                    System.out.println(recmsg);
                    dispose();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                
            }  
        });
        controlPanel.add(nameLabel);
        controlPanel.add(nameText);
        controlPanel.add(phoneLabel);
        controlPanel.add(phoneText);
        controlPanel.add(statusLabel);
        controlPanel.add(statusText);
        controlPanel.add(emailLabel);
        controlPanel.add(emailText);
        controlPanel.add(addressLabel);
        controlPanel.add(addressText);
        controlPanel.add(send);
        add(controlPanel);
    }
    /*public static void main(String[] args) {
        editpro5 page = new editpro5();
        page.setVisible(true);
}*/
}
