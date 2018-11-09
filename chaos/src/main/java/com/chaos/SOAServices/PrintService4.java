package com.chaos.SOAServices;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Dao.EmployeeDao;
import com.chaos.Dao.EmployeeDao2;
import com.chaos.Domain.Employee;


@ServiceMapping(Value="PrintService4",Method =0)   //Service Register
@Service
public class PrintService4 implements BaseService {

	@Autowired
	private EmployeeDao2 employeeDao;
	private final Logger log = Logger.getLogger(getClass());
	@Override
	public String run(String ... Parameter) throws Exception {	
		log.warn("welcome to access print service4");
		
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
		
		return "print Service4:" + result+"Service Provider IP Address:"+localIP;
       	
	}

}