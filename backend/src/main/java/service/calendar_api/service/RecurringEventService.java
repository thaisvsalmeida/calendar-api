package service.calendar_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.calendar_api.dto.RecurringEventDTO;
import service.calendar_api.entity.RecurringEvent;
import service.calendar_api.enums.Status;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.mapper.RecurringEventMapper;
import service.calendar_api.repository.RecurringEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringEventService {
	private final RecurringEventRepository repository;
	private final OwnerService ownerService;

	public List<RecurringEventDTO> findAllRecurringEventsByOwner(Long ownerId) {
		List<RecurringEvent> events = repository.findAllByOwnerId(ownerId);
		return RecurringEventMapper.INSTANCE.toRecurringEventDTOList(events);
	}

	private RecurringEvent findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurring event not found"));
	}

	public RecurringEventDTO getRecurringEventById(Long id) {
		return RecurringEventMapper.INSTANCE.toRecurringEventDTO(findById(id));
	}

	public RecurringEventDTO createRecurringEvent(RecurringEventDTO request) {
		var owner = ownerService.getOwner(request.getOwnerId());

		var event = repository.save(RecurringEventMapper.INSTANCE.toRecurringEvent(request, owner));
		return RecurringEventMapper.INSTANCE.toRecurringEventDTO(event);
	}

	public RecurringEventDTO updateRecurringEvent(Long id, RecurringEventDTO request) {
		var event = findById(id);
		RecurringEventMapper.INSTANCE.updateRecurringEventFromDTO(request, event);

		event = repository.save(event);
		return RecurringEventMapper.INSTANCE.toRecurringEventDTO(event);
	}

	public RecurringEventDTO cancelRecurringEvent(Long id) {
		var event = findById(id);

		if (Status.TO_DO.equals(event.getStatus())) {
			event.setStatus(Status.CANCELLED);
			event = repository.save(event);
		}

		return RecurringEventMapper.INSTANCE.toRecurringEventDTO(event);
	}

	public void deleteRecurringEventById(Long id) {
		repository.deleteById(id);
	}
}
