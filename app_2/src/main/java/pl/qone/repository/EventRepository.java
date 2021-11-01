package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.Event;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Long>{
	
	boolean existsByName(String name);

}
