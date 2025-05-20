package service.calendar_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.calendar_api.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
