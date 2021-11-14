package pl.qone.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.qone.model.Department;
import pl.qone.model.Event;
import pl.qone.model.EventImage;
import pl.qone.model.EventType;
import pl.qone.model.StatusEvent;
import pl.qone.model.StatusEventEnum;
import pl.qone.model.User;
import pl.qone.payload.request.EventRequest;
import pl.qone.payload.response.EventResponse;
import pl.qone.payload.response.ImageResponse;
import pl.qone.payload.response.MessageResponse;
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
		try {
//			System.out.println(eventRequest.getName() + eventRequest.getDescription() + eventRequest.getData_start() + 
//					eventRequest.getData_end() + eventRequest.getDepartmentId() + eventRequest.getEventTypeId() + 
//					eventRequest.getImageId() + eventRequest.getMax_number_of_contestant() + eventRequest.getStatusEventId());
			Event event = new Event(eventRequest.getName(), eventRequest.getDescription(), eventRequest.getData_start(),
					eventRequest.getData_end(), eventRequest.getMax_number_of_contestant());
			
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
			
			try {
				eventRepository.save(event);
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
	
	@GetMapping("/events")
	public ResponseEntity<?> getAllEvents() {
		List<Event> events = new ArrayList<>();
		List<EventResponse> eventsRes = new ArrayList<>();
		byte[] encode = null;
		String data = null;
		long imageSize = 0;
		boolean czyZapisano = false, czyMoznaZapisac = false, czyMoznaOceniac = false;
		int contestantsInEvent = 0, rate = 0;
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
				System.out.println(e.getId());
				contestantsInEvent = userInEventRepository.countContestantInEvent(e);
				System.out.println(contestantsInEvent);
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
			
				
				EventResponse oneEventRes = new EventResponse(e.getId(),e.getName(),e.getDescription(),e.getMax_number_of_contestant(),
						e.getData_start(),e.getData_end(),e.getDepartment().getName(),e.getEventType().getName(),e.getStatusEvent().getName(),
						data, imageSize, czyZapisano, czyMoznaZapisac, czyMoznaOceniac, rate);
				eventsRes.add(oneEventRes);
			}
			return ResponseEntity.ok(eventsRes);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
