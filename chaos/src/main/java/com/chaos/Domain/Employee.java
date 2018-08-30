package com.chaos.Domain;

/**
 * User映射类

 */
public class Employee {
	
	
	private Integer Id;
	private String EmployeeNO;
	private String RealName;
	private String Password;
	private String Role;
	private String Department;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getEmployeeNO() {
		return EmployeeNO;
	}
	public void setEmployeeNO(String employeeNO) {
		EmployeeNO = employeeNO;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	
	
	@Override
	public String toString() {
		return "Employee [Id=" + Id + ", EmployeeNO=" + EmployeeNO + ", RealName=" + RealName + ", Password=" + Password
				+ ", Role=" + Role + ", Department=" + Department + "]";
	}


	
	
	
}
