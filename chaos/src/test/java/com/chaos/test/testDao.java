package com.chaos.test;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class testDao {
	

   private SqlSession sqlSession;

			testDao()
			{
				   ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
				   sqlSession =(SqlSession) ctx.getBean("sqlSession");	      
			}
			
			public String selectEmployeeByNo(String employeeNo)
			{
				return sqlSession.selectOne("selectEmployeeByNo",employeeNo).toString();
			}
   
   
   public static void main(String[] args) throws Exception {
	   testDao td=new testDao();   
       System.out.print(td.selectEmployeeByNo("9521"));
   }
   
   
	

}
