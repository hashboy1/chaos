package com.chaos.SOAService;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;



@ServiceMapping(Value="PrintServiceTesting",Method =0)
public class PrintServiceTesting extends BaseService {

	@Override
	public String run(String ... Parameter) {
		
		System.out.println("welcome to access print service");
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{
			result+=Parameter[i];
		}
		
		return "print Service2:" + result;
		
	}

}
