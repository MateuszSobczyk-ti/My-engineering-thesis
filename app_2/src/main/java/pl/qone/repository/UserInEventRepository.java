package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.qone.model.Event;
import pl.qone.model.RoleInEventEnum;
import pl.qone.model.User;
import pl.qone.model.UserInEvent;

@Repository
@Transactional
public interface UserInEventRepository extends JpaRepository<UserInEvent, Long>{

	boolean existsByEventAndUser(Event event, User user);
	
	UserInEvent findByEventAndUser(Event event, User user);
	
	UserInEvent findByEventAndRoleInEvent(Event event, RoleInEventEnum roleInEvent);
	 
	 @Query("SELECT COUNT(u) FROM UserInEvent u WHERE u.event=:event")
	 int countContestantInEvent(@Param("event") Event event);
	
}
