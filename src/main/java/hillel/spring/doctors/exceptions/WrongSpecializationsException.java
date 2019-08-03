package hillel.spring.doctors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong specialization. Not found")
public class WrongSpecializationsException extends RuntimeException{
}
