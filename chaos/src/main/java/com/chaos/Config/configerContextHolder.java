package com.chaos.Config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;



public class configerContextHolder {
	private static  Properties prop = new Properties();
	
	public static String getProp(String key) {
		return prop.getProperty(key);
	}

	public static void setProp(String filePath) {
		
		try {
			/*
			URL url = configerContextHolder.class.getClassLoader().getResource(filePath);
			
			String filepath=url.getFile();
			//System.out.println(filepath.substring(1, filepath.length()));
			FileInputStream fis = new FileInputStream(filepath);
			*/
			
			InputStream ips = configerContextHolder.class.getResourceAsStream(filePath);
			BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));	
			
			/*
			byte[] b = new byte[4096];
			fis.read(b);
			System.out.println(new String(b,"UTF-8"));
			*/
			prop.load(ipss);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}


	public static void main(String[] args) {
		configerContextHolder cch=new configerContextHolder();
		cch.setProp("chaos.properties");
		System.out.println(configerContextHolder.getProp("chaos.Services.DefaultHttpIP"));

	}

}
