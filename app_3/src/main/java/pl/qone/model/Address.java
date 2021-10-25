package pl.qone.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, length=5)
	private String zip_code;
	@Column(nullable=false, length=100)
	private String city;
	@Column(nullable=false, length=100)
	private String street;
	@Column(nullable=false, length=5)
	private String house_number;

	@JsonManagedReference
	@OneToMany(mappedBy="address", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Department> departments = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy="address", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Company> companies = new ArrayList<>();
	
	
	public Address() {}

	public Address(String zip_code, String city, String street, String house_number) {
		this.zip_code = zip_code;
		this.city = city;
		this.street = street;
		this.house_number = house_number;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouse_number() {
		return house_number;
	}

	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	
	public void addDepartment(Department department) {
		departments.add(department);
		department.setAddress(this);
	}
	
	public void removeDepartment(Department department) {
		departments.remove(department);
		department.setAddress(null);
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	
	public void addCompany(Company company) {
		companies.add(company);
		company.setAddress(this);
	}
	
	public void removeCompany(Company company) {
		companies.remove(company);
		company.setAddress(null);
	}

	
	
}