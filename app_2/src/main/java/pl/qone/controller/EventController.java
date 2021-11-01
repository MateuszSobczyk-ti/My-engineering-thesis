package pl.qone.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import pl.qone.payload.request.EventRequest;
import pl.qone.payload.response.EventResponse;
import pl.qone.payload.response.MessageResponse;
import pl.qone.repository.DepartmentRepository;
import pl.qone.repository.EventImageRepository;
import pl.qone.repository.EventRepository;
import pl.qone.repository.EventTypeRepository;
import pl.qone.repository.StatusEventRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/event")
public class EventController {
	
	@Autowired 
	EventRepository eventRepository;
	
	@Autowired
	EventTypeRepository eventTypeRepository;
	
	@Autowired
	StatusEventRepository statusEventRepository;
	
	@Autowired 
	DepartmentRepository departmentRepository;
	
	@Autowired 
	EventImageRepository eventImageRepository;
	
	@PostMapping("/image")
	public ResponseEntity<MessageResponse> uploadImage( @RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			EventImage image = new EventImage(fileName, file.getContentType(), file.getBytes());
			
			try {
				EventImage savedImage = eventImageRepository.save(image);
				message = "file uploaded successfully: " + savedImage.getId();
			} catch (Exception e) {
	   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "could not upload file: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
	
	@PostMapping("/event")
	public ResponseEntity<MessageResponse> saveEvent(@Valid @RequestBody EventRequest eventRequest) {
		String message = "";
		try {
			Event event = new Event(eventRequest.getName(), eventRequest.getDescription(), eventRequest.getData_start(),
					eventRequest.getData_end());
			StatusEvent statusEvent = statusEventRepository.getById(eventRequest.getStatusEventId());
			EventType eventType = eventTypeRepository.getById(eventRequest.getEventTypeId());
			Department department = departmentRepository.getById(eventRequest.getDepartmentId());
			department.addEvent(event);
			eventType.addEvent(event);
			statusEvent.addEvent(event);
			
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
		try {
			List<Event> events = new ArrayList<>();
			List<EventResponse> eventsRes = new ArrayList<>();
			byte[] encode = null;
			String data = null;
			long imageSize = 0;
			eventRepository.findAll().forEach(events::add);
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
				EventResponse oneEventRes = new EventResponse(e.getId(),e.getName(),e.getDescription(),e.getMax_number_of_contestant(),
						e.getData_start(),e.getData_end(),e.getDepartment().getName(),e.getEventType().getName(),e.getStatusEvent().getName(),
						data, imageSize);
				eventsRes.add(oneEventRes);
			}
			return ResponseEntity.ok(eventsRes);
		} catch (Exception e) {
   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@GetMapping("/images")
//	public ResponseEntity<?> getAllImages() {
//		try {
//			List<EventImage> images = new ArrayList<>();
//			eventImageRepository.findAll().forEach(images::add);
//			byte[] encode = null;
//			for(EventImage im : images) {
//				encode = java.util.Base64.getEncoder().encode(im.getData());
//			}
//			System.out.println(new String(encode, "UTF-8").length());
//			return ResponseEntity.ok(new String(encode, "UTF-8"));
//		} catch (Exception e) {
//   	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
}
