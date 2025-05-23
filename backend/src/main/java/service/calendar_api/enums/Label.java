package service.calendar_api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import service.calendar_api.exception.InvalidEnumArgumentException;

import java.util.Arrays;

public enum Label {
	STUDY,
	WORK,
	REST,
	OTHER;

	@JsonCreator
	public static Label fromString(String value) {
		try {
			return Label.valueOf(value.toUpperCase());
		} catch (Exception ex) {
			throw new InvalidEnumArgumentException(
					String.format("Invalid Label: %s. Choose between: %s",
							value, Arrays.toString(Label.values())));
		}
	}
}
