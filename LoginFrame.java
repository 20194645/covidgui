package com.covidclient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar; 
import com.covidclient.genqrcode;
import com.covidclient.ReadQRCode;
import com.covidclient.GUI;
import com.covidclient.signupform;
public class LoginFrame extends JFrame {
    private JLabel userLabel, passLabel, statusLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton,signupButton;
    public Socket client;
    String sendmsg,recmsg;
    
    public LoginFrame(Socket socket) {
        client = socket;
        setTitle("Login Form");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // create the components for the login form
        userLabel = new JLabel("Username",JLabel.LEFT );
        passLabel = new JLabel("Password",JLabel.LEFT );
        usernameField = new JTextField(30);
        usernameField.setSize(50,5);
        passwordField = new JPasswordField(30);
        passwordField.setSize(50,5);
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");
        statusLabel = new JLabel("");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    // pw.println(args[0]);
                    sendmsg = username+","+password;
                    pw.println(sendmsg);
                    // server msg receive!
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    recmsg = br.readLine();
                    statusLabel.setText(recmsg);
                    if(recmsg.equals("ACCEPT"))
                    {
                        
                        GUI swingControlDemo = new GUI(client); 
                        swingControlDemo.setVisible(true);;
                        //System.exit(0);
                        dispose();
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            
        });
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
              signupform page = new signupform(client);
              page.setVisible(true);
            }
        });
        // set the layout for the login form
        setLayout(new FlowLayout());
    
        // add the components to the login form
        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(signupButton);
        add(loginButton);
        add(new JLabel(""));
        // add the status label to the bottom of the login form
        add(statusLabel, BorderLayout.SOUTH);
    
        // register the login button to listen for button clicks
        
    }
    
        // perform the login authentication here
        // ...

        // update the status label to show login success/failure
        
}

   