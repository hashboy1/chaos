package com.chaos.SOAServices;

import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;



@ServiceMapping(Value="PrintServiceTesting",Method =0)
@Service
public class PrintServiceTesting extends BaseService {
	
	PrintServiceTesting()
	{
		
		System.out.println("welcome to access PrintServiceTesting service");
		
	}
	
	

	@Override
	public String run(String ... Parameter) {
		
		System.out.println("welcome to access print service");
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{
			result+=Parameter[i];
		}
		
		return "print Service Testing:" + result;
		
	}

}
