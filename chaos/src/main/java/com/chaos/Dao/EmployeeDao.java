package com.chaos.Dao;

import org.apache.ibatis.session.SqlSession;

import com.chaos.Context.SpringContextHolder;
import com.chaos.Domain.Employee;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public class EmployeeDao {
	 private SqlSession sqlSession;
	 public EmployeeDao()
	 {
		 sqlSession =(SqlSession)SpringContextHolder.getApplicationContext().getBean("sqlSession");
	 } 
	 public Employee selectEmployeeByNo(String EmployeeNo)
	 {
		 return sqlSession.selectOne("selectEmployeeByNo",EmployeeNo);
	 }
	 
}
