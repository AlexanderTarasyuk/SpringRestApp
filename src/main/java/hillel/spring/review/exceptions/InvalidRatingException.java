package hillel.spring.review.exceptions;

public class InvalidRatingException extends RuntimeException {


    public InvalidRatingException(String s) {
        System.out.println(s);
    }
}
