package service.calendar_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.calendar_api.dto.EventDTO;
import service.calendar_api.dto.RecurringEventDTO;
import service.calendar_api.service.RecurringEventService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/recurring-event")
@RequiredArgsConstructor
public class RecurringEventController {
	private final RecurringEventService service;

	@GetMapping
	public ResponseEntity<List<RecurringEventDTO>> findAllRecurringEventsByOwner(@RequestParam Long ownerId) {
		List<RecurringEventDTO> events = service.findAllRecurringEventsByOwner(ownerId);
		return ResponseEntity.ok(events);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RecurringEventDTO> findEventById(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok(service.getRecurringEventById(id));
	}

	@PostMapping
	public ResponseEntity<RecurringEventDTO> createRecurringEvent(@RequestBody @Valid RecurringEventDTO request) {
		var event = service.createRecurringEvent(request);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(event.getId()).toUri();

		return ResponseEntity.created(uri).body(event);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RecurringEventDTO> updateRecurringEvent(@PathVariable(value = "id") Long id,
																  @RequestBody @Valid RecurringEventDTO request) {
		var event = service.updateRecurringEvent(id, request);
		return ResponseEntity.accepted().body(event);
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<RecurringEventDTO> cancelEvent(@PathVariable(value = "id") Long id) {
		var event = service.cancelRecurringEvent(id);
		return ResponseEntity.accepted().body(event);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<EventDTO> deleteRecurringEvent(@PathVariable(value = "id") Long id) {
		service.deleteRecurringEventById(id);
		return ResponseEntity.noContent().build();
	}
}
