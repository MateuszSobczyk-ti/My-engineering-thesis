package pl.qone.payload.response;

import java.util.Date;

import pl.qone.model.StatusEventEnum;


public class EventResponse {
	private Long id;
	private String name;
	private String description;
	private int max_number_of_contestant;
	private Date data_start;
	private Date data_end;
	private String department;
	private String eventType;
	private StatusEventEnum statusEvent;
	private String imageData;
	private long imageSize;
	private boolean czyZapisano;
	private boolean czyMoznaZapisac;
	private boolean czyMoznaOceniac;
	private int rate;
	
	
	public EventResponse(Long id, String name, String description, int max_number_of_contestant, Date data_start, Date data_end,
			String department, String eventType, StatusEventEnum statusEvent, String imageData, long imageSize, boolean czyZapisano, 
			boolean czyMoznaZapisac, boolean czyMoznaOceniac, int rate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.max_number_of_contestant = max_number_of_contestant;
		this.data_start = data_start;
		this.data_end = data_end;
		this.department = department;
		this.eventType = eventType;
		this.statusEvent = statusEvent;
		this.imageData = imageData;
		this.imageSize = imageSize;
		this.czyZapisano = czyZapisano;
		this.czyMoznaZapisac = czyMoznaZapisac;
		this.czyMoznaOceniac = czyMoznaOceniac;
		this.rate = rate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public StatusEventEnum getStatusEvent() {
		return statusEvent;
	}
	public void setStatusEvent(StatusEventEnum statusEvent) {
		this.statusEvent = statusEvent;
	}
	public String getImageData() {
		return imageData;
	}
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
	public long getImageSize() {
		return imageSize;
	}
	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
	}
	public boolean isCzyZapisano() {
		return czyZapisano;
	}
	public void setCzyZapisano(boolean czyZapisano) {
		this.czyZapisano = czyZapisano;
	}
	public boolean isCzyMoznaZapisac() {
		return czyMoznaZapisac;
	}
	public void setCzyMoznaZapisac(boolean czyMoznaZapisac) {
		this.czyMoznaZapisac = czyMoznaZapisac;
	}
	public boolean isCzyMoznaOceniac() {
		return czyMoznaOceniac;
	}
	public void setCzyMoznaOceniac(boolean czyMoznaOceniac) {
		this.czyMoznaOceniac = czyMoznaOceniac;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}

}
