package com.chaos.SOAService;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;


@ServiceMapping(Value="printService4",Method =0)   //Service Register
public class printService4 extends BaseService {


	@Override
	public String run(String Parameter1,String Parameter2) {	
		System.out.println("welcome to access print service4");
		return "print Service4:" + Parameter1 + Parameter2;
       	
	}

}