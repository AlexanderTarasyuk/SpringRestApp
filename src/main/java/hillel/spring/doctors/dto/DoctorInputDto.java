package hillel.spring.doctors.dto;

import hillel.spring.doctors.validator.ValidSpecialization;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorInputDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private List <@ValidSpecialization String> specialization;
    @NotEmpty
    private final String diplomNumber;
}
