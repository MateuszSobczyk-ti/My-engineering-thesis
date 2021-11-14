package pl.qone.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.qone.model.Event;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Long>{
	
	boolean existsByName(String name);
	
	List<Event> findByNameContaining(String name);

	 
	 @Query("SELECT e FROM Event e ORDER BY e.data_start DESC")
	 List<Event> findAllOrdered();

}
