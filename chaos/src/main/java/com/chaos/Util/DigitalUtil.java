package com.chaos.Util;

import java.util.Random;

public class DigitalUtil {

	
	public static int getRandomNum(int upper)
	{
		Random rand =new Random();
		return rand.nextInt(upper);	
	}
}
