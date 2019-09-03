package hillel.spring.review.exceptions;

public class NoSuchReviewException extends RuntimeException {
    public NoSuchReviewException() {
        super(" no such review");
    }

}

