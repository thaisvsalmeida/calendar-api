package service.calendar_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.calendar_api.utils.BaseMocks;
import service.calendar_api.service.OwnerService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private OwnerService service;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getOwnerShouldReturnOwnerDtoWhenExists() throws Exception {
		when(service.getOwner(anyLong())).thenReturn(BaseMocks.getOwnerDTOMock());

		mockMvc.perform(get("/owner/{id}", 1L))
				.andExpect(status().isOk());
	}

	@Test
	void createOwnerShouldCreateAndReturnOwnerDto() throws Exception {
		var owner = BaseMocks.getOwnerDTOMock();
		String jsonBody = objectMapper.writeValueAsString(owner);

		owner.setId(1L);
		when(service.create(any())).thenReturn(owner);

		mockMvc.perform(post("/owner")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(header().exists("Location"));
	}

	@Test
	void updateOwnerShouldUpdateAndReturnOwnerDto() throws Exception {
		var owner = BaseMocks.getOwnerDTOMock();
		owner.setId(1L);
		owner.setName("Name test");
		String jsonBody = objectMapper.writeValueAsString(owner);

		when(service.update(anyLong(), any())).thenReturn(owner);

		mockMvc.perform(put("/owner/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name", equalTo(owner.getName())));
	}

	@Test
	void deleteOwnerShouldDeleteAndReturnNoContent() throws Exception {
		mockMvc.perform(delete("/owner/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(service, times(1)).deleteById(anyLong());
	}
}
