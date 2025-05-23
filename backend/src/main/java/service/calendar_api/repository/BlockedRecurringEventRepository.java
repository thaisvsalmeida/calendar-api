package service.calendar_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.calendar_api.entity.BlockedRecurringEvent;
import service.calendar_api.entity.RecurringEvent;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BlockedRecurringEventRepository extends JpaRepository<BlockedRecurringEvent, Long> {
	boolean existsByRecurringEventAndDate(RecurringEvent recurringEvent, LocalDate date);

	List<BlockedRecurringEvent> findAllByRecurringEventAndDateBetween(RecurringEvent event, LocalDate start, LocalDate end);
}
