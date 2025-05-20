package service.calendar_api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.calendar_api.entity.Event;
import service.calendar_api.enums.Status;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.repository.EventRepository;
import service.calendar_api.utils.BaseMocks;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
	@InjectMocks
	private EventService service;
	@Mock
	private EventRepository repository;
	@Mock
	private OwnerService ownerService;
	@Captor
	private ArgumentCaptor<Event> eventArgumentCaptor;

	@Test
	void findAllEventsByDateAndOwnerShouldReturnEventDtoList() {
		when(repository.findAllByOwnerIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(anyLong(), any(), any()))
				.thenReturn(Collections.singletonList(BaseMocks.getEventMock()));

		var response = service.findAllEventsByDateAndOwner(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

		Assertions.assertNotNull(response);
		Assertions.assertEquals(1, response.size());
	}

	@Test
	void getEventShouldReturnEventDtoWhenExists() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getEventMock()));

		var response = service.getEventById(1L);

		Assertions.assertNotNull(response);
	}

	@Test
	void getEventShouldThrowsResourceNotFoundExceptionWhenNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		var ex = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> service.getEventById(1L));

		Assertions.assertNotNull(ex);
		Assertions.assertEquals("Event not found", ex.getMessage());
	}

	@Test
	void createEventShouldSaveAndReturnEventDto() {
		when(ownerService.getOwner(anyLong())).thenReturn(BaseMocks.getOwnerDTOMock());
		when(repository.save(any())).thenReturn(BaseMocks.getEventMock());

		var response = service.createEvent(BaseMocks.getEventDTOMock());

		Assertions.assertNotNull(response);
	}

	@Test
	void updateEventShouldSaveAndReturnEventDto() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getEventMock()));
		when(repository.save(any())).thenReturn(BaseMocks.getEventMock());

		var response = service.updateEvent(1L, BaseMocks.getEventDTOMock());

		Assertions.assertNotNull(response);
	}

	@ParameterizedTest
	@EnumSource(value = Status.class, names = {"DONE", "CANCELLED"})
	void updateStatusEventShouldSave(Status status) {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getEventMock()));

		service.updateStatusEvent(1L, status);

		verify(repository, times(1)).save(eventArgumentCaptor.capture());
		Assertions.assertEquals(status, eventArgumentCaptor.getValue().getStatus());
	}

	@ParameterizedTest
	@EnumSource(value = Status.class, names = {"DONE", "CANCELLED"})
	void updateStatusEventShouldNotSaveWhenPreviousStatusIsNotToDo(Status status) {
		var event = BaseMocks.getEventMock();
		event.setStatus(status);
		when(repository.findById(anyLong())).thenReturn(Optional.of(event));

		var response = service.updateStatusEvent(1L, status);

		verify(repository, never()).save(any());
		Assertions.assertNotNull(response);
	}

	@Test
	void deleteEventShouldDeleteAndReturnNothing() {
		service.deleteById(1L);

		verify(repository, times(1)).deleteById(anyLong());
	}
}
