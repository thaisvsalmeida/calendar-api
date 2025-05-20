package service.calendar_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.calendar_api.entity.Owner;
import service.calendar_api.enums.Label;
import service.calendar_api.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

	private Long id;

	@NotBlank(message = "Event title can not be null or blank")
	private String title;

	private String description;

	@NotNull(message = "Start date time can not be null")
	private LocalDateTime startDateTime;

	@NotNull(message = "End date time can not be null")
	private LocalDateTime endDateTime;

	private Label label;

	private Status status;

	@NotNull(message = "Owner ID can not be null")
	private Long ownerId;
}
