package service.calendar_api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import service.calendar_api.exception.InvalidEnumArgumentException;

import java.util.Arrays;

public enum Status {
	TO_DO,
	DONE,
	CANCELLED;

	@JsonCreator
	public static Status fromString(String value) {
		try {
			return Status.valueOf(value.toUpperCase());
		} catch (Exception ex) {
			throw new InvalidEnumArgumentException(
					String.format("Invalid Status: %s. Choose between: %s",
							value, Arrays.toString(Status.values())));
		}
	}
}
