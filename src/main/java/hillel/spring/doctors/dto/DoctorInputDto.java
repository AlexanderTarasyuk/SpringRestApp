package hillel.spring.doctors.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoctorInputDto {

    private String name;
    private final List<String> specialization;
}
