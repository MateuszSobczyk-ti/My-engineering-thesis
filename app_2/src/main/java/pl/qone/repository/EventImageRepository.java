package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.EventImage;

@Repository
@Transactional
public interface EventImageRepository extends JpaRepository<EventImage, String>{
	

}
