package pl.qone.payload.request;

public class EnrollRequest {
	
	private String eventId;
	private String userId;
	private String roleInEvent;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleInEvent() {
		return roleInEvent;
	}
	public void setRoleInEvent(String roleInEvent) {
		this.roleInEvent = roleInEvent;
	}
}
