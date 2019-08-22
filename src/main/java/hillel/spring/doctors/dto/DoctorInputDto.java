package hillel.spring.doctors.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorInputDto {

    private String name;
    private String [] specialization;
}
