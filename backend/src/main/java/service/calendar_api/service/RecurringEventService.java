package service.calendar_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.calendar_api.dto.RecurringEventDTO;
import service.calendar_api.entity.BlockedRecurringEvent;
import service.calendar_api.entity.Event;
import service.calendar_api.entity.RecurringEvent;
import service.calendar_api.enums.Status;
import service.calendar_api.exception.DuplicatedBlockException;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.mapper.RecurringEventMapper;
import service.calendar_api.repository.BlockedRecurringEventRepository;
import service.calendar_api.repository.RecurringEventRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurringEventService {
	private final RecurringEventRepository repository;
	private final OwnerService ownerService;
	private final BlockedRecurringEventRepository blockedRecurringEventRepository;

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

	public void addBlocksToRecurringEvents(Long recurringEventId, LocalDate date) {
		RecurringEvent recurringEvent = findById(recurringEventId);

		boolean exists = blockedRecurringEventRepository.existsByRecurringEventAndDate(recurringEvent, date);
		if (exists) {
			throw new DuplicatedBlockException("Block already exists for this date");
		}

		blockedRecurringEventRepository.save(new BlockedRecurringEvent(date, recurringEvent));
	}

	public List<Event> getEventsFromRecurringEvents(Long ownerId, LocalDate start, LocalDate end) {
		List<Event> events = new ArrayList<>();

		List<RecurringEvent> recurringEvents = repository
				.findAllByOwnerIdAndStartDateBetween(ownerId, start, end);

		recurringEvents.forEach(recurringEvent -> {
			List<BlockedRecurringEvent> blocks = blockedRecurringEventRepository
					.findAllByRecurringEventAndDateBetween(recurringEvent, start, end);
			events.addAll(generateEvents(recurringEvent, start, end, blocks));
		});

		return events;
	}

	private List<Event> generateEvents(RecurringEvent recurringEvent, LocalDate startDate,
									  LocalDate endDate, List<BlockedRecurringEvent> blocks) {
		List<Event> events = new ArrayList<>();

		Set<LocalDate> blockDates = blocks.stream()
				.map(BlockedRecurringEvent::getDate)
				.collect(Collectors.toSet());

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			DayOfWeek day = DayOfWeek.valueOf(date.getDayOfWeek().name());

			if (recurringEvent.getDaysOfWeek().contains(day) && !blockDates.contains(date)) {
				LocalDateTime start = LocalDateTime.of(date, recurringEvent.getStartTime());
				LocalDateTime end = recurringEvent.getEndTime().isAfter(recurringEvent.getStartTime())
						? LocalDateTime.of(date, recurringEvent.getEndTime())
						: LocalDateTime.of(date.plusDays(1), recurringEvent.getEndTime());

				Event e = new Event(recurringEvent.getTitle(), recurringEvent.getDescription(), start, end,
						recurringEvent.getLabel(), recurringEvent.getStatus(), recurringEvent.getOwner());

				events.add(e);
			}
		}

		return events;
	}
}
