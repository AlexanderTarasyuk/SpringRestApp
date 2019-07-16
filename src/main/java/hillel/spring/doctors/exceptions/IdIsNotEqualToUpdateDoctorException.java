package hillel.spring.doctors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "id should not be changed")

public class IdIsNotEqualToUpdateDoctorException extends RuntimeException {
}
