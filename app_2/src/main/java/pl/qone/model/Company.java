package pl.qone.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "company", uniqueConstraints = {@UniqueConstraint(name="unique_name_constraints", columnNames="name")})
public class Company {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=100)
	private String name;
	@Column(nullable=false, length=10)
	private String nip;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Address address;
	@JsonManagedReference
	@OneToMany(mappedBy="company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> users = new ArrayList<>();
	
	public Company() {}

	public Company(String name, String nip) {
		this.name = name;
		this.nip = nip;
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

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
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
		user.setCompany(this);
	}
	
	public void removeUser(User user) {
		users.remove(user);
		user.setCompany(null);
	}
	

}
