package com.chaos.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.zookeeper.ZooKeeper;

import com.chaos.Config.configer;

import groovyjarjarantlr.collections.List;

public class fileUtil {
    
	private String IP;  //for services register
	private int port;   //for services register
	ZooKeeper zk;
	
	public fileUtil(String IP,int port)
	{
		this.IP=IP;
		this.port=port;
		
	}
	
	public fileUtil(String IP,int port,ZooKeeper zk)
	{
		this.IP=IP;
		this.port=port;
		this.zk=zk;
		
	}
	
	
   	public void ZooKeeperFileRegister(String Path) throws Exception
    {
   		
   		
   		
   	}
	
   	
   	    /*
		 *   read the binary file
		 *   split the file into the block
		 *   save the block into the hard disk
		 * 
		 */
   	
   	
   	public LinkedHashMap<String,String> SaveFiletoBlock(String FileName,String SavePath) throws Exception
   	{
   		
   		LinkedHashMap<String, String> rv=new LinkedHashMap<String, String>();
   		File file=new File(FileName);
   		FileInputStream in = new FileInputStream(file);  
   		byte[] blockData=new byte[configer.FSCBlockSize];
   		int len = 0 ;
   		while ((len = in.read(blockData)) != -1)
   		{
   			String blockID=UUID.randomUUID().toString();
            String lastChar=SavePath.substring(SavePath.length()-1,SavePath.length());
            String blockFileName = new String();
            //check the last char for SavePath
   			if (lastChar.equals("/")||lastChar.equals("\\")) blockFileName=SavePath+blockID;
   			else blockFileName=SavePath+"/"+blockID;
   			FileOutputStream fos = new FileOutputStream(blockFileName);
   			DataOutputStream out=new DataOutputStream(fos);
   			out.write(blockData,0,len);
   			out.close();
   			//System.out.println(blockFileName);
   			rv.put(blockID, blockFileName);
   	
   		}
   		
   		
   		return rv;
   		
   	
   	}
   	public void MergeBlocktoFile(LinkedHashMap<String,String> BlockMap,String TargetFileName) throws Exception
   	{       
   		FileOutputStream fos = new FileOutputStream(TargetFileName,false);
		DataOutputStream out=new DataOutputStream(fos);
   		System.out.println("------------------------------------");
   		
   		for (Entry<String,String> block:BlockMap.entrySet())
   		{   	
   			File file=new File(block.getValue());
   			//System.out.println(block.getValue());
   			FileInputStream in = new FileInputStream(file);  
   	   		byte[] fileContent=new byte[(int) file.length()];
   			in.read(fileContent);
   			in.close();
   			out.write(fileContent);
   		}
   		out.close();
   		
   	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		fileUtil fu=new fileUtil(configer.DefaultHttpIP,configer.DefaultFileSystemPort);
		try {
			LinkedHashMap<String,String>  bl=fu.SaveFiletoBlock("E:/blockTest/lemon-1.8.0.zip", "E:/blockTest/");
			fu.MergeBlocktoFile(bl, "E:/blockTest/1.zip");
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

}
