package service.calendar_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO {
	private Long id;

	@NotBlank(message = "Name can not be null or blank")
	private String name;

	@NotBlank(message = "Email can not be null or blank")
	private String email;
}
