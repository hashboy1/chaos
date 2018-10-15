package com.chaos.Util;

import org.apache.log4j.Logger;

public class PathUtil {
	
	 private final Logger log = Logger.getLogger(getClass());
	
	public String getCurrentPath(){  
	       //取得根目录路径  
	       String rootPath=getClass().getResource("/").getFile().toString();  
	       //当前目录路径  
	       String currentPath1=getClass().getResource(".").getFile().toString();  
	       String currentPath2=getClass().getResource("").getFile().toString();  
	       //当前目录的上级目录路径  
	       String parentPath=getClass().getResource("../").getFile().toString();  
	         
	       return rootPath;         
	  
	   } 
	
	public static void main(String[] args) throws Exception {
	
		PathUtil hj=new PathUtil();
		System.out.println(hj.getCurrentPath());
	}

}
