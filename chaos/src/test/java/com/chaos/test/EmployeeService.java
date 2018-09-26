package com.chaos.test;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.chaos.Dao.EmployeeDao;
import com.chaos.Domain.Employee;


/**
 * 功能概要：UserService实现类
 * 
 */
@Repository(value = "EmployeeService")  
public class EmployeeService{
	/*
	@Resource
	private EmployeeDao employeeDao;
	*/
	EmployeeService() {
		System.out.println("employee service");
	}
	
	public String selectUserById(String  EmployeeNo) {
		
		
		//return employeeDao.selectEmployeeByNo(EmployeeNo);
		return "{'result':0,'content':'print Service2:Employee [Id=1, EmployeeNO=9521, RealName=Ada, Password=TIBDbyATMOw=, Role=TM, Department=HR]'}";
	}

}
