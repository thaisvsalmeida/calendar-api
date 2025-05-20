package service.calendar_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import service.calendar_api.enums.Label;
import service.calendar_api.enums.Status;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
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

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "START_TIME", nullable = false)
	private LocalTime startTime;

	@Column(name = "END_TIME", nullable = false)
	private LocalTime endTime;

	@Column(name = "START_DATE", nullable = false)
	private LocalDate startDate;

	@Column(name = "END_DATE")
	private LocalDate endDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name = "TB_RECURRING_EVENT_DAYS",
			joinColumns = @JoinColumn(name = "RECURRING_EVENT_ID")
	)
	@Column(name = "DAYS_OF_WEEK", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<DayOfWeek> daysOfWeek;

	@Column(name = "LABEL")
	@Enumerated(EnumType.STRING)
	private Label label;

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID", nullable = false)
	private Owner owner;

	@CreationTimestamp
	@Column(name = "CREATE_AT", updatable = false, nullable = false)
	private Date createAt;

	@UpdateTimestamp
	@Column(name = "UPDATE_AT", nullable = false)
	private Date updateAt;
}
