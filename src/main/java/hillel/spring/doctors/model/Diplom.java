package hillel.spring.doctors.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Diplom {
    private String universityName;
    private String specialization;
    private Integer dateOfGraduation;
}
