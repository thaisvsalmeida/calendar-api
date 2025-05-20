package service.calendar_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.calendar_api.enums.Label;
import service.calendar_api.enums.Status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecurringEventDTO {
	private Long id;

	@NotBlank(message = "Recurring event title can not be null or blank")
	private String title;

	private String description;

	@NotNull(message = "Start time can not be null")
	private LocalTime startTime;

	@NotNull(message = "End time can not be null")
	private LocalTime endTime;

	@NotNull(message = "Start date can not be null")
	private LocalDate startDate;

	private LocalDate endDate; // can be null for infinite recurrence

	@NotEmpty(message = "Days of week can not be empty")
	private Set<@NotNull DayOfWeek> daysOfWeek;

	private Label label;

	private Status status;

	@NotNull(message = "Owner ID can not be null")
	private Long ownerId;
}
