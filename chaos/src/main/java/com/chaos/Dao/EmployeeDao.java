package com.chaos.Dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.chaos.Domain.Employee;

/**
 * 功能概要：User的DAO类

 */
public interface EmployeeDao {

	 Employee selectEmployeeByNo(String EmployeeNo);
	
	  

}
