package pl.qone.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.qone.model.Department;
import pl.qone.model.Event;
import pl.qone.model.EventImage;
import pl.qone.model.EventType;
import pl.qone.model.RoleInEventEnum;
import pl.qone.model.StatusEvent;
import pl.qone.model.StatusEventEnum;
import pl.qone.model.User;
import pl.qone.model.UserInEvent;
import pl.qone.payload.request.EventRequest;
import pl.qone.payload.response.EventResponse;
import pl.qone.payload.response.ImageResponse;
import pl.qone.payload.response.MessageResponse;
import pl.qone.payload.response.OneEventResponse;
import pl.qone.repository.DepartmentRepository;
import pl.qone.repository.EventImageRepository;
import pl.qone.repository.EventRepository;
import pl.qone.repository.EventTypeRepository;
import pl.qone.repository.StatusEventRepository;
import pl.qone.repository.UserInEventRepository;
import pl.qone.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/event")
public class EventController {
	
	@Autowired 
	EventRepository eventRepository;
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	EventTypeRepository eventTypeRepository;
	
	@Autowired
	StatusEventRepository statusEventRepository;
	
	@Autowired 
	DepartmentRepository departmentRepository;
	
	@Autowired 
	EventImageRepository eventImageRepository;
	
	@Autowired 
	UserInEventRepository userInEventRepository;
	
	@PostMapping("/image")
	public ResponseEntity<ImageResponse> uploadImage( @RequestParam("file") MultipartFile file) {
		String message = "";
		String imageId = null;
		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			EventImage image = new EventImage(fileName, file.getContentType(), file.getBytes());
			
			try {
				EventImage savedImage = eventImageRepository.save(image);
				message = "file uploaded successfully: " + file.getOriginalFilename();
				imageId = savedImage.getId();
				
			} catch (Exception e) {
	   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(new ImageResponse(message, imageId));
		} catch (Exception e) {
			message = "could not upload file: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImageResponse(message, imageId));
		}
	}
	
	@PostMapping("/event")
	public ResponseEntity<MessageResponse> saveEvent(@Valid @RequestBody EventRequest eventRequest) {
		String message = "";
		User user = null;
		try {
			Event event = new Event(eventRequest.getName(), eventRequest.getDescription(), eventRequest.getData_start(),
					eventRequest.getData_end(), eventRequest.getMax_number_of_contestant(), eventRequest.getPlace());
			
			if (eventRequest.getStatusEventId() != null) {
				StatusEvent statusEvent = statusEventRepository.findById(Long.valueOf(eventRequest.getStatusEventId())).orElse(null);
				if (statusEvent != null) {
					statusEvent.addEvent(event);
				}
			}
			if (eventRequest.getEventTypeId() != null) {
				EventType eventType = eventTypeRepository.findById(Long.valueOf(eventRequest.getEventTypeId())).orElse(null);
				if (eventType != null) {
					eventType.addEvent(event);
				}
			}
			if (eventRequest.getDepartmentId() != null) {
				Department department = departmentRepository.findById(Long.valueOf(eventRequest.getDepartmentId())).orElse(null);
				if (department != null) {
					department.addEvent(event);
				}
			}
			
			if(eventRequest.getImageId() != null) {
				EventImage image = eventImageRepository.getById(eventRequest.getImageId());
				event.addImage(image);
				try {
					eventImageRepository.save(image);
				} catch (Exception e) {
		   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				String currentPrincipalName = authentication.getName();
				System.out.println(currentPrincipalName);
				user = userRepository.findByUsername(currentPrincipalName).orElse(null);
			}
			
			if (eventRequest.getComment() != null) {
				if (!(authentication instanceof AnonymousAuthenticationToken)) {
					String currentPrincipalName = authentication.getName();
					event.setComments((event.getComments() == null ? "" : event.getComments()) + currentPrincipalName + ": " + eventRequest.getComment() + "\n");
				} 
			}
			
			try {
				RoleInEventEnum role;
				Event savedEvent = eventRepository.save(event);
				role = RoleInEventEnum.ORGANIZER;
				UserInEvent userInEvent = new UserInEvent(role);
				if (savedEvent !=  null) {
					savedEvent.addUser(userInEvent);
				}
				
				if (user != null) {
					user.addUser(userInEvent);
				}
				
				try {
					userInEventRepository.save(userInEvent);
				} catch (Exception e) {
					 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				
			} catch (Exception e) {
	   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			message = "Event saved successfully: ";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "could not save event";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
	
	@PutMapping("/event/{id}")
	public ResponseEntity<Event> updateEvent(@PathVariable("id") long id, @RequestBody EventRequest eventRequest) {
		Optional<Event> eventData = eventRepository.findById(id);
		Authentication authentication;
		User user = null;
		
		if (eventData.isPresent()) {
			Event ev = eventData.get();
			ev.setName(eventRequest.getName());
			ev.setDescription(eventRequest.getDescription());
			ev.setMax_number_of_contestant(eventRequest.getMax_number_of_contestant());
			ev.setData_start(eventRequest.getData_start());
			ev.setData_end(eventRequest.getData_end());
			ev.setPlace(eventRequest.getPlace());
			
			if (!eventRequest.getStatusEventId().equals(ev.getStatusEvent().getId().toString()) && eventRequest.getStatusEventId() != null) {
				ev.getStatusEvent().removeEvent(ev);
				StatusEvent statusEvent = statusEventRepository.findById(Long.valueOf(eventRequest.getStatusEventId())).orElse(null);
				if (statusEvent != null) {
					statusEvent.addEvent(ev);
				}
			}

			if (!eventRequest.getEventTypeId().equals(ev.getEventType().getId().toString()) && eventRequest.getEventTypeId() != null) {
				ev.getEventType().removeEvent(ev);
				EventType eventType = eventTypeRepository.findById(Long.valueOf(eventRequest.getEventTypeId())).orElse(null);
				if (eventType != null) {
					System.out.println(eventType.getId());
					eventType.addEvent(ev);
				}
			}
			if (!eventRequest.getDepartmentId().equals(ev.getDepartment().getId().toString()) && eventRequest.getDepartmentId() != null) {
				ev.getDepartment().removeEvent(ev);
				Department department = departmentRepository.findById(Long.valueOf(eventRequest.getDepartmentId())).orElse(null);
				if (department != null) {
					department.addEvent(ev);
				}
			}
			
			System.out.println("imageId = " + eventRequest.getImageId() + "ev.getimages().size = " + ev.getImages().size());
			if (eventRequest.getImageId() != null) {
				System.out.println("inside image if");
				if (ev.getImages().size() != 0) {
					EventImage imageToRemove = eventImageRepository.findById(ev.getImages().get(0).getId()).orElse(null);
					if (imageToRemove != null) {
						ev.removeImage(imageToRemove);
					}
				}
				EventImage image = eventImageRepository.getById(eventRequest.getImageId());
				ev.addImage(image);
				try {
					eventImageRepository.save(image);
				} catch (Exception e) {
		   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				String currentPrincipalName = authentication.getName();
				System.out.println(currentPrincipalName);
				user = userRepository.findByUsername(currentPrincipalName).orElse(null);
			}
			
			if (eventRequest.getComment() != null) {
				if (!(authentication instanceof AnonymousAuthenticationToken)) {
					String currentPrincipalName = authentication.getName();
					ev.setComments((ev.getComments() == null ? "" : ev.getComments()) + currentPrincipalName + ": " + eventRequest.getComment() + "\n");
				} 
			}
	
			boolean enrolled = false;
			for (UserInEvent userInEvent : ev.getUsers()) {
				if (userInEvent.getUser().equals(user)) {
					enrolled = true;
				}
			}
			if (enrolled == false) {
				RoleInEventEnum role = RoleInEventEnum.SUPERVISOR;
				UserInEvent userInEvent = new UserInEvent(role);
				if (ev !=  null) {
					ev.addUser(userInEvent);
				}
				
				if (user != null) {
					user.addUser(userInEvent);
				}
				
				try {
					userInEventRepository.save(userInEvent);
				} catch (Exception e) {
					 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			return new ResponseEntity<>(eventRepository.save(ev), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/events")
	public ResponseEntity<?> getAllEvents() {
		List<Event> events = new ArrayList<>();
		List<EventResponse> eventsRes = new ArrayList<>();
		byte[] encode = null;
		String data = null, organizer = null;
		long imageSize = 0;
		boolean czyZapisano = false, czyMoznaZapisac = false, czyMoznaOceniac = false;
		int contestantsInEvent = 0;
		float rate = 0;
		User user;
		Authentication authentication;
		LocalDate localDate = LocalDate.now();
		
		
		try {
			eventRepository.findAllOrdered().forEach(events::add);
			for(Event e : events) {
				if(e.getImages().size() != 0) {
					imageSize = e.getImages().get(0).getData().length;
					encode = java.util.Base64.getEncoder().encode(e.getImages().get(0).getData());
					data = new String(encode, "UTF-8");
				} else {
					imageSize = 0;
					encode = null;
					data = null;
				}
				authentication = SecurityContextHolder.getContext().getAuthentication();
				if (!(authentication instanceof AnonymousAuthenticationToken)) {
					String currentPrincipalName = authentication.getName();
					System.out.println(currentPrincipalName);
					user = userRepository.findByUsername(currentPrincipalName).orElse(null);
					czyZapisano = userInEventRepository.existsByEventAndUser(e, user); 
					if (czyZapisano) {
						rate = this.userInEventRepository.findByEventAndUser(e, user).getEventRate();
					} else {
						rate = 0;
					}
				} else {
					czyZapisano = false;
					rate = 0;
				}

				contestantsInEvent = userInEventRepository.countContestantInEvent(e);
				if (contestantsInEvent < e.getMax_number_of_contestant() && e.getStatusEvent().getName().equals(StatusEventEnum.ZAAKCEPTOWANY) &&
						e.getData_end().after(java.sql.Date.valueOf(localDate))) {
					czyMoznaZapisac = true;
				} else {
					czyMoznaZapisac = false;
				}
				if (czyZapisano && e.getStatusEvent().getName().equals(StatusEventEnum.ZAAKCEPTOWANY)
						&& e.getData_start().before(java.sql.Date.valueOf(localDate)) ) {
					czyMoznaOceniac = true;
				} else {
					czyMoznaOceniac = false;
				}
				
				UserInEvent userInEvent = userInEventRepository.findByEventAndRoleInEvent(e, RoleInEventEnum.ORGANIZER);
				if (userInEvent != null) {
					organizer = userInEvent.getUser().getUsername();
				} else {
					organizer = null;
				}
				
				EventResponse oneEventRes = new EventResponse(e.getId(),e.getName(),e.getDescription(),e.getMax_number_of_contestant(),
						e.getData_start(), e.getData_end(), e.getDepartment().getName(),e.getEventType().getName(),e.getStatusEvent().getName(),
						data, imageSize, czyZapisano, czyMoznaZapisac, czyMoznaOceniac, rate, e.getComments(), organizer, e.getPlace());
				eventsRes.add(oneEventRes);
			}
			return ResponseEntity.ok(eventsRes);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/events/{id}")
	public ResponseEntity<?> getEventById(@PathVariable("id") long id) {
		Optional<Event> eventData = eventRepository.findById(id);
		byte[] encode = null;
		String data = null;
		
		if(eventData.isPresent()) {
			Event event = eventData.get();
			if(event.getImages().size() != 0) {
				encode = java.util.Base64.getEncoder().encode(event.getImages().get(0).getData());
				try {
					data = new String(encode, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				encode = null;
				data = null;
			}
			OneEventResponse oneEvent = new OneEventResponse(event.getId(), event.getName(), event.getDescription(), event.getMax_number_of_contestant(),
					event.getData_start(), event.getData_end(), event.getDepartment().getName(), event.getEventType().getName(),
					event.getStatusEvent().getName(), data ,event.getComments(), event.getPlace());
			return ResponseEntity.ok(oneEvent);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping("/eventTypes")
    public ResponseEntity<List<EventType>> getAlleventTypes() {
		try {
			List<EventType> types = new ArrayList<>();
			eventTypeRepository.findAll().forEach(types::add);
			return new ResponseEntity<>(types, HttpStatus.OK);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
    @GetMapping("/statusEvent")
    public ResponseEntity<List<StatusEvent>> getAllStatusEvent() {
		try {
			List<StatusEvent> status = new ArrayList<>();
			statusEventRepository.findAll().forEach(status::add);
			return new ResponseEntity<>(status, HttpStatus.OK);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
