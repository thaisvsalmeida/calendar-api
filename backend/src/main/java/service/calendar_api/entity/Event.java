package service.calendar_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.calendar_api.enums.Label;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_EVENT")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID")
	private Long id;

	private String title;

	private String description;

	@Column(name = "START_DATE_TIME")
	private LocalDateTime startDateTime;

	@Column(name = "END_DATE_TIME")
	private LocalDateTime endDateTime;

	@Enumerated(EnumType.STRING)
	private Label label;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OWNER_ID", nullable = false)
	private Owner owner;
}
