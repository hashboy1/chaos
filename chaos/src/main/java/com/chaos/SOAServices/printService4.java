package com.chaos.SOAServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Dao.EmployeeDao;
import com.chaos.Dao.EmployeeDao2;
import com.chaos.Domain.Employee;


@ServiceMapping(Value="printService4",Method =0)   //Service Register
@Service
public class printService4 extends BaseService {

	@Autowired
	private EmployeeDao2 employeeDao;
	
	@Override
	public String run(String ... Parameter) {	
		System.out.println("welcome to access print service4");
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
		
		return "print Service4:" + result;
       	
	}

}