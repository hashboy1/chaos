package com.chaos.test;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.chaos.Config.configer;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public class testSpringStartup {
	public static void main(String[] args) throws Exception {
    	ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
    	//EmployeeController es =(EmployeeController) ctx.getBean("EmployeeController");
        //System.out.println(es.run("9521").toString());

    	EmployeeController es=ctx.getBean(EmployeeController.class);    
    	System.out.println(es.run("9521"));
    }
}
