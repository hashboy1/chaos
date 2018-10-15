package com.chaos.BaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {
	
	//protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public  abstract String run(String...Parameter) throws Exception;   //key function for Service
	

}
