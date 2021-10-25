package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.Company;

@Repository
@Transactional
public interface CompanyRepository extends JpaRepository<Company, Long> {

	boolean existsById(Long id);

	boolean existsByName(String name);
	
}
