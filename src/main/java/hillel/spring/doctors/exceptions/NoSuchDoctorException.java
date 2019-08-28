package hillel.spring.doctors.exceptions;

public class NoSuchDoctorException extends RuntimeException {

    public NoSuchDoctorException() {

        super("No such doctor");
    }

    public NoSuchDoctorException(Integer fromDoctorId) {

        super("No such doctor" +  fromDoctorId);
    }
}
