package com.chaos.Util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Config.configer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ServiceUtil {
	
	private String IP;  //for services register
	private int port;   //for services register
	ZooKeeper zk;
	
	public ServiceUtil(String IP,int port)
	{
		this.IP=IP;
		this.port=port;
		
	}
	
	public ServiceUtil(String IP,int port,ZooKeeper zk)
	{
		this.IP=IP;
		this.port=port;
		this.zk=zk;
		
	}
	
	//static function for call the ancestor method
	public static String callBaseService(String className,String parameter1,String parameter2) throws Exception
	{
		
		
		Class a = Class.forName(className);
        BaseService instance1 = (BaseService) a.newInstance(); 
        String writecontent= instance1.run(parameter1,parameter2);
        return writecontent;
	}
	
	

    
   	public void RedisServiceRegister() throws Exception
   	{
   		
   		 JedisPool pool;   
   		 Jedis jedis;
   		 JedisPoolConfig config;
   		 config= new JedisPoolConfig();
   		 pool = new JedisPool(configer.RedisIp,configer.RedisPort);    
   		 jedis = pool.getResource();
   		 
   		 
   	     List<Class<?>> Lc=new ClassListUtil().getAllClassess("","");
   	     for (Class a :  Lc)
   	     {
   	
   	    	
   	    	Annotation cc=a.getAnnotation(ServiceMapping.class);

   	    	 ServiceMapping sm = (ServiceMapping)a.getAnnotation(ServiceMapping.class); 
   	    	 	 
   	    	 if (sm!=null)
   	    	 {
   	    	 
   	    	 String url = this.IP+":"+this.port+"/"+a.getName();
   	    	 String key = sm.Value();
   	    	 jedis.setex(key,configer.RedisKeyExpiredSeconds,url); //key will be expired over 120s
   	    	 }
   	     }
   		
   	}
   	

   	public void ZooKeeperServiceRegister() throws Exception
   	{
   	       

   		//create the root path for SOAServices
	      Stat exists = zk.exists(configer.ZooKeeperBaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
       	    System.out.println("Create Base Path:"+configer.ZooKeeperBaseKey);
            zk.create(configer.ZooKeeperBaseKey, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  
           }  	
   		 
   	     List<Class<?>> Lc=new ClassListUtil().getAllClassess("","");
   	     for (Class a :  Lc)
   	     {
   	
   	    	
   	    	Annotation cc=a.getAnnotation(ServiceMapping.class);

   	    	 ServiceMapping sm = (ServiceMapping)a.getAnnotation(ServiceMapping.class); 
   	    	 	 
   	    	 if (sm!=null)
   	    	 {
   	    	 
   	    	 String url = this.IP+":"+this.port+"/"+a.getName();
   	    	 String key = sm.Value();

   	    	 String znode = configer.ZooKeeperBaseKey+"/"+key;
   	    	 exists = zk.exists(znode, false);  
	   	        if (exists == null) {           // the child node will be EPHEMERAL
	   	        	System.out.println("register zookeeper key:"+znode);
	   	            zk.create(znode, url.getBytes(configer.encoding), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);  
	   	          
	   	        	//zk.create(znode, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  
	   	         
	   	        }  
   	    	 
   	    	 }
   	     }
   		
   	}
   	
   	
   	public Map<String,String> ZooKeeperListService() throws Exception{
   		
   	    Stat exists = zk.exists(configer.ZooKeeperBaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
     	    System.out.println("it can't find the Base Path:"+configer.ZooKeeperBaseKey);
            return null;
         }  
	      
	      
	      Map<String,String> rv=new HashMap<>();
	      
	      List<String> nodelist= zk.getChildren(configer.ZooKeeperBaseKey, false);
	      for (String nodename:nodelist)
	      {
	    	  
	    	  String nodeFullName= configer.ZooKeeperBaseKey+"/"+nodename;
	    	  System.out.println("child node name:"+nodeFullName);
	    	  byte[] nodeDatabyte = zk.getData(nodeFullName, false, null);
	    	  String nodeData = null;
	    	  if (nodeDatabyte !=null && nodeDatabyte.length>0)
	    	  {
	    	  nodeData=new String(nodeDatabyte,configer.encoding);
	    	  System.out.println("child node data:"+nodeData);
	    	  }
	    	  
	    	  rv.put(nodename, nodeData);
	      }
	      
	      return rv;
   		
   	}
   	

	public static void main(String[] args) throws Exception {
		
		ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null);
		ServiceUtil cl = new ServiceUtil(configer.DefaultHttpIP,configer.DefaultHttpPort,zk);
		cl.ZooKeeperServiceRegister();
		cl.ZooKeeperListService();
		
		/*
		List<Class<?>> lc= cl.getAllClassess("", "");

		 for (Class a :  lc)
		{
        	try 
			{
        	System.out.println("Class Name:"+a.getName());	
			//JSONRPCBaseService instance1 = (JSONRPCBaseService) a.newInstance();    			
			//System.out.println(instance1.run("test message"));
			}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        	}
			
		}
		*/
	}
	
	
}
