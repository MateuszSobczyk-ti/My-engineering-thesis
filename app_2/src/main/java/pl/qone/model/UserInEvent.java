package pl.qone.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user_in_event")
public class UserInEvent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private int eventRate;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Event event;
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private RoleInEventEnum roleInEvent;
	
	public UserInEvent() {}

	public UserInEvent(RoleInEventEnum roleInEvent) {
		this.roleInEvent = roleInEvent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEventRate() {
		return eventRate;
	}

	public void setEventRate(int eventRate) {
		this.eventRate = eventRate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public RoleInEventEnum getRoleInEvent() {
		return roleInEvent;
	}

	public void setRoleInEvent(RoleInEventEnum roleInEvent) {
		this.roleInEvent = roleInEvent;
	}
	
	

}
