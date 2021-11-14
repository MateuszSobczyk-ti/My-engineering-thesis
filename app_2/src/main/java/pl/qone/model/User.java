package pl.qone.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name="email")
	private String username;

	@NotBlank
	@Size(max = 120)
	private String password;
	
	@Size(max = 12)
	private String phone;

	//relacja wiele-do-wielu z tabelą roles po kluczach glownych id
	//powstaje nowa tabela pośredniczaca - user_roles
	//tabela users (owning side) posiada tabele roles (invers side)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	private Company company;
	
	@JsonManagedReference
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserInEvent> users = new ArrayList<>();
	

	public User() {
	}

	public User(String username, String password, String phone) {
		this.username = username;
		this.password = password;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<UserInEvent> getUsers() {
		return users;
	}

	public void setUsers(List<UserInEvent> users) {
		this.users = users;
	}
	
	public void addUser(UserInEvent user) {
		users.add(user);
		user.setUser(this);
	}
	
	public void removeUser(UserInEvent user) {
		users.remove(user);
		user.setUser(null);
	}
}