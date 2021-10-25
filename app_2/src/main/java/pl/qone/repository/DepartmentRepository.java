package pl.qone.repository;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.Department;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	boolean existsByName(String name);


}
