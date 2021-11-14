package pl.qone.payload.request;

import java.util.Date;

public class EventRequest {
	
	private String name;
	private String description;
	private int max_number_of_contestant;
	private Date data_start;
	private Date data_end;
	private String departmentId;
	private String eventTypeId;
	private String statusEventId;
	private String imageId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMax_number_of_contestant() {
		return max_number_of_contestant;
	}
	public void setMax_number_of_contestant(int max_number_of_contestant) {
		this.max_number_of_contestant = max_number_of_contestant;
	}
	public Date getData_start() {
		return data_start;
	}
	public void setData_start(Date data_start) {
		this.data_start = data_start;
	}
	public Date getData_end() {
		return data_end;
	}
	public void setData_end(Date data_end) {
		this.data_end = data_end;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public String getStatusEventId() {
		return statusEventId;
	}
	public void setStatusEventId(String statusEventId) {
		this.statusEventId = statusEventId;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}
