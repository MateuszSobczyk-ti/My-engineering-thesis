package pl.qone.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=100)
	private String name;
	@Column(nullable=false, length=1000)
	private String description;
	@Column()
	private int max_number_of_contestant;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date data_start;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date data_end;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private EventType eventType;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private StatusEvent statusEvent;
	
	@JsonManagedReference
	@OneToMany(mappedBy="event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventImage> images = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy="event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserInEvent> users = new ArrayList<>();
	
	public Event() {}
	
	public Event(String name, String description, Date data_start, Date data_end, int contestant) {
		this.name = name;
		this.description = description;
		this.data_start = data_start;
		this.data_end = data_end;
		this.max_number_of_contestant = contestant;
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
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
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
 
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public StatusEvent getStatusEvent() {
		return statusEvent;
	}

	public void setStatusEvent(StatusEvent statusEvent) {
		this.statusEvent = statusEvent;
	}
	
	public List<EventImage> getImages() {
		return images;
	}

	public void setImages(List<EventImage> images) {
		this.images = images;
	}

	public void addImage(EventImage image) {
		images.add(image);
		image.setEvent(this);
	}
	
	public void removeImage(EventImage image) {
		images.remove(image);
		image.setEvent(null);
	}

	public List<UserInEvent> getUsers() {
		return users;
	}

	public void setUsers(List<UserInEvent> users) {
		this.users = users;
	}
	
	public void addUser(UserInEvent user) {
		users.add(user);
		user.setEvent(this);
	}
	
	public void removeUser(UserInEvent user) {
		users.remove(user);
		user.setEvent(null);
	}
	
}
