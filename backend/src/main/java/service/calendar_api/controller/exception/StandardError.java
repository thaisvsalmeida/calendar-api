package service.calendar_api.controller.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardError implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String timestamp;
	private Integer status;
	private String error;
	private String message;
	private List<String> messages;
	private String path;

	public StandardError(String timestamp, Integer status, String error, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public StandardError(String timestamp, Integer status, String error, List<String> messages, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.messages = messages;
		this.path = path;
	}
}
