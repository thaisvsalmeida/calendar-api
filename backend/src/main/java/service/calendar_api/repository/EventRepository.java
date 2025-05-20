package service.calendar_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.calendar_api.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findAllByOwnerIdAndStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(Long ownerId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
