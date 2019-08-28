package hillel.spring.review;

import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.review.dto.ReviewDtoConverter;
import hillel.spring.review.dto.ReviewInputDto;
import hillel.spring.review.dto.ReviewOutputDto;
import hillel.spring.review.exceptions.InvalidRatingException;
import hillel.spring.review.exceptions.NoSuchMedicalRecordException;
import hillel.spring.review.exceptions.NoSuchReviewException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.hibernate.StaleObjectStateException;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
public class ReviewController {
    private final ReviewDtoConverter dtoConverter = Mappers.getMapper(ReviewDtoConverter.class);

    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{doctorId}/reviews/{reviewId}");

    private final ReviewService reviewService;

    @PostMapping("/doctors/{doctorId}/reviews")
    public ResponseEntity<?> createReview(@PathVariable Integer doctorId, @RequestBody ReviewInputDto dto){
        val created = reviewService.save(doctorId, dtoConverter.toModel(dto));
        return ResponseEntity.created(uriBuilder.build(created.getDoctorId(), created.getId())).build();
    }


    @GetMapping("/doctors/{doctorId}/reviews")
    public ReviewOutputDto findReviewsTotalByDoctorId(@PathVariable Integer doctorId){
        return reviewService.findReviews(doctorId);
    }


    @PutMapping("/doctors/{doctorId}/reviews/{id}")
    @Retryable(StaleObjectStateException.class)
    public ResponseEntity<?> updateReview(@RequestBody ReviewInputDto dto,
                                          @PathVariable Integer doctorId,
                                          @PathVariable Integer id){
        val review = dtoConverter.toModel(dto, id);
        reviewService.update(doctorId, review);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchDoctor(NoSuchDoctorException ex){
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchReview(NoSuchReviewException ex){
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchMedicalRecord(NoSuchMedicalRecordException ex){
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidRating(InvalidRatingException ex){
    }

}
