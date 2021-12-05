package pl.qone.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.qone.model.Address;
import pl.qone.model.Company;
import pl.qone.model.Department;
import pl.qone.payload.request.CompanyRequest;
import pl.qone.payload.request.DepRequest;
import pl.qone.payload.response.MessageResponse;
import pl.qone.repository.AddressRepository;
import pl.qone.repository.CompanyRepository;
import pl.qone.repository.DepartmentRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/org")
public class DepartmentCompanyController {
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired 
	DepartmentRepository departmentRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
    @PostMapping("/department")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepRequest depRequest) {
    	
		if (departmentRepository.existsByName(depRequest.getName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Department name is already taken!"));
		}
	
		Address address = new Address(depRequest.getZip_code(), depRequest.getCity(), depRequest.getStreet(), depRequest.getHouse_number());
		Department department = new Department(depRequest.getName());
		
		address.addDepartment(department);
		
		
		try {
			addressRepository.save(address);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(new MessageResponse("Department added successfully!"));
	  }
    
    @PostMapping("/company")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequest compRequest) {
    	
		if (companyRepository.existsByName(compRequest.getName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Company name is already taken!"));
		}
	
		Address address = new Address(compRequest.getZip_code(), compRequest.getCity(), compRequest.getStreet(), compRequest.getHouse_number());
		Company company = new Company(compRequest.getName(), compRequest.getNip());

		address.addCompany(company);	
		
		try {
			addressRepository.save(address);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(new MessageResponse("Company added successfully!"));
	  } 
    
    
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
		try {
			List<Department> deps = new ArrayList<>();
			departmentRepository.findAll().forEach(deps::add);
			return new ResponseEntity<>(deps, HttpStatus.OK);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
		try {
			List<Company> comps = new ArrayList<>();
			companyRepository.findAll().forEach(comps::add);
			return new ResponseEntity<>(comps, HttpStatus.OK);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
