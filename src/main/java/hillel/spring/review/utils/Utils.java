package hillel.spring.review.utils;

import hillel.spring.review.exceptions.InvalidRatingException;
import hillel.spring.review.model.Review;

import java.util.Optional;

public class Utils {
    public static boolean checkIsRatingValid(Review toModel) {

        return isRatingValid(toModel.getServiceStars())
                && isRatingValid(toModel.getEquipmentsStars())
                && isRatingValid(toModel.getQualificationsStars())
                && isRatingValid(toModel.getTreatmentStars())
                && isRatingValid(toModel.getGeneralStars());
    }

    private static boolean isRatingValid(Optional<Integer> rating) {

        return rating.filter(integer -> (integer >= 1) && (integer <= 5)).isPresent();
    }
}

