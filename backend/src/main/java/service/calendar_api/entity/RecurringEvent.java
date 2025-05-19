package service.calendar_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.calendar_api.enums.Label;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_RECURRING_EVENT")
public class RecurringEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECURRING_EVENT_ID")
	private Long id;

	private String title;

	private String description;

	@Column(name = "START_TIME")
	private LocalTime startTime;

	@Column(name = "END_TIME")
	private LocalTime endTime;

	@Column(name = "START_DATE")
	private LocalDate startDate;

	@Column(name = "END_DATE")
	private LocalDate endDate; // can be null for infinite recurrence

	@Column(name = "DAYS_OF_WEEK")
	@Enumerated(EnumType.STRING)
	private Set<DayOfWeek> daysOfWeek;

	@Enumerated(EnumType.STRING)
	private Label label;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OWNER_ID", nullable = false)
	private Owner owner;
}
