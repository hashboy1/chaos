package com.chaos.SOAService;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;

@ServiceMapping(Value="PrintService2",Method =0)   //Service Register, the ServiceMapping value can't  one "."
public class PrintService2 extends BaseService {   //the service class must belong to one java package  


	@Override
	public String run(String Parameter1,String Parameter2) {	
		System.out.println("welcome to access print service2");
		return "print Service2:" + Parameter1 + Parameter2;
       	
	}

}
