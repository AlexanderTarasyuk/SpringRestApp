package hillel.spring.doctors.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,  ElementType.TYPE_USE})
@Constraint(validatedBy = SpecializationValidator.class)
public @interface ValidSpecialization {
    String message() default "Wrong specialization";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}