package com.chaos.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.chaos.Dao.EmployeeDao;
import com.chaos.Domain.Employee;


/**
 * 功能概要：UserService实现类
 * 
 */
@Service
public class EmployeeService{
	@Resource
	private EmployeeDao employeeDao;
	
	EmployeeService() {
	}
	
	public Employee selectUserById(String  EmployeeNo) {
		return employeeDao.selectEmployeeByNo(EmployeeNo);
		
	}

}
