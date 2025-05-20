package service.calendar_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_OWNER")
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OWNER_ID")
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@CreationTimestamp
	@Column(name = "CREATE_AT", updatable = false, nullable = false)
	private Date createAt;

	@UpdateTimestamp
	@Column(name = "UPDATE_AT", nullable = false)
	private Date updateAt;
}
