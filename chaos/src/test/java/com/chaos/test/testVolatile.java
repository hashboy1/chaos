package com.chaos.test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class testVolatile {
	public volatile static int inc = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService  tpe=Executors.newFixedThreadPool(10); 
		 for (int i = 0; i < 10; i ++) 
		        {tpe.execute(new Runnable() {
							@Override
							public void run() {
			                             inc++;
			                           System.out.println(inc);  
							}			 
						 });
				 //System.out.println("Thread Pool Count:"+);
		        }
		 
	       tpe.shutdown();
		 
	    }
	
	

}
