package com.chaos.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.chaos.Domain.Employee;



public interface EmployeeDao2 {

	 Employee selectEmployeeByNo(String EmployeeNo);


}
