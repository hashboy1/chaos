package com.chaos.test;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.chaos.Config.configer;
import com.chaos.SOA.HttpJSONSOAServerSpring;
import com.chaos.SOAService.PrintServiceTesting;

@Component
public class EmployeeController {

	@Autowired
	EmployeeService es;
	
	@Autowired
	PrintServiceTesting ps;
	
	public String run(String  EmployeeNo)
	{	
		return es.selectUserById(EmployeeNo).toString();
		//return EmployeeNo;
	}
	
	
	
}
