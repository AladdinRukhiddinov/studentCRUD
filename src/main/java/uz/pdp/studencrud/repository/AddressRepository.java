package uz.pdp.studencrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.studencrud.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
