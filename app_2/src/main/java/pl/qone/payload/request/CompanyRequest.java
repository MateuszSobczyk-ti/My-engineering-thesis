package pl.qone.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompanyRequest {

	@NotBlank
	@Size(min=5,max=8)
	private String zip_code;
	@NotBlank
	private String city;
	@NotBlank
	private String street;
	@NotBlank
	@Size(max=6)
	private String house_number;
	@NotBlank
	private String name;
	@NotBlank
	@Size(min=10, max=10)
	private String nip;
	
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
	
	
}
