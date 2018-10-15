package com.chaos.Dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.chaos.Context.SpringContextHolder;
import com.chaos.Domain.Employee;
import com.chaos.SOA.HttpJSONSOAServerSpring;

public interface EmployeeDao {
	/*
	 private  SqlSession sqlSession;
	 public EmployeeDao()
	 {
		 sqlSession =(SqlSession)SpringContextHolder.getApplicationContext().getBean("sqlSession");
	 }
	 
	 public Employee selectEmployeeByNo(String EmployeeNo)
	 {
		 return sqlSession.selectOne("selectEmployeeByNo",EmployeeNo);
	 }
	 */ 
	 
	 Employee selectEmployeeByNo(String EmployeeNo);
	 
}
