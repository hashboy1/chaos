package com.chaos.test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
 

 
 
public class text {
 
	@Autowired
	private static Chinese chinese;
	
	public static void main(String[] args) {
 
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
		//Chinese chinese = (Chinese) ctx.getBean("CHINESE");  
		chinese.eat();
		chinese.walk();
		
		//text a = (text) ctx.getBean("text");
		//a.say();
		
	}
	
	public void say(){
		System.out.println("hello world");
		chinese.eat();
		chinese.walk();
	}
}
