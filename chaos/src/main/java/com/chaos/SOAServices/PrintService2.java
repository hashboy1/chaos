package com.chaos.SOAServices;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Dao.EmployeeDao;
import com.chaos.Dao.EmployeeDao2;
import com.chaos.Domain.Employee;


@ServiceMapping(Value="PrintService2",Method =0)   //Service Register
@Service                                           //manage this function by spring
public class PrintService2 implements BaseService {   //the service class must belong to one java package  
	@Autowired
	private EmployeeDao2 employeeDao;
	private final Logger log = Logger.getLogger(getClass());
	@Override
	public String run(String ... Parameter) throws Exception {	
		//employeeDao=new EmployeeDao();
		log.warn("welcome to access print service2");
		
		String localIP = InetAddress.getLocalHost().getHostAddress();
		log.warn("Service Provider IP Address:"+localIP);
		
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{   
			log.warn("parameter"+i+":"+Parameter[i]);
			result+=Parameter[i];
		}
		
		if (Parameter.length>0) {
			log.warn("Dao:"+employeeDao);
		Employee emp=employeeDao.selectEmployeeByNo(Parameter[0]);
		if (emp == null)
		{
			result="The employee does not exist!!!";
		}
		else
		{
			result=emp.toString();
		}
		}
		
		return "print Service2:" + result+"Service Provider IP Address:"+localIP;
       	
	}

}
