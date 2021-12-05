package pl.qone.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import pl.qone.exportPdf.EventStatementPdfExporter;
import pl.qone.model.Event;
import pl.qone.model.EventType;
import pl.qone.model.Role;
import pl.qone.model.RoleInEventEnum;
import pl.qone.model.User;
import pl.qone.model.UserInEvent;
import pl.qone.payload.request.EventTypeRequest;
import pl.qone.payload.request.RoleRequest;
import pl.qone.payload.response.EventStatementResponse;
import pl.qone.payload.response.MessageResponse;
import pl.qone.payload.response.UserResponse;
import pl.qone.repository.EventRepository;
import pl.qone.repository.EventTypeRepository;
import pl.qone.repository.RoleRepository;
import pl.qone.repository.UserInEventRepository;
import pl.qone.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	RoleRepository roleRepository;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired 
	UserInEventRepository userInEventRepository;
	
	@Autowired
	EventTypeRepository eventTypeRepository;
	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllUsers() {
		List<User> users = new ArrayList<>();
		List<UserResponse> usersRes = new ArrayList<>();
		Double avgRate = null;
		int numberSigned = 0;
		
		try {
			userRepository.findAll().forEach(users::add);
			for (User u : users) {
				avgRate = userInEventRepository.averageEventUserRate(u);
				numberSigned = userInEventRepository.countContestantInEventUser(u);
				UserResponse singleUserRes = new UserResponse(u.getId(), u.getUsername(), u.getPhone(), 
						u.getDepartment()==null ? null : u.getDepartment().getName(), 
						u.getCompany()==null ? null : u.getCompany().getName(), u.getRoles(), avgRate, numberSigned);
				usersRes.add(singleUserRes);
			}
			return ResponseEntity.ok(usersRes);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		try { 
			User user = userRepository.findById(id).orElse(null);
			if (user != null) {
				user.setDeleted(1);
				userRepository.save(user);
			} else {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: user not found!"));
			}
			return ResponseEntity.ok(new MessageResponse("User archivised successfully"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: cannot arhcivise user!"));
	    }
	}
	
	@PutMapping("userAddRole/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addRoleUser(@RequestBody RoleRequest newRole, @PathVariable("id") long id) {
		System.out.println("newRole = " + newRole.getRole());
		Optional<User> userData = userRepository.findById(id);
		Optional<Role> roleData = roleRepository.findById(newRole.getRole());
		Role role; 
		User user;
		
		if(userData.isPresent() && roleData.isPresent()) {
			user = userData.get();
			role = roleData.get();
			Set<Role> userRoles = user.getRoles();
			if (!userRoles.contains(role)) {
				userRoles.add(role);
				user.setRoles(userRoles);
				userRepository.save(user);
			}
			return ResponseEntity.ok(new MessageResponse("Role " + role.getName().name() + " added successfully to " + user.getUsername()));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: user or role not found!"));
		}
	}
	
	@PutMapping("userDeleteRole/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteRoleUser(@PathVariable("id") long id, @RequestBody RoleRequest oldRole) {
		Optional<User> userData = userRepository.findById(id);
		Optional<Role> roleData = roleRepository.findById(oldRole.getRole());
		Role role; 
		User user;
		
		if(userData.isPresent() && roleData.isPresent()) {
			user = userData.get();
			role = roleData.get();
			Set<Role> userRoles = user.getRoles();
			if (userRoles.contains(role)) {
				userRoles.remove(role);
				user.setRoles(userRoles);
				userRepository.save(user);
			}
			return ResponseEntity.ok(new MessageResponse("Role " + role.getName().name() + " detached successfully from " + user.getUsername()));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: user or role not found!"));
		}
	}
	
    @GetMapping("/roles")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
		try {
			List<Role> roles = new ArrayList<>();
			roleRepository.findAll().forEach(roles::add);
			return new ResponseEntity<>(roles, HttpStatus.OK);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("/eventStatement")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEventsStatement() {
    	List<Event> events = new ArrayList<>();
    	List<EventStatementResponse> eventsStatement = new ArrayList<>();
    	
    	try {
    		eventRepository.findAll().forEach(events::add);
    		eventsStatement = this.getEventsStatement(events);
    		return ResponseEntity.ok(eventsStatement);
    	} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @PostMapping("/eventType")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createEventType(@Valid @RequestBody EventTypeRequest eventTypeRequest) {
    	
		if (eventTypeRepository.existsByName(eventTypeRequest.getName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Event type name is already taken!"));
		}
	
		EventType type = new EventType(eventTypeRequest.getName());
		
		
		try {
			eventTypeRepository.save(type);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(new MessageResponse("Event type " + type.getName() + " added successfully!"));
	  }
    
    @GetMapping("/eventExportPdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> exportPdf() {
    	List<EventStatementResponse> eventsStatement = new ArrayList<>();
    	List<Event> events = new ArrayList<>();
    	
    	eventRepository.findAll().forEach(events::add);
    	eventsStatement = this.getEventsStatement(events);
        ByteArrayInputStream bis = EventStatementPdfExporter.eventsReportList(eventsStatement);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    
    private List<EventStatementResponse> getEventsStatement(List<Event> events) {
    	List<EventStatementResponse> eventsStatement = new ArrayList<>();
    	Double averageRate = null;
    	String organizer = null;
    	String company = null;
    	double contestantsPercentage = 0.0;
    	int contestants = 0;
    	
		for (Event e : events) {
			averageRate = userInEventRepository.averageEventRate(e);
			if (averageRate == null) {
				averageRate = 0.0;
			} else {
				averageRate = Math.round(averageRate * 10.0) / 10.0;
			}
			UserInEvent userInEvent = userInEventRepository.findByEventAndRoleInEvent(e, RoleInEventEnum.ORGANIZER);
			if (userInEvent != null) {
				organizer = userInEvent.getUser().getUsername();
				company = userInEvent.getUser().getCompany() == null ? (userInEvent.getUser().getDepartment() == null ? null : 
					userInEvent.getUser().getDepartment().getName()) :	userInEvent.getUser().getCompany().getName();
			} else {
				organizer = null;
			}
			
			contestants = userInEventRepository.countContestantInEvent(e);
			if (e.getMax_number_of_contestant() != 0) {
				contestantsPercentage = contestants * 100.0/e.getMax_number_of_contestant();
			} else {
				contestantsPercentage = 0.0;
			}
			
			EventStatementResponse singleEvent = new EventStatementResponse(e.getName(),
					averageRate, contestants + "/" + e.getMax_number_of_contestant(), organizer, contestantsPercentage, company);
			eventsStatement.add(singleEvent);
		}
		eventsStatement.sort(Comparator.comparing(EventStatementResponse::getAverageRate).reversed());
		return eventsStatement;
    }
    
}