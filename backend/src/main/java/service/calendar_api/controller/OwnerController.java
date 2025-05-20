package service.calendar_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.service.OwnerService;

import java.net.URI;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
	private final OwnerService service;

	@GetMapping("/{id}")
	public ResponseEntity<OwnerDTO> getOwner(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok(service.getOwner(id));
	}

	@PostMapping
	public ResponseEntity<OwnerDTO> createOwner(@RequestBody @Valid OwnerDTO request) {
		var owner = service.create(request);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(owner.getId()).toUri();

		return ResponseEntity.created(uri).body(owner);
	}

	@PutMapping("/{id}")
	public ResponseEntity<OwnerDTO> updateOwner(@PathVariable(value = "id") Long id,
												@RequestBody @Valid OwnerDTO request) {
		var owner = service.update(id, request);
		return ResponseEntity.accepted().body(owner);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOwner(@PathVariable(value = "id") Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
