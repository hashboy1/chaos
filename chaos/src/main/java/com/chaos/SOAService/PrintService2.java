package com.chaos.SOAService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;
import com.chaos.Dao.EmployeeDao;

@Service
@ServiceMapping(Value="PrintService2",Method =0)   //Service Register, the ServiceMapping value can't  one "."
public class PrintService2 extends BaseService {   //the service class must belong to one java package  

	@Resource
	private EmployeeDao employeeDao;
	
	@Override
	public String run(String ... Parameter) {	
		System.out.println("welcome to access print service2");
		String result="";
		
		for (int i=0;i<Parameter.length;i++)
		{   
		    System.out.println("parameter"+i+":"+Parameter[i]);
			result+=Parameter[i];
		}
		
		if (Parameter.length>0) {
		System.out.println("Dao:"+employeeDao);
		result=employeeDao.selectEmployeeByNo(Parameter[0]).toString();
		}
		
		return "print Service2:" + result;
       	
	}

}
