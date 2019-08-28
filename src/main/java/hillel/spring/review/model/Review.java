package hillel.spring.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;

    private Integer doctorId;
    private Integer medicalRecordId;

    private Integer serviceStars;
    private Integer equipmentsStars;
    private Integer qualificationsStars;
    private Integer treatmentStars;
    private Integer generalStars;
    private String comments;

    @CreationTimestamp
    private LocalDateTime creationTime;



    public Optional<Integer> getServiceStars() {
        return Optional.ofNullable(serviceStars);
    }

    public Optional<Integer> getEquipmentsStars() {
        return Optional.ofNullable(equipmentsStars);
    }

    public Optional<Integer> getQualificationsStars() {
        return Optional.ofNullable(qualificationsStars);
    }

    public Optional<Integer> getTreatmentStars() {
        return Optional.ofNullable(treatmentStars);
    }

    public Optional<Integer> getGeneralStars() {
        return Optional.ofNullable(generalStars);
    }

    public Optional<String> getComments() {
        return Optional.ofNullable(comments);
    }
}
