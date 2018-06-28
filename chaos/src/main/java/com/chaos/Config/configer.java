package com.chaos.Config;

public class configer {
	
	
      //encode configuration
      public static String encoding = "ISO-8859-1";
    
	
      //redis configration  
	  public static String RedisIp = "192.168.0.160";
      public static int    RedisPort = 6379;
      public static int    RedisKeyExpiredSeconds = 120;
      
      
      //zookeeper configuration (option)
      public static String ZooKeeperIp = "192.168.0.160";
      public static int    ZooKeeperPort = 2181;
      public static String ZooKeeperSOABaseKey ="/SOAServices";
      public static String ZooKeeperFSCBaseKey ="/FileSystemCluster";
      
      
      //IP address for multiservice
      public static String DefaultHttpIP="192.168.0.160";
      
      //SOA service 
      public static int    DefaultSOAPort=8888;
     
      
       
  
      //path for HashMapFileServer
      public static int    DefaultFileSystemPort=8889;
      public static String SourcePath="C:\\Users\\Administrator\\Desktop\\cache\\";
      
      //FileSystem Cluster
      public static int    FSCNameNodePort=8890;   // for cluster it only can have one NameNode,service will open in this port
      public static int    FSCDataNodePort=8891;
      public static String FSCDataNodeRoot="C:\\Users\\Administrator\\Desktop\\cache\\";
      public static int    FSCDataNodeMemoryFileUpperCount = 100;   //this is files quantity,these files will be read into the memory,the other files will still storage into the hard disk.
      public static String FSCRegisterNameNodeIP="192.168.0.160";    //this is one service address for name node, when the data node opens, it will be connect to this address and finish the register process.
      public static int    FSCRegisterNameNodePort=8890; 
      public static int    FSCBlockSize=1024*1024*2;                 //DefaultBlockSize is 2M.
      public static int    FSCBlockCopied=2;
      public static int    FSCMaxUploadFileSize=1024*1024*100;         //100M
      
      //service package configuration,but it has been abandoned, you can register the service by the class annotation.
      public static String ServicePackage="JSONRPC.Service";    
      
      
    
}
