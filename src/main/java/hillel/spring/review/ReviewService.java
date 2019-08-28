package hillel.spring.review;

import hillel.spring.card.MedicalCardRepository;
import hillel.spring.doctors.DoctorService;
import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.review.model.Comment;
import hillel.spring.review.dto.ReviewOutputDto;
import hillel.spring.review.exceptions.DoctorIdMismatchException;
import hillel.spring.review.exceptions.InvalidRatingException;
import hillel.spring.review.exceptions.NoSuchMedicalRecordException;
import hillel.spring.review.exceptions.NoSuchReviewException;
import hillel.spring.review.model.Review;
import hillel.spring.review.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final DoctorService doctorService;
    private final MedicalCardRepository medicalCardRepository;

    public Review save(Integer doctorId, Review toModel) {
        if (doctorId!=toModel.getDoctorId()){
            throw new DoctorIdMismatchException(doctorId);
        }
        if (doctorService.findById(doctorId).isEmpty()){
            throw new NoSuchDoctorException(doctorId);
        }
        if (medicalCardRepository.findByRecordsId(toModel.getMedicalRecordId()).isEmpty()) {
            throw new NoSuchMedicalRecordException();
        }
        if (!Utils.checkIsRatingValid(toModel)) {
            throw new InvalidRatingException(" bad rating in  " + toModel.toString());
        }

        return reviewRepository.save(toModel);
    }

    public ReviewOutputDto findReviews(Integer doctorId) {
        if (doctorService.findById(doctorId).isEmpty()){
            throw new NoSuchDoctorException(doctorId);
        }
        List<Review> reviews = reviewRepository.findByDoctorId(doctorId);
        ReviewOutputDto reviewOutputDto = new ReviewOutputDto();
       reviewOutputDto.setAverageEquipmentStars(reviews.stream().collect(Collectors.averagingDouble(review-> review.getEquipmentsStars().get())));
        reviewOutputDto.setAverageQualificationStars(reviews.stream().collect(Collectors.averagingDouble(review-> review.getQualificationsStars().get())));
        reviewOutputDto.setAverageServiceStars(reviews.stream().collect(Collectors.averagingDouble(review-> review.getServiceStars().get())));
        reviewOutputDto.setAverageTreatmentResultsStars(reviews.stream().collect(Collectors.averagingDouble(review-> review.getTreatmentStars().get())));
        reviewOutputDto.setAverageGeneralStars(reviews.stream().collect(Collectors.averagingDouble(review-> review.getGeneralStars().get())));
        reviewOutputDto.setComments(reviews
                .stream()
                .map(review->new Comment(review.getCreationTime(), review.getComments().orElse("there is no comment")))
                .collect(Collectors.toList()));
        return reviewOutputDto;
    }

    public void update(Integer doctorId, Review review) {
        if (reviewRepository.existsById(review.getId())){
            save(doctorId, review);
        } else {
            throw new NoSuchReviewException();
        }
    }
}

