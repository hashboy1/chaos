package com.chaos.SOAServices;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.BaseService.BaseService;


@Service
@ServiceMapping(Value="PrintServiceTesting",Method =0)
public class PrintServiceTesting implements BaseService {
	private final Logger log = Logger.getLogger(getClass());

	@Override
	public String run(String ... Parameter) throws Exception {

		log.warn("welcome to access PrintServiceTesting service");
		String localIP = InetAddress.getLocalHost().getHostAddress();
		log.warn("Service Provider IP Address:"+localIP);
		
		String result="";
		for (int i=0;i<Parameter.length;i++)
		{
			result+=Parameter[i];
		}
		
		return "print Service Testing:" + result+"Service Provider IP Address:"+localIP;
		
	}

}
