package BootStrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.chaos.Config.configer;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public class SOAStartup {

	public static void main(String[] args) throws Exception {
        String IP = configer.DefaultHttpIP;
    	int port = configer.DefaultSOAPort;
    	ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
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
        
        HttpJSONSOAServerSpring hs =(HttpJSONSOAServerSpring) ctx.getBean("HttpJSONSOAServerSpring");
        hs.run(IP,port, url);
        
        //new HttpJSONSOAServerSpring().run(IP,port, url);
    }

}
