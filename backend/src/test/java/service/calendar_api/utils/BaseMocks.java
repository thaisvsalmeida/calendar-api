package service.calendar_api.utils;

import service.calendar_api.dto.EventDTO;
import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Event;
import service.calendar_api.entity.Owner;
import service.calendar_api.enums.Label;
import service.calendar_api.enums.Status;

import java.time.LocalDateTime;

public class BaseMocks {

	public static OwnerDTO getOwnerDTOMock() {
		var dto = new OwnerDTO();
		dto.setName("Charlie");
		dto.setEmail("charlie@gmail.com");
		return dto;
	}

	public static Owner getOwnerMock() {
		return new Owner(1L, "Charlie", "charlie@gmail.com");
	}

	public static EventDTO getEventDTOMock() {
		return new EventDTO(1L, "Meeting", "Work meeting", LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), Label.WORK, Status.TO_DO, 1L);
	}

	public static Event getEventMock() {
		return new Event(1L, "Meeting", "Work meeting", LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), Label.WORK, Status.TO_DO, getOwnerMock());
	}
}
