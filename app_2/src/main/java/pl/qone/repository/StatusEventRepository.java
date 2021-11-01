package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.StatusEvent;

@Repository
@Transactional
public interface StatusEventRepository extends JpaRepository<StatusEvent, Long>{
	
	boolean existsByName(String name);

}
