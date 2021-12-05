package pl.qone.payload.request;

public class RoleRequest {
	private long role;

	public RoleRequest() {}
	
	public RoleRequest(long role) {
		this.role = role;
	}

	public long getRole() {
		return role;
	}

	public void setRole(long role) {
		this.role = role;
	}
	
}
