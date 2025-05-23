package service.calendar_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_BLOCKED_RECURRING_EVENT")
public class BlockedRecurringEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "DATE", nullable = false)
	private LocalDate date;

	@ManyToOne
	@JoinColumn(name = "RECURRING_EVENT_ID", nullable = false)
	private RecurringEvent recurringEvent;

	@CreationTimestamp
	@Column(name = "CREATE_AT", updatable = false, nullable = false)
	private Date createAt;

	@UpdateTimestamp
	@Column(name = "UPDATE_AT", nullable = false)
	private Date updateAt;

	public BlockedRecurringEvent(LocalDate date, RecurringEvent recurringEvent) {
		this.date = date;
		this.recurringEvent = recurringEvent;
	}
}
