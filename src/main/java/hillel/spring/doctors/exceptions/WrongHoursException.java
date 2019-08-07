package hillel.spring.doctors.exceptions;

public class WrongHoursException extends RuntimeException {
    public WrongHoursException(Integer hour) {
        super("Wrong hour " + hour);
    }
}
