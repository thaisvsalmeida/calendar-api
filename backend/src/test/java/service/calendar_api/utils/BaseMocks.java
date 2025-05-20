package service.calendar_api.utils;

import service.calendar_api.dto.OwnerDTO;
import service.calendar_api.entity.Owner;

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
}
