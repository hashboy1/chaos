package com.chaos.SOAService;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;



@ServiceMapping(Value="PrintServiceTesting",Method =0)
public class PrintServiceTesting extends BaseService {

	
	
	@Override
	public String run(String Parameter1,String Parameter2) {
		
		System.out.println("welcome to access print service");
		return "print Service:" + Parameter1+Parameter2;
		
	}

}
