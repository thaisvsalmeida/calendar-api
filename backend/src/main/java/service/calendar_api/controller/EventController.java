package service.calendar_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.calendar_api.dto.EventDTO;
import service.calendar_api.enums.Status;
import service.calendar_api.service.EventService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
	private final EventService service;

	@GetMapping
	public ResponseEntity<List<EventDTO>> findAllEventsByDateAndOwner(@RequestParam Long ownerId,
																	  @RequestParam LocalDateTime startDateTime,
																	  @RequestParam LocalDateTime endDateTime) {
		List<EventDTO> events = service.findAllEventsByDateAndOwner(ownerId, startDateTime, endDateTime);
		return ResponseEntity.ok(events);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventDTO> findEventById(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok(service.getEventById(id));
	}

	@PostMapping
	public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid EventDTO request) {
		var event = service.createEvent(request);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(event.getId()).toUri();

		return ResponseEntity.created(uri).body(event);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EventDTO> updateEvent(@PathVariable(value = "id") Long id,
												@RequestBody @Valid EventDTO request) {
		var event = service.updateEvent(id, request);
		return ResponseEntity.accepted().body(event);
	}

	@PutMapping("/{id}/complete")
	public ResponseEntity<EventDTO> completeEvent(@PathVariable(value = "id") Long id) {
		var event = service.updateStatusEvent(id, Status.DONE);
		return ResponseEntity.accepted().body(event);
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<EventDTO> cancelEvent(@PathVariable(value = "id") Long id) {
		var event = service.updateStatusEvent(id, Status.CANCELLED);
		return ResponseEntity.accepted().body(event);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<EventDTO> deleteEvent(@PathVariable(value = "id") Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
