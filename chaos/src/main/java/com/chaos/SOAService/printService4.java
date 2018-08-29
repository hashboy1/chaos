package com.chaos.SOAService;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;


@ServiceMapping(Value="printService4",Method =0)   //Service Register
public class printService4 extends BaseService {


	@Override
	public String run(String ... Parameter) {	
		System.out.println("welcome to access print service4");
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{
			result+=Parameter[i];
		}
		
		return "print Service2:" + result;
       	
	}

}