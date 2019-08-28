package hillel.spring.review.dto;

import hillel.spring.review.model.Comment;
import lombok.Data;

import java.util.List;

@Data
public class ReviewOutputDto {
    private Double averageServiceStars;
    private Double averageEquipmentStars;
    private Double averageQualificationStars;
    private Double averageTreatmentResultsStars;
    private Double averageGeneralStars;
    private List<Comment> comments;



}
