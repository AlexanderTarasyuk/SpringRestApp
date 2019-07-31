package hillel.spring.doctors.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity

public class Doctor {
    @Id
    @GeneratedValue
    private Integer id = 0;
    private String name;
    private String specialization;


}