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

import java.time.LocalDateTime;
import java.util.Date;

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

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "START_DATE_TIME", nullable = false)
	private LocalDateTime startDateTime;

	@Column(name = "END_DATE_TIME", nullable = false)
	private LocalDateTime endDateTime;

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
