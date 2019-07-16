package hillel.spring.doctors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "id must be not present")

public class IdIsPresentToCreateException extends RuntimeException {
}
