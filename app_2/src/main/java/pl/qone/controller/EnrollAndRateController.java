package pl.qone.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.qone.model.Event;
import pl.qone.model.RoleInEventEnum;
import pl.qone.model.User;
import pl.qone.model.UserInEvent;
import pl.qone.payload.request.EnrollRequest;
import pl.qone.payload.request.RateRequest;
import pl.qone.payload.response.MessageResponse;
import pl.qone.repository.EventRepository;
import pl.qone.repository.UserInEventRepository;
import pl.qone.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/event")
public class EnrollAndRateController {
	
	@Autowired 
	EventRepository eventRepository;
	
	@Autowired 
	UserInEventRepository userInEventRepository;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/enroll")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<MessageResponse> enrollToEvent(@Valid @RequestBody EnrollRequest enrollRequest) {
		RoleInEventEnum role;
		User user = null;
		String message = "";
		
		switch (enrollRequest.getRoleInEvent()) {
		case "contestant":
			role = RoleInEventEnum.CONTESTANT;
			break;
		case "organizer":
			role = RoleInEventEnum.ORGANIZER;
			break;
		case "supervisor":
			role = RoleInEventEnum.SUPERVISOR;
			break;
		default:
			role = RoleInEventEnum.CONTESTANT;
		}
		
		try {
			UserInEvent userInEvent = new UserInEvent(role);
			
			if (enrollRequest.getEventId() != null) {
				Event event = eventRepository.findById(Long.valueOf(enrollRequest.getEventId())).orElse(null);
				if (event != null) {
					event.addUser(userInEvent);
				}
			}
			
			if (enrollRequest.getUserId() != null) {
				user = userRepository.findById(Long.valueOf(enrollRequest.getUserId())).orElse(null);
			} else {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (!(authentication instanceof AnonymousAuthenticationToken)) {
					String currentPrincipalName = authentication.getName();
					user = userRepository.findByUsername(currentPrincipalName).orElse(null);
				}
			}
			
			if (user != null) {
				user.addUser(userInEvent);
			}
			
			try {
				userInEventRepository.save(userInEvent);
			} catch (Exception e) {
				 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			message = "User enrolled to event successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
			} catch (Exception e) {
			message = "could not enroll user to event";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
	
	@PostMapping("/rate")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<MessageResponse> rateEvent(@Valid @RequestBody RateRequest rateRequest) {
		
		UserInEvent userInEvent = null;
		Authentication authentication;
		String message = "";
		Event event = null;
		User user = null;
		
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				String currentPrincipalName = authentication.getName();
				System.out.println(currentPrincipalName);
				user = userRepository.findByUsername(currentPrincipalName).orElse(null);
			} else {
				message = "cannot find logged in user";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
			}
			
			if (rateRequest.getEventId() != null) {
				event = this.eventRepository.findById(Long.valueOf(rateRequest.getEventId())).orElse(null);
			} 
			
			if (event != null && user != null) {
				userInEvent = this.userInEventRepository.findByEventAndUser(event, user);
			}
			
			if (userInEvent != null) {
				userInEvent.setEventRate(rateRequest.getRate());
			}
			
			try {
				this.userInEventRepository.save(userInEvent);
			} catch (Exception e) {
				 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			message = "User rated event successfully";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "cannot save rate";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
}
