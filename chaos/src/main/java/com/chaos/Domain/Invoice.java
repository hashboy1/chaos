package com.chaos.Domain;

import java.math.BigDecimal;

/**
 * User映射类

 */
public class Invoice {
	
	
	private Integer id;
	private Integer employeeID;
	private String employeeNO;
	private String creationDate;
	private BigDecimal amount;
	private String remark;
	private String lastModifierId;
	private String department;
	private String realName;
	private String role;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}
	public String getEmployeeNO() {
		return employeeNO;
	}
	public void setEmployeeNO(String employeeNO) {
		this.employeeNO = employeeNO;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLastModifierId() {
		return lastModifierId;
	}
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", employeeID=" + employeeID + ", employeeNO=" + employeeNO + ", creationDate="
				+ creationDate + ", amount=" + amount + ", remark=" + remark + ", lastModiferId=" + lastModifierId
				+ ", department=" + department + ", realName=" + realName + ", role=" + role + "]";
	}
	
	
	
	
	
	
	
	

	
	
	
}
