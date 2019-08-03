package hillel.spring.doctors;


import hillel.spring.doctors.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findBySpecializationInAndNameIgnoreCaseStartingWith(String strings, String s);

    List<Doctor> findBySpecializationIn(String strings);

    List<Doctor> findByNameIgnoreCaseStartingWith(String s);
}
