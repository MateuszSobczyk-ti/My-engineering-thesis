package pl.qone.payload.response;


import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String phone;
	private List<String> roles;
	private String department;
	private String companyName;
	private String companyNip;

	public JwtResponse(String accessToken, Long id, String username, String phone, List<String> roles, String department, String companyName, String companyNip) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.phone = phone;
		this.roles = roles;
		this.department = department;
		this.companyName = companyName;
		this.companyNip = companyNip;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
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

	public List<String> getRoles() {
		return roles;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNip() {
		return companyNip;
	}

	public void setCompanyNip(String companyNip) {
		this.companyNip = companyNip;
	}
	
}