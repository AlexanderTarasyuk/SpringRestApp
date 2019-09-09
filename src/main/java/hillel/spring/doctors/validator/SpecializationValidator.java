package hillel.spring.doctors.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SpecializationValidator implements ConstraintValidator<ValidSpecialization, String> {
    private final List<String> specializations;

    public SpecializationValidator(@Value("${doctors.specializations}") String[] specializations) {
        this.specializations = List.of(specializations);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null
                && specializations.stream()
                .anyMatch(specialization -> specialization.equals(value));
    }
}