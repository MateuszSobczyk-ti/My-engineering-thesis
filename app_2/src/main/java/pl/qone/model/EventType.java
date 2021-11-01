package pl.qone.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "event_type", uniqueConstraints = {@UniqueConstraint(name="unique_name_constraints", columnNames="name")})
public class EventType {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable=false, length=50)
	private String name;
	
	@JsonManagedReference
	@OneToMany(mappedBy="eventType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> events = new ArrayList<>();
	
	public EventType() {}

	public EventType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		events.add(event);
		event.setEventType(this);
	}
	
	public void removeEvent(Event event) {
		events.remove(event);
		event.setEventType(null);
	}
	
}
