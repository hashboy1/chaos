package com.chaos.BootStrap;
import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.chaos.Config.configerContextHolder;
import com.chaos.Context.SpringContextHolder;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public class SOAStartup {
	
	
	static
	{
		
		//read the chaos configuration file
				configerContextHolder cch=new configerContextHolder();
				cch.setProp("chaos.properties");
				
				//load the spring configuration file
				SpringContextHolder sc= new SpringContextHolder();
			    sc.setApplicationContext(new FileSystemXmlApplicationContext("classpath:applicationContext.xml"));	
	}
	
	public static void main(String[] args) throws Exception  {
       
	    //get the information from configuration file
	    HttpJSONSOAServerSpring hs =(HttpJSONSOAServerSpring)SpringContextHolder.getApplicationContext().getBean("HttpJSONSOAServerSpring");
		String IP = configerContextHolder.getProp("chaos.Services.DefaultHttpIP");
    	int port = Integer.parseInt(configerContextHolder.getProp("chaos.SOA.DefaultSOAPort"));
        String ZooKeeperIp=configerContextHolder.getProp("chaos.Zookeeper.ZooKeeperIp");
        String ZooKeeperPort=configerContextHolder.getProp("chaos.Zookeeper.ZooKeeperPort");

    	//parameters check
        if(args.length > 0)
        {
            try{
            	IP = args[0];
                port = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){

                System.out.println("parameters issue, system will startup the service by the default configuration");
            }
        }
       
        String url = "/index.html";
      
        ZooKeeper zk =new ZooKeeper(ZooKeeperIp+":"+ZooKeeperPort,2000,new Watcher()
        //just the temporary solution for null point exception on the zookeeper watcher
        {
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub	
			} 
			
		});
  
       //service portal
        hs.run(IP,port, url,zk);
        
       

    }

}
