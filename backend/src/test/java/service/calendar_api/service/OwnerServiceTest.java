package service.calendar_api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.calendar_api.exception.ResourceNotFoundException;
import service.calendar_api.repository.OwnerRepository;
import service.calendar_api.utils.BaseMocks;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
	@InjectMocks
	private OwnerService service;
	@Mock
	private OwnerRepository repository;

	@Test
	void getOwnerShouldReturnOwnerDtoWhenExists() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getOwnerMock()));

		var response = service.getOwner(1L);

		Assertions.assertNotNull(response);
	}

	@Test
	void getOwnerShouldThrowsResourceNotFoundExceptionWhenNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		var ex = Assertions.assertThrows(ResourceNotFoundException.class,
				() -> service.getOwner(1L));

		Assertions.assertNotNull(ex);
		Assertions.assertEquals("Owner not found", ex.getMessage());
	}

	@Test
	void createOwnerShouldSaveAndReturnOwnerDto() {
		when(repository.save(any())).thenReturn(BaseMocks.getOwnerMock());

		var response = service.create(BaseMocks.getOwnerDTOMock());

		Assertions.assertNotNull(response);
	}

	@Test
	void updateOwnerShouldSaveAndReturnOwnerDto() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(BaseMocks.getOwnerMock()));
		when(repository.save(any())).thenReturn(BaseMocks.getOwnerMock());

		var response = service.update(1L, BaseMocks.getOwnerDTOMock());

		Assertions.assertNotNull(response);
	}

	@Test
	void deleteOwnerShouldDeleteAndReturnNothing() {
		service.deleteById(1L);

		verify(repository, times(1)).deleteById(anyLong());
	}
}
