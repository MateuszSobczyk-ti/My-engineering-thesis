package pl.qone.payload.request;

import javax.validation.constraints.NotBlank;

public class EventTypeRequest {
	
	@NotBlank
	private String name;
	
	
	public EventTypeRequest() {}
	
	public EventTypeRequest(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
