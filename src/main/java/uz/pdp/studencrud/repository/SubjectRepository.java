package uz.pdp.studencrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.studencrud.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsByName(String name);

}
