package hillel.spring.doctors.exceptions;

public class InvalidSpecializationException extends RuntimeException {
    public InvalidSpecializationException(String s) {
        super(s + "  is an invalid specialization");
    }
}
