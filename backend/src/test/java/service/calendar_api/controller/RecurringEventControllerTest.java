package service.calendar_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.calendar_api.enums.Status;
import service.calendar_api.service.RecurringEventService;
import service.calendar_api.utils.BaseMocks;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecurringEventController.class)
class RecurringEventControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private RecurringEventService service;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void findAllRecurringEventsByOwnerShouldReturnEventDtoList() throws Exception {
		when(service.findAllRecurringEventsByOwner(anyLong()))
				.thenReturn(Collections.singletonList(BaseMocks.getRecurringEventDTOMock()));

		mockMvc.perform(get("/recurring-event")
						.param("ownerId", "1"))
				.andExpect(status().isOk());
	}

	@Test
	void findRecurringEventByIdShouldReturnRecurringEventDtoWhenExists() throws Exception {
		when(service.getRecurringEventById(anyLong())).thenReturn(BaseMocks.getRecurringEventDTOMock());

		mockMvc.perform(get("/recurring-event/{id}", 1L))
				.andExpect(status().isOk());
	}

	@Test
	void createRecurringEventShouldCreateAndReturnEventDto() throws Exception {
		var recurringEventDTOMock = BaseMocks.getRecurringEventDTOMock();
		String jsonBody = objectMapper.writeValueAsString(recurringEventDTOMock);

		when(service.createRecurringEvent(any())).thenReturn(recurringEventDTOMock);

		mockMvc.perform(post("/recurring-event")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(header().exists("Location"));
	}

	@Test
	void updateRecurringEventShouldUpdateAndReturnEventDto() throws Exception {
		var recurringEventDTOMock = BaseMocks.getRecurringEventDTOMock();
		recurringEventDTOMock.setTitle("Name test");
		String jsonBody = objectMapper.writeValueAsString(recurringEventDTOMock);

		when(service.updateRecurringEvent(anyLong(), any())).thenReturn(recurringEventDTOMock);

		mockMvc.perform(put("/recurring-event/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.title", equalTo(recurringEventDTOMock.getTitle())));
	}

	@Test
	void cancelRecurringEventShouldUpdateAndReturnEventDto() throws Exception {
		var recurringEventDTOMock = BaseMocks.getRecurringEventDTOMock();
		recurringEventDTOMock.setStatus(Status.CANCELLED);

		when(service.cancelRecurringEvent(anyLong())).thenReturn(recurringEventDTOMock);

		mockMvc.perform(put("/recurring-event/{id}/cancel", 1L))
				.andExpect(status().isAccepted());
	}

	@Test
	void deleteRecurringEventShouldDeleteAndReturnNoContent() throws Exception {
		mockMvc.perform(delete("/recurring-event/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(service, times(1)).deleteRecurringEventById(anyLong());
	}
}
