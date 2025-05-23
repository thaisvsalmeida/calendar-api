package service.calendar_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.calendar_api.entity.RecurringEvent;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecurringEventRepository extends JpaRepository<RecurringEvent, Long> {
	List<RecurringEvent> findAllByOwnerId(Long ownerId);

	List<RecurringEvent> findAllByOwnerIdAndStartDateBetween(Long ownerId, LocalDate start, LocalDate end);
}
