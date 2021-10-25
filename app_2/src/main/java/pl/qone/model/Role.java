package pl.qone.model;

import javax.persistence.*;

@Entity
@Table(name="roles", uniqueConstraints= {@UniqueConstraint(name="unique_name_constraints", columnNames="name")})
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	//mapowanie wartości enum (typu String) na bazodanową reprezentację
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=25)
	private RoleEnum name;
	
	@Column(length=200)
	private String description;
	
	public Role() {}

	public Role(RoleEnum name) {
		this.name = name;
	}
	
	public Role(RoleEnum name, String description) {
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleEnum getName() {
		return name;
	}

	public void setName(RoleEnum name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
