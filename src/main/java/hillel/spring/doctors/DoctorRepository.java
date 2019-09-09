package hillel.spring.doctors;


import hillel.spring.doctors.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Page<Doctor> findBySpecializationInAndNameIgnoreCaseStartingWith(List<String> specialization,
                                                                     String name,
                                                                     Pageable pageable);

    Page<Doctor> findBySpecializationIn(List<String> strings, Pageable pageable);

    Page<Doctor> findByLetterIgnoreCaseStartingWith(String name, Pageable pageable);


}
