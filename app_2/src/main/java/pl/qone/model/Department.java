package pl.qone.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "department", uniqueConstraints = {@UniqueConstraint(name="unique_name_constraints", columnNames="name")})
public class Department {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=100)
	private String name;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Address address;
	@JsonManagedReference
	@OneToMany(mappedBy="department", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> users = new ArrayList<>();

	public Department() {}

	public Department(String name) {
		this.name = name;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		users.add(user);
		user.setDepartment(this);
	}
	
	public void removeUser(User user) {
		users.remove(user);
		user.setDepartment(null);
	}
}