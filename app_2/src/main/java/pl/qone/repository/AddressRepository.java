package pl.qone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.qone.model.Address;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long>{

}
