package pl.qone.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "status_event", uniqueConstraints = {@UniqueConstraint(name="unique_name_constraints", columnNames="name")})
public class StatusEvent {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="name", nullable=false)
	private StatusEventEnum name;
	
	@Column(name="description", length=300)
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="statusEvent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> events = new ArrayList<>();
	
	public StatusEvent() {}
	
	public StatusEvent(StatusEventEnum name, String description) {
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusEventEnum getName() {
		return name;
	}

	public void setName(StatusEventEnum name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		events.add(event);
		event.setStatusEvent(this);
	}
	
	public void removeEvent(Event event) {
		events.remove(event);
		event.setStatusEvent(null);
	}
}
