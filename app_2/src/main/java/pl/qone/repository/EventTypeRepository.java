package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.EventType;

@Repository
@Transactional
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

	boolean existsByName(String name);
	
}
 