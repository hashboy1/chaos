package com.chaos.SOAServices;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Dao.EmployeeDao;
import com.chaos.Dao.EmployeeDao2;
import com.chaos.Domain.Employee;


@ServiceMapping(Value="printService2",Method =0)   //Service Register
@Service                                           //manage this function by spring
public class PrintService2 extends BaseService {   //the service class must belong to one java package  
	private EmployeeDao2 employeeDao;
	@Override
	public String run(String ... Parameter) {	
		//employeeDao=new EmployeeDao();
		System.out.println("welcome to access print service2");
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{   
		    System.out.println("parameter"+i+":"+Parameter[i]);
			result+=Parameter[i];
		}
		
		if (Parameter.length>0) {
		System.out.println("Dao:"+employeeDao);
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
		
		return "print Service2:" + result;
       	
	}

}
