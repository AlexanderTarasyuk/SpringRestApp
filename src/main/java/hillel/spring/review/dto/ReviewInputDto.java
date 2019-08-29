package hillel.spring.review.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewInputDto {
    private Integer doctorId;
    private Integer medicalRecordId;
    private Integer serviceStars;
    private Integer equipmentsStars;
    private Integer qualificationsStars;
    private Integer treatmentsStars;
    private Integer generalStars;
    private String comments;

    public Optional<Integer> getServiceStars() {
        return Optional.ofNullable(serviceStars);
    }

    public Optional<Integer> getEquipmentsStars() {
        return Optional.ofNullable(equipmentsStars);
    }

    public Optional<Integer> getQualificationsStars() {
        return Optional.ofNullable(qualificationsStars);
    }

    public Optional<Integer> getTreatmentsStars() {
        return Optional.ofNullable(treatmentsStars);
    }

    public Optional<Integer> getGeneralStars() {
        return Optional.ofNullable(generalStars);
    }

    public Optional<String> getComments() {
        return Optional.ofNullable(comments);
    }

}
