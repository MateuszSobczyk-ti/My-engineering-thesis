package pl.qone.payload.response;

import java.util.Set;

import pl.qone.model.Role;

public class UserResponse {
	private Long id;
	private String username;
	private String phone;
	private String department;
	private String company;
	private Set<Role> roles;
	private Double averageRate;
	private int numberSignedEvents;
	
	public UserResponse(Long id, String username, String phone, String department, String company, Set<Role> roles,
			Double averageRate, int numberSignedEvents) {
		this.id = id;
		this.username = username;
		this.phone = phone;
		this.department = department;
		this.company = company;
		this.roles = roles;
		this.averageRate = averageRate;
		this.numberSignedEvents = numberSignedEvents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Double getAverageRate() {
		return averageRate;
	}

	public void setAverageRate(Double averageRate) {
		this.averageRate = averageRate;
	}

	public int getNumberSignedEvents() {
		return numberSignedEvents;
	}

	public void setNumberSignedEvents(int numberSignedEvents) {
		this.numberSignedEvents = numberSignedEvents;
	}
	
	
	

}
