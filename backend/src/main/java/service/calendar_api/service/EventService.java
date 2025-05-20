package service.calendar_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.calendar_api.dto.EventDTO;
import service.calendar_api.entity.Event;
import service.calendar_api.enums.Status;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.mapper.EventMapper;
import service.calendar_api.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
	private final EventRepository repository;
	private final OwnerService ownerService;

	public List<EventDTO> findAllEventsByDateAndOwner(Long ownerId,
													  LocalDateTime startDateTime,
													  LocalDateTime endDateTime) {
		List<Event> events = repository.findAllByOwnerIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(ownerId, startDateTime, endDateTime);
		return EventMapper.INSTANCE.toEventDTOList(events);
	}

	private Event findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found"));
	}

	public EventDTO getEventById(Long id) {
		return EventMapper.INSTANCE.toEventDTO(findById(id));
	}

	public EventDTO createEvent(EventDTO request) {
		var owner = ownerService.getOwner(request.getOwnerId());

		var event = repository.save(EventMapper.INSTANCE.toEvent(request, owner));
		return EventMapper.INSTANCE.toEventDTO(event);
	}

	public EventDTO updateEvent(Long id, EventDTO request) {
		var event = findById(id);
		EventMapper.INSTANCE.updateEventFromDTO(request, event);

		event = repository.save(event);
		return EventMapper.INSTANCE.toEventDTO(event);
	}

	public EventDTO updateStatusEvent(Long id, Status status) {
		var event = findById(id);

		if (Status.TO_DO.equals(event.getStatus())) {
			event.setStatus(status);
			event = repository.save(event);
		}

		return EventMapper.INSTANCE.toEventDTO(event);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
