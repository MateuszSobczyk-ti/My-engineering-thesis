package pl.qone.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.qone.model.Company;
import pl.qone.model.Department;
import pl.qone.model.Role;
import pl.qone.model.RoleEnum;
import pl.qone.model.User;
import pl.qone.payload.request.LoginRequest;
import pl.qone.payload.request.SignUpRequest;
import pl.qone.payload.response.JwtResponse;
import pl.qone.payload.response.MessageResponse;
import pl.qone.repository.CompanyRepository;
import pl.qone.repository.DepartmentRepository;
import pl.qone.repository.RoleRepository;
import pl.qone.repository.UserRepository;
import pl.qone.security.JwtUtils;
import pl.qone.security.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
 
		String depName = userDetails.getDepartment() == null ? null : userDetails.getDepartment().getName();
		String compName = userDetails.getCompany() == null ? null : userDetails.getCompany().getName();
		String compNip = userDetails.getCompany() == null ? null : userDetails.getCompany().getNip();
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getPhone(),
												 roles,
												 depName,
												 compName,
												 compNip
												 ));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		final String EMAIL_PATTERN_POLLUB_STUDENT = "^(.+)@pollub.edu.pl$";
		final String EMAIL_PATTERN_POLLUB_EMPL = "^(.+)@pollub.pl$";
    	final String EMAIL_PATTERN = "^(.+)@(.+)$";
    	final Pattern patternPollubStudent = Pattern.compile(EMAIL_PATTERN_POLLUB_STUDENT);
    	final Pattern patternPollubEmpl = Pattern.compile(EMAIL_PATTERN_POLLUB_EMPL);
    	final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
		
		if(userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
		}
			
			// Create new user's account
			User user = new User(signUpRequest.getUsername(),
								 encoder.encode(signUpRequest.getPassword()),
								 signUpRequest.getPhone());
			
			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();
			Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						
			System.out.println(signUpRequest.getCompId());
			System.out.println(signUpRequest.getDepId());
			if (strRoles == null) {
				if (signUpRequest.getDepId() != null && patternPollubEmpl.matcher(signUpRequest.getUsername()).matches()) {
					Role depEmplRole = roleRepository.findByName(RoleEnum.ROLE_EMPL_DEPARTMENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(depEmplRole);
					roles.add(userRole);
				} else if (signUpRequest.getCompId() != null && patternEmail.matcher(signUpRequest.getUsername()).matches()) {
					Role compEmplRole = roleRepository.findByName(RoleEnum.ROLE_EMPL_COMPANY)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(compEmplRole);
					roles.add(userRole);
				} else if (signUpRequest.getDepId() != null && patternPollubStudent.matcher(signUpRequest.getUsername()).matches()) {
					roles.add(userRole);
				} else {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is not valid!"));
				}
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;
					case "mod":
						Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);
						break;
					case "depEmpl":
						Role depEmplRole = roleRepository.findByName(RoleEnum.ROLE_EMPL_DEPARTMENT)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(depEmplRole);
						break;
					case "compEmpl":
						Role compEmplRole = roleRepository.findByName(RoleEnum.ROLE_EMPL_COMPANY)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(compEmplRole);
						break;
					case "user":
						roles.add(userRole);
						break;
					default:
						roles.add(userRole);
					}
				});
			}
			user.setRoles(roles);
			
			if (signUpRequest.getDepId() != null) {
				Department dep = departmentRepository.findById(Long.valueOf(signUpRequest.getDepId())).orElse(null);
				if (dep != null) {
					dep.addUser(user);
				}
			}
			if (signUpRequest.getCompId() != null) {
				Company comp = companyRepository.findById(Long.valueOf(signUpRequest.getCompId())).orElse(null);
				if (comp != null) {
					comp.addUser(user);
				} 
			}
			userRepository.save(user);

			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
	
