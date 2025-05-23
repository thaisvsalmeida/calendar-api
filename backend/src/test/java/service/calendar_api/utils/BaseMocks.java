package service.calendar_api.utils;

import service.calendar_api.dto.EventDTO;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.dto.RecurringEventDTO;
import service.calendar_api.entity.Event;
import service.calendar_api.entity.Owner;
import service.calendar_api.entity.RecurringEvent;
import service.calendar_api.enums.Label;
import service.calendar_api.enums.Status;

import java.time.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseMocks {
	public static final LocalDateTime now = LocalDateTime.now();
	public static final Date dateNow = Date.from(Instant.now());
	public static final LocalTime localTimeNow = LocalTime.now();
	public static final LocalDate localDateNow = LocalDate.now();

	public static OwnerDTO getOwnerDTOMock() {
		var dto = new OwnerDTO();
		dto.setName("Charlie");
		dto.setEmail("charlie@gmail.com");
		return dto;
	}

	public static Owner getOwnerMock() {
		return new Owner(1L, "Charlie", "charlie@gmail.com", dateNow, dateNow);
	}

	public static EventDTO getEventDTOMock() {
		return new EventDTO(1L, "Meeting", "Work meeting", now,
				now.plusHours(1), Label.WORK, Status.TO_DO, 1L);
	}

	public static Event getEventMock() {
		return new Event(1L, "Meeting", "Work meeting", now,
				now.plusHours(1), Label.WORK, Status.TO_DO, getOwnerMock(), dateNow, dateNow);
	}

	public static RecurringEventDTO getRecurringEventDTOMock() {
		return new RecurringEventDTO(1L, "Daily", "Daily meeting", localTimeNow,
				localTimeNow.plusHours(1), localDateNow, null, getDaysOfWeek(),
				Label.WORK, Status.TO_DO, 1L);
	}

	public static RecurringEvent getRecurringEventMock() {
		return new RecurringEvent(1L, "Daily", "Daily meeting", localTimeNow,
				localTimeNow.plusHours(1), localDateNow, null, getDaysOfWeek(),
				Label.WORK, Status.TO_DO, getOwnerMock(), dateNow, dateNow);
	}

	public static Set<DayOfWeek> getDaysOfWeek() {
		return new HashSet<>(List.of(DayOfWeek.values()));
	}
}
