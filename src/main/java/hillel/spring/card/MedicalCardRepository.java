package hillel.spring.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalCardRepository extends JpaRepository<MedicalCard, Integer> {


    Optional<MedicalCard> findByRecordsId(Integer medicalRecordId);
}