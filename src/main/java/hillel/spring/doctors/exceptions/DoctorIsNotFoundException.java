package hillel.spring.doctors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Such a doctor is mot found")
public class DoctorIsNotFoundException extends RuntimeException {


}
