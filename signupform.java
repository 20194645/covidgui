package com.covidclient;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Exception; 
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class signupform extends JFrame{
    String usr,psw,name,phone,status,email,address,mes,recmsg;
    JLabel userLabel,passwordLabel,nameLabel, phoneLabel, statusLabel, emailLabel, addressLabel,alert;
    JTextField userText,passwordText,nameText, phoneText, statusText, emailText, addressText;
    public JPanel controlPanel;
    JButton send;
    public Socket client;
    public signupform(Socket socket)
    {
        this.client = socket;
        setTitle("Sign Up");
        setSize(500, 400);
        alert = new JLabel(" ",JLabel.LEFT);
        controlPanel = new JPanel(new GridLayout(0, 1));
        
        userLabel = new JLabel("Username",JLabel.LEFT);
        userText = new JTextField(30);
        
        passwordLabel = new JLabel("Password",JLabel.LEFT);
        passwordText = new JTextField(30);
        
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
            public void actionPerformed(ActionEvent e){
            try {
             PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
             BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             usr = userText.getText();
             psw = passwordText.getText();
             name = nameText.getText();
             phone = phoneText.getText();
             status = statusText.getText();
             email = emailText.getText();
             address = addressText.getText();
             if(!usr.isEmpty()&&!psw.isEmpty()&&!name.isEmpty()&&!phone.isEmpty()&&!status.isEmpty()&&!email.isEmpty()&&!address.isEmpty())
             {
             mes ="'"+usr+"','"+psw+"','"+name+"','"+status+"','"+phone+"','"+email+"','"+address+"'";
             //System.out.println(mes);
             pw.println("OP_CODE_0:"+mes+"\n");
             recmsg = br.readLine();
             //statusLabel.setText(address);
              dispose();
             }
             else{
                statusLabel.setText("You need to fill all information");
             }
            } catch (Exception ex) {
                System.out.println(ex);
            }
            
            }
        });
        controlPanel.add(userLabel);
        controlPanel.add(userText);
        controlPanel.add(passwordLabel);
        controlPanel.add(passwordText);
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
        add(alert, BorderLayout.SOUTH);
        add(controlPanel);
    }
}
