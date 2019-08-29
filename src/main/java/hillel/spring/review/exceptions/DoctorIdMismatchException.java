package hillel.spring.review.exceptions;


public class DoctorIdMismatchException extends RuntimeException{
    public DoctorIdMismatchException(Integer doctorId) {
        System.out.println("Doctors id " + doctorId +" mismatch");
    }
}

