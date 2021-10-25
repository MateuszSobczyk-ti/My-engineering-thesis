package pl.qone.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
	

}
/*
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users", 
	uniqueConstraints= {@UniqueConstraint(name="email_unque_constraint", columnNames="email"),
						@UniqueConstraint(name="username_unique_constraint", columnNames="username")})
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=45)
	private String firstname;
	
	@Column(nullable=false, length=45)
	private String lastname;
	
	@NotBlank
	@Email(message = "Email should be valid")
	@Column(nullable=false, length=45)
	private String email;
	
	@NotBlank
	@Column(nullable=false, length=45)
	private String username;
	
	@NotBlank
	@Column(nullable=false, length=45)
	private String password;
	
	@Column(length=9)
	private String phone;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", 
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	
	@ManyToOne
	@JoinColumn(name="id_department", foreignKey=@ForeignKey(name="fk_user_department"))
	private Department department;
	@ManyToOne
	@JoinColumn(name="id_company", foreignKey=@ForeignKey(name="fk_user_company"))
	private Company company;
	@OneToMany(mappedBy="user")
	private List<UserInEvent> userInEvents = new ArrayList<>();
	@OneToMany(mappedBy="user")
	private Set<UserInPost> userInPosts = new HashSet<>();
	
	public User() {}

	public User(String firstname, String lastname, String email, String username, String password, String phone) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
*/