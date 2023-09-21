package com.covidclient;

// Java code to read the QR code

import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ReadQRCode {
    
	public static String Qrread(String args) throws Exception {
		String code = "";
		try {
			String path = "/mnt/d/java/project/"+args;
			
			BufferedImage bf = ImageIO.read(new FileInputStream(path));
			
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(bf)));
			
			Result result = new MultiFormatReader().decode(bitmap);
			
			//System.out.println("Decode:"+result.getText());
			code = result.getText();
		} catch(Exception e) {
		}
		return code;
	}
    
}