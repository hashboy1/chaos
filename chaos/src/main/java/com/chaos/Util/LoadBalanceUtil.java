package com.chaos.Util;

import org.apache.log4j.Logger;

public class LoadBalanceUtil {
	private final Logger log = Logger.getLogger(getClass());
	
	public static String LoadBalance(String[] NodeList)
	{
	int random = DigitalUtil.getRandomNum(NodeList.length);  //get the random number
  	  String detailURL=NodeList[random];
  	  if (detailURL.indexOf("http://") == -1) detailURL="http://"+detailURL;
  	  return detailURL;
	}

}
