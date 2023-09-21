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
import com.covidclient.editpro5;
public class GUI extends JFrame {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   public Socket client;
   String sendmsg,recmsg;
   String data="", path="";
   static JButton b, b1;
   public GUI(Socket socket){
      this.client = socket;
      prepareGUI();
   }
  
   public void prepareGUI() {
       
        setTitle("Menu");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statusLabel = new JLabel("");
        setLayout(new FlowLayout());
        add(statusLabel, BorderLayout.SOUTH);
        JMenuBar menuBar = new JMenuBar();
  
        // Create a menu
        JMenu fileMenu = new JMenu("Menu");

        // Create a menu item
        JMenuItem item1 = new JMenuItem("Your status");
        JMenuItem item2 = new JMenuItem("QrCode");
        JMenuItem item3 = new JMenuItem("Edit profile");
        JMenuItem item4 = new JMenuItem("My profile");
        // Add the menu items to the menu
        fileMenu.add(item1);
        fileMenu.add(item2);
        fileMenu.add(item3);
        fileMenu.add(item4);
       
        // Add event handling to the menu items
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("New menu item clicked.");
                try{
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    pw.println("OP_CODE_1:");
                    recmsg = br.readLine();
                    statusLabel.setText(recmsg);
                }
                catch(IOException ex)
                {
                    System.out.println(ex);
                }
            }
        });
        
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("");
                controlPanel = null;
                getplace page = new getplace(client);
                page.setVisible(true);;
            }
        });
        
        item3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editpro5 page = new editpro5(client);
                page.setVisible(true);
                //System.out.println("Save menu item clicked.");
            }
        });
        
        item4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                genqrcode create = new genqrcode();
                PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pw.println("OP_CODE_4:");
                recmsg = br.readLine();
                statusLabel.setText(recmsg);
                String[] arr = recmsg.split(";");
                data = arr[0]+","+arr[1];
                path = "/mnt/d/java/project/"+arr[0]+".png";
                create.QRcode(data, path);
                //System.out.println(data);
                //System.out.println(path);
                //statusLabel.setText(data);
                displayimage page = new displayimage(path);
                page.setVisible(true);
               } catch (Exception ex) {
                System.out.println(ex);
               }
            }
        });

        // Add the menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);
   }
   /*public static void main(String[] args){
    GUI swingControlDemo = new GUI(socket); 
    swingControlDemo.setVisible(true);
    //swingControlDemo.showEventDemo();;   

 }*/
}