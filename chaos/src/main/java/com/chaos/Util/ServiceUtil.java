package com.chaos.Util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Context.SpringContextHolder;
import com.chaos.Context.configerContextHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class ServiceUtil {
	
	private String IP;  //for services register
	private int port;   //for services register
	ZooKeeper zk;       //reference parameter from constructor or init function
	private final Logger log = Logger.getLogger(getClass());
	
	public ServiceUtil()
	{		
		log.warn("ServiceUtil Created!!");
	}
	
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
	
	
	public void init(String IP,int port)
	{
		this.IP=IP;
		this.port=port;
		
	}
	
	public void init(String IP,int port,ZooKeeper zk)
	{
		this.IP=IP;
		this.port=port;
		this.zk=zk;	
		
	}
	
	//static function for call the ancestor method reflect and dynamic proxy model
	public static String callBaseServiceReflect(String className,String...parameter) throws Exception
	{
		Class a = Class.forName(className);
        BaseService instance1 = (BaseService) a.newInstance(); 
        String writecontent= instance1.run(parameter);
        return writecontent;
	}
	
	
	//static function for call the ancestor method by spring
	public static String callBaseServiceSpring(String className,String...parameter) throws Exception 
	{   
		try 
		{
		BaseService instance1 = (BaseService) SpringContextHolder.getApplicationContext().getBean(Class.forName(className));
        String writecontent= instance1.run(parameter);
        return writecontent;
		}
        catch (ClassNotFoundException ex)
        {
        	return "unknown service in this server,please contact with the system administrator!";		
        }   
	}
	
	
	
   	public void RedisServiceRegister() throws Exception
   	{	
   		 JedisPool pool;   
   		 Jedis jedis;
   		 JedisPoolConfig config;
   		 String RedisIp=configerContextHolder.getProp("chaos.Redis.RedisIp");
   		 int RedisPort=Integer.parseInt(configerContextHolder.getProp("chaos.Redis.RedisPort"));
   		 int RedisKeyExpiredSeconds=Integer.parseInt(configerContextHolder.getProp("chaos.Redis.RedisKeyExpiredSeconds"));
   		 
   		 config= new JedisPoolConfig();
   		 pool = new JedisPool(RedisIp,RedisPort);    
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
   	    	 jedis.setex(key,RedisKeyExpiredSeconds,url); //key will be expired over 120s
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
   		
   		String ZooKeeperSOABaseKey=configerContextHolder.getProp("chaos.Zookeeper.ZooKeeperSOABaseKey");
   		String encoding=configerContextHolder.getProp("chaos.encoding");

   		//create the root path for SOAServices
	      Stat exists = zk.exists(ZooKeeperSOABaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
	    	  log.warn("Create Base Path:"+ZooKeeperSOABaseKey);
            zk.create(ZooKeeperSOABaseKey, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);  
           }  	
   		 
   	     List<Class<?>> Lc=new ClassListUtil().getAllClassess("","");
   	     log.warn("Class List:"+Lc.toString());
   	     for (Class a :  Lc)
   	     {
   	
   	    	Annotation cc=a.getAnnotation(ServiceMapping.class);
            ServiceMapping sm = (ServiceMapping)a.getAnnotation(ServiceMapping.class); 
   	    	 	 
   	    	 if (sm!=null)
   	    	 {
   	    	 String ServiceName = sm.Value();   //Service Name
   	    	 String zServicenode = ZooKeeperSOABaseKey+"/"+ServiceName;
   	    	 String url = this.IP+":"+this.port+"/"+a.getName();
   	    	 String hosturl=this.IP+":"+this.port;
   	    	 String hosturlkey=zServicenode+"/"+hosturl;
   	    	 
   	    	 //Create the PERSISTENT service Node, if not exists
   	    	 exists = zk.exists(zServicenode, false);  
	   	     if (exists == null)  
	   	     {
	   	     log.warn("Create Service Path:"+zServicenode);
	   	     zk.create(zServicenode, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);         
	   	     }
	   	     //create the EPHEMERAL host node,if not exists
	   	     exists = zk.exists(hosturlkey, false); 
	   	     if (exists == null)     
	   	     {
	   	     log.warn("Create hosturl key:"+hosturlkey);
		   	 zk.create(hosturlkey, url.getBytes(encoding), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);          
	   	     }
	   	     }
   	     }
   		
   	}
   	
   	
   	public HashMap<String,Object> ZooKeeperListService() throws Exception{
   		String ZooKeeperSOABaseKey=configerContextHolder.getProp("chaos.Zookeeper.ZooKeeperSOABaseKey");
   		String encoding=configerContextHolder.getProp("chaos.encoding");
   		
   	    Stat exists = zk.exists(ZooKeeperSOABaseKey, false);   
	      if (exists == null) {           //if it can't found the root node, it will create the root node and it will be persistent.
	    	log.warn("it can't find the Base Path:"+ZooKeeperSOABaseKey);
            return null;
         }  
	            
	      HashMap<String,Object> rv=new HashMap<>();
	      
	      List<String> ServiceNodeList= zk.getChildren(ZooKeeperSOABaseKey, false);
	      for (String ServiceNodeName:ServiceNodeList)
	      {
	    	  
	    	  //change to only return the services list, it didn'y return the detail service
	    	  
	    	  String ServiceNodeFullName= ZooKeeperSOABaseKey+"/"+ServiceNodeName;
	    	  log.warn("Service Node Name:"+ServiceNodeFullName);
	    	 
	    	  List<String> HostNodeList= zk.getChildren(ServiceNodeFullName, false);
	    	  if (HostNodeList.size()>0) rv.put(ServiceNodeName, ServiceNodeName);   // only the available services will be added to the list
	    	  
	    	  /*
	    	  String ServiceNodeFullName= ZooKeeperSOABaseKey+"/"+ServiceNodeName;
	    	  log.warn("Service Node Name:"+ServiceNodeFullName);
	    	 
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
		    	  nodeData=new String(nodeDatabyte,encoding);
		    	  log.warn("child node data:"+nodeData);
		    	  }		    	  
		    	  HostNodeDataList[i]=nodeData;
		    	  i++;	  
	    	  }
	    	    
	    	  rv.put(ServiceNodeName, HostNodeDataList);
	    	  */
	      }
	      
	      return rv;
   		
   	}
   	
   	
   	
public String ZooKeeperGetServiceURL(String ServiceNodeName) throws Exception{
	          String ZooKeeperSOABaseKey=configerContextHolder.getProp("chaos.Zookeeper.ZooKeeperSOABaseKey");
	      	  String encoding=configerContextHolder.getProp("chaos.encoding");
              String ServiceNodeFullName= ZooKeeperSOABaseKey+"/"+ServiceNodeName;
              Stat exists = zk.exists(ServiceNodeFullName, false); 
              if (exists ==null) 
              {   
            	  log.warn("Unknown Service Node:"+ServiceNodeFullName);
            	  return null; 
              }
	    	  List<String> HostNodeList= zk.getChildren(ServiceNodeFullName, false);  
	    	  if (HostNodeList.size()<=0)
	    	  {
	    		  log.warn("no host found for Service Node:"+ServiceNodeFullName);
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
		    	  nodeData=new String(nodeDatabyte,encoding);
		    	  log.warn("child node data:"+nodeData);
		    	  }		    	  
		    	  HostNodeDataList[i]=nodeData;
		    	  i++;	  
	    	  }
	    	    	 
	    	  String detailURL=LoadBalanceUtil.LoadBalance(HostNodeDataList);
	    	  log.warn("get the detail service URL:"+detailURL);  
	    	  return detailURL;  
	    
	      
	 
   		
   	}
   	

	public static void main(String[] args) throws Exception {
		
		/*
		ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null);
		ServiceUtil cl = new ServiceUtil(configer.DefaultHttpIP,configer.DefaultSOAPort,zk);
		cl.ZooKeeperServiceRegister();
		cl.ZooKeeperListService();
         Thread.sleep(100000);  
		*/
		
		
		
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
