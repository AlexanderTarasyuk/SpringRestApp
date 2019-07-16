package hillel.spring.doctors.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DoctorModel {

    private final Integer id;
    private final String name;
    private final String specialization;
}
