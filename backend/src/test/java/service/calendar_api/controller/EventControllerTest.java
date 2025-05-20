package service.calendar_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.calendar_api.enums.Status;
import service.calendar_api.service.EventService;
import service.calendar_api.utils.BaseMocks;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private EventService service;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void findAllEventsByDateAndOwnerShouldReturnEventDtoList() throws Exception {
		when(service.findAllEventsByDateAndOwner(anyLong(), any(), any()))
				.thenReturn(Collections.singletonList(BaseMocks.getEventDTOMock()));

		mockMvc.perform(get("/event")
						.param("ownerId", "1")
						.param("startDateTime", "2025-05-21T00:00")
						.param("endDateTime", "2025-06-21T00:00"))
				.andExpect(status().isOk());
	}

	@Test
	void findEventByIdShouldReturnEventDtoWhenExists() throws Exception {
		when(service.getEventById(anyLong())).thenReturn(BaseMocks.getEventDTOMock());

		mockMvc.perform(get("/event/{id}", 1L))
				.andExpect(status().isOk());
	}

	@Test
	void createEventShouldCreateAndReturnEventDto() throws Exception {
		var eventDTO = BaseMocks.getEventDTOMock();
		String jsonBody = objectMapper.writeValueAsString(eventDTO);

		when(service.createEvent(any())).thenReturn(eventDTO);

		mockMvc.perform(post("/event")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(header().exists("Location"));
	}

	@Test
	void updateEventShouldUpdateAndReturnEventDto() throws Exception {
		var eventDTO = BaseMocks.getEventDTOMock();
		eventDTO.setTitle("Name test");
		String jsonBody = objectMapper.writeValueAsString(eventDTO);

		when(service.updateEvent(anyLong(), any())).thenReturn(eventDTO);

		mockMvc.perform(put("/event/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.title", equalTo(eventDTO.getTitle())));
	}

	@Test
	void completeEventShouldUpdateAndReturnEventDto() throws Exception {
		var eventDTO = BaseMocks.getEventDTOMock();
		eventDTO.setStatus(Status.DONE);

		when(service.updateStatusEvent(anyLong(), any())).thenReturn(eventDTO);

		mockMvc.perform(put("/event/{id}/complete", 1L))
				.andExpect(status().isAccepted());
	}

	@Test
	void cancelEventShouldUpdateAndReturnEventDto() throws Exception {
		var eventDTO = BaseMocks.getEventDTOMock();
		eventDTO.setStatus(Status.CANCELLED);

		when(service.updateStatusEvent(anyLong(), any())).thenReturn(eventDTO);

		mockMvc.perform(put("/event/{id}/cancel", 1L))
				.andExpect(status().isAccepted());
	}

	@Test
	void deleteEventShouldDeleteAndReturnNoContent() throws Exception {
		mockMvc.perform(delete("/event/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(service, times(1)).deleteById(anyLong());
	}
}
