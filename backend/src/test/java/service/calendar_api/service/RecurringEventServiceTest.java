package service.calendar_api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.calendar_api.entity.RecurringEvent;
import service.calendar_api.enums.Status;
import service.calendar_api.exception.DuplicatedBlockException;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.repository.BlockedRecurringEventRepository;
import service.calendar_api.repository.RecurringEventRepository;
import service.calendar_api.utils.BaseMocks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecurringEventServiceTest {
	@InjectMocks
	private RecurringEventService service;
	@Mock
	private RecurringEventRepository repository;
	@Mock
	private OwnerService ownerService;
	@Mock
	private BlockedRecurringEventRepository blockedRecurringEventRepository;
	@Captor
	private ArgumentCaptor<RecurringEvent> eventArgumentCaptor;

	@Test
	void findAllRecurringEventsByOwnerShouldReturnRecurringEventDTOList() {
		when(repository.findAllByOwnerId(anyLong()))
				.thenReturn(Collections.singletonList(BaseMocks.getRecurringEventMock()));

		var response = service.findAllRecurringEventsByOwner(1L);

		Assertions.assertNotNull(response);
		Assertions.assertEquals(1, response.size());
	}

	@Test
	void getRecurringEventByIdShouldReturnRecurringEventDTOWhenExists() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getRecurringEventMock()));

		var response = service.getRecurringEventById(1L);

		Assertions.assertNotNull(response);
	}

	@Test
	void getRecurringEventByIdShouldThrowsResourceNotFoundExceptionWhenNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		var ex = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> service.getRecurringEventById(1L));

		Assertions.assertNotNull(ex);
		Assertions.assertEquals("Recurring event not found", ex.getMessage());
	}

	@Test
	void createRecurringEventShouldSaveAndReturnRecurringEventDTO() {
		when(ownerService.getOwner(anyLong())).thenReturn(BaseMocks.getOwnerDTOMock());
		when(repository.save(any())).thenReturn(BaseMocks.getRecurringEventMock());

		var response = service.createRecurringEvent(BaseMocks.getRecurringEventDTOMock());

		Assertions.assertNotNull(response);
	}

	@Test
	void updateRecurringEventShouldSaveAndReturnRecurringEventDTO() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getRecurringEventMock()));
		when(repository.save(any())).thenReturn(BaseMocks.getRecurringEventMock());

		var response = service.updateRecurringEvent(1L, BaseMocks.getRecurringEventDTOMock());

		Assertions.assertNotNull(response);
	}

	@Test
	void cancelRecurringEventShouldSaveAndReturnRecurringEventDTO() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getRecurringEventMock()));

		service.cancelRecurringEvent(1L);

		verify(repository, times(1)).save(eventArgumentCaptor.capture());
		Assertions.assertEquals(Status.CANCELLED, eventArgumentCaptor.getValue().getStatus());
	}

	@Test
	void cancelRecurringEventShouldNotSaveWhenPreviousStatusIsNotToDo() {
		var event = BaseMocks.getRecurringEventMock();
		event.setStatus(Status.CANCELLED);
		when(repository.findById(anyLong())).thenReturn(Optional.of(event));

		var response = service.cancelRecurringEvent(1L);

		verify(repository, never()).save(any());
		Assertions.assertNotNull(response);
	}

	@Test
	void deleteRecurringEventByIdShouldDeleteAndReturnNothing() {
		service.deleteRecurringEventById(1L);

		verify(repository, times(1)).deleteById(anyLong());
	}

	@Test
	void addBlocksToRecurringEventsShouldSaveBlock() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getRecurringEventMock()));
		when(blockedRecurringEventRepository.existsByRecurringEventAndDate(any(), any())).thenReturn(false);

		service.addBlocksToRecurringEvents(1L, LocalDate.now());

		verify(blockedRecurringEventRepository, times(1)).save(any());
	}

	@Test
	void addBlocksToRecurringEventsShouldThrowsDuplicatedBlockExceptionWhenAlreadyExists() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getRecurringEventMock()));
		when(blockedRecurringEventRepository.existsByRecurringEventAndDate(any(), any())).thenReturn(true);

		var ex = Assertions.assertThrows(DuplicatedBlockException.class,
				() -> service.addBlocksToRecurringEvents(1L, LocalDate.now()));

		verify(blockedRecurringEventRepository, never()).save(any());
		Assertions.assertEquals("Block already exists for this date", ex.getMessage());
	}

	@Test
	void getEventsFromRecurringEventsShouldReturnEventListWhenEventOccursDuringDay() {
		var recurringEvent = BaseMocks.getRecurringEventMock();
		recurringEvent.setStartTime(LocalTime.of(8, 0));
		recurringEvent.setEndTime(LocalTime.of(12, 0));

		when(repository.findAllByOwnerIdAndStartDateBetween(anyLong(), any(), any()))
				.thenReturn(Collections.singletonList(recurringEvent));
		when(blockedRecurringEventRepository.findAllByRecurringEventAndDateBetween(any(), any(), any()))
				.thenReturn(Collections.singletonList(BaseMocks.getBlockedRecurringEventMock()));

		var response = service.getEventsFromRecurringEvents(1L, LocalDate.now(), LocalDate.now().plusDays(7));

		Assertions.assertNotNull(response);
		Assertions.assertEquals(7, response.size());
		Assertions.assertEquals(Collections.emptyList(),
				response.stream().filter(r -> r.getStartDateTime().equals(LocalDateTime.now())).toList());
	}

	@Test
	void getEventsFromRecurringEventsShouldReturnEventListWhenEventCrossesMidnight() {
		var recurringEvent = BaseMocks.getRecurringEventMock();
		recurringEvent.setStartTime(LocalTime.of(23, 0));
		recurringEvent.setEndTime(LocalTime.of(7, 0));

		when(repository.findAllByOwnerIdAndStartDateBetween(anyLong(), any(), any()))
				.thenReturn(Collections.singletonList(recurringEvent));
		when(blockedRecurringEventRepository.findAllByRecurringEventAndDateBetween(any(), any(), any()))
				.thenReturn(Collections.singletonList(BaseMocks.getBlockedRecurringEventMock()));

		var response = service.getEventsFromRecurringEvents(1L, LocalDate.now(), LocalDate.now().plusDays(7));

		Assertions.assertNotNull(response);
		Assertions.assertEquals(7, response.size());
		Assertions.assertEquals(Collections.emptyList(),
				response.stream().filter(r -> r.getStartDateTime().equals(LocalDateTime.now())).toList());
	}
}
