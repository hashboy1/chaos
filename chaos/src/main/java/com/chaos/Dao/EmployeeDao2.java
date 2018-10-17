package com.chaos.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import com.chaos.Annotation.MyBatisDao;
import com.chaos.Domain.Employee;

@MyBatisDao
public interface EmployeeDao2 {

	 Employee selectEmployeeByNo(String EmployeeNo);


}
