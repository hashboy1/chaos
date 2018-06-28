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
   	    /*
   	     *    zookeeper nodes structures:
   	     *                  BaseKey -> ServiceKey1  -> host1ip:port            
   	     *                                          -> host2ip:port
   	     *                             ServiceKey2  -> host1ip:port 
   	     *                                          -> host2ip:port
   	     *    
   	     */
   		
   		

   		//create the root path for SOAServices
	      Stat exists = zk.exists(configer.ZooKeeperSOABaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
       	    System.out.println("Create Base Path:"+configer.ZooKeeperSOABaseKey);
            zk.create(configer.ZooKeeperSOABaseKey, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  
           }  	
   		 
   	     List<Class<?>> Lc=new ClassListUtil().getAllClassess("","");
   	     for (Class a :  Lc)
   	     {
   	
   	    	Annotation cc=a.getAnnotation(ServiceMapping.class);
            ServiceMapping sm = (ServiceMapping)a.getAnnotation(ServiceMapping.class); 
   	    	 	 
   	    	 if (sm!=null)
   	    	 {
   	    	 String ServiceName = sm.Value();   //Service Name
   	    	 String zServicenode = configer.ZooKeeperSOABaseKey+"/"+ServiceName;
   	    	 String url = this.IP+":"+this.port+"/"+a.getName();
   	    	 String hosturl=this.IP+":"+this.port;
   	    	 String hosturlkey=zServicenode+"/"+hosturl;
   	    	 
   	    	 //Create the PERSISTENT service Node, if not exists
   	    	 exists = zk.exists(zServicenode, false);  
	   	     if (exists == null)            
	   	     zk.create(zServicenode, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);         
	   	     
	   	     //create the EPHEMERAL host node,if not exists
	   	     exists = zk.exists(hosturlkey, false); 
	   	     if (exists == null)            
		   	 zk.create(hosturlkey, url.getBytes(configer.encoding), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);         
   	    	 
   	    	 }
   	     }
   		
   	}
   	
   	
   	public HashMap<String,Object> ZooKeeperListService() throws Exception{
   		
   	    Stat exists = zk.exists(configer.ZooKeeperSOABaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
     	    System.out.println("it can't find the Base Path:"+configer.ZooKeeperSOABaseKey);
            return null;
         }  
	            
	      HashMap<String,Object> rv=new HashMap<>();
	      
	      List<String> ServiceNodeList= zk.getChildren(configer.ZooKeeperSOABaseKey, false);
	      for (String ServiceNodeName:ServiceNodeList)
	      {
	    	  
	    	  String ServiceNodeFullName= configer.ZooKeeperSOABaseKey+"/"+ServiceNodeName;
	    	  System.out.println("Service Node Name:"+ServiceNodeFullName);
	    	 
	    	  List<String> HostNodeList= zk.getChildren(ServiceNodeFullName, false);
	    	  String[] HostNodeDataList = new String[HostNodeList.size()];
	    	  int i=0;
	    	  for (String HostNodeName:HostNodeList)
	    	  {
	    		  String HostNodeFullName= ServiceNodeFullName+"/"+HostNodeName;
	    		  
	    		  byte[] nodeDatabyte = zk.getData(HostNodeFullName, false, null);
		    	  String nodeData = null;
		    	  if (nodeDatabyte !=null && nodeDatabyte.length>0)
		    	  {
		    	  nodeData=new String(nodeDatabyte,configer.encoding);
		    	  System.out.println("child node data:"+nodeData);
		    	  }		    	  
		    	  HostNodeDataList[i]=nodeData;
		    	  i++;	  
	    	  }
	    	    
	    	  rv.put(ServiceNodeName, HostNodeDataList);
	      }
	      
	      return rv;
   		
   	}
   	
   	
   	
public String ZooKeeperGetServiceURL(String ServiceNodeName) throws Exception{
   		
              String ServiceNodeFullName= configer.ZooKeeperSOABaseKey+"/"+ServiceNodeName;
              Stat exists = zk.exists(ServiceNodeFullName, false); 
              if (exists ==null) 
              {   
            	  System.out.println("Unknown Service Node:"+ServiceNodeFullName);
            	  return null; 
              }
	    	  List<String> HostNodeList= zk.getChildren(ServiceNodeFullName, false);
	    	  
	    	  if (HostNodeList.size()<=0)
	    	  {
	    		  System.out.println("no host found for Service Node:"+ServiceNodeFullName);
            	  return null; 
	    	  }
	    	  
	    	  String[] HostNodeDataList = new String[HostNodeList.size()];
	    	  int i=0;
	    	  for (String HostNodeName:HostNodeList)
	    	  {
	    		  String HostNodeFullName= ServiceNodeFullName+"/"+HostNodeName;
	    		  byte[] nodeDatabyte = zk.getData(HostNodeFullName, false, null);
		    	  String nodeData = null;
		    	  if (nodeDatabyte !=null && nodeDatabyte.length>0)
		    	  {
		    	  nodeData=new String(nodeDatabyte,configer.encoding);
		    	  System.out.println("child node data:"+nodeData);
		    	  }		    	  
		    	  HostNodeDataList[i]=nodeData;
		    	  i++;	  
	    	  }
	    	  
	    	  //System.out.println("Array upper:"+HostNodeDataList.length);
	    	  int random = DigitalUtil.getRandomNum(HostNodeDataList.length);  //get the random number
	    	  String detailURL=HostNodeDataList[random];
	    	  if (detailURL.indexOf("http://") == -1) detailURL="http://"+detailURL;
	    	  System.out.println("get the detail service URL:"+HostNodeDataList[random]);  
	    	  return detailURL;   //return the random available host for load balance
	    
	      
	 
   		
   	}
   	

	public static void main(String[] args) throws Exception {
		
		ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null);
		ServiceUtil cl = new ServiceUtil(configer.DefaultHttpIP,configer.DefaultSOAPort,zk);
		cl.ZooKeeperServiceRegister();
		cl.ZooKeeperListService();

	  Thread.sleep(100000);  
		
		
		
		
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
