package com.chaos.Util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import com.chaos.Config.configer;



public class hashmapUtil {
	
    private  Map<String, Object> hm = new HashMap<>();
	
	public hashmapUtil() throws Exception   //constructor
	{
		putFolder(configer.SourcePath);
	}
	
	public void putFolder(String path) throws Exception
	{
		if (path.length()== 0)
		{
			System.out.println("empty path");
			return;
		}
		 File f=new File(path);

		   if(f!=null){
	             if(f.isDirectory()){
	                File[] fileArray=f.listFiles();
	                if(fileArray!=null){
	                    for (int i = 0; i < fileArray.length; i++) {
	                        if (fileArray[i].isDirectory()) 
	                    		putFolder(fileArray[i].getPath());
	                        else
	                    	{
	                           Long filelength = fileArray[i].length();  
	                           byte[] filecontent = new byte[filelength.intValue()]; 
	                           FileInputStream in = new FileInputStream(fileArray[i]);  
	                           in.read(filecontent);  
	                           in.close();
	                           //System.out.println("Filename:"+fileArray[i].getPath());
	                           String filename = fileArray[i].getPath().replace(configer.SourcePath,"\\").replace("\\", "/");
	                           //System.out.println("Key:"+filename);
	                           //System.out.println("file content:"+new String(filecontent, configer.encoding));
	                    	   hm.put(filename, filecontent);
	                    	}
	                    	}
	                }
	            }
	            else{
	                System.out.println(f);
	            }
	        }
		
		
	}
	
	public byte[] getKey(String key)
	{		
	return (byte[]) hm.get(key);
	}
	
	public Map<String, Object> getHM()
	{
		return this.hm;
	}

	public static void main(String[] args) throws Exception {
		hashmapUtil hm = new hashmapUtil();
		
	}

}
