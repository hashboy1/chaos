package com.chaos.BootStrap;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.chaos.Config.configer;
import com.chaos.Context.SpringContextHolder;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public class SOAStartup {

	public static void main(String[] args) throws Exception {
        String IP = configer.DefaultHttpIP;
    	int port = configer.DefaultSOAPort;
    
        if(args.length > 0)
        {
            try{
            	IP = args[0];
                port = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
            	IP = configer.DefaultHttpIP;
                port = configer.DefaultSOAPort;
            }
        }
       
        String url = "/index.html";
        
        SpringContextHolder sc= new SpringContextHolder();
        sc.setApplicationContext(new FileSystemXmlApplicationContext("classpath:applicationContext.xml"));
        
    	//ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
        
        HttpJSONSOAServerSpring hs =(HttpJSONSOAServerSpring)SpringContextHolder.getApplicationContext().getBean("HttpJSONSOAServerSpring");
        //ZooKeeper zk=(ZooKeeper) ctx.getBean("zookeeper");
       // Watcher wt=Watcher();
        ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null);
        hs.run(IP,port, url,zk);

    }

}
