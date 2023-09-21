package com.covidclient;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class displayimage extends JFrame {
    private BufferedImage image;

    public displayimage(String a) {
        setTitle("Display Image");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(280, 280);

        try {
            image = javax.imageio.ImageIO.read(new java.io.File(a));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        });
    }
}
