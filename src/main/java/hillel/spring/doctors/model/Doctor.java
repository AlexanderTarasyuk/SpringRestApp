package hillel.spring.doctors.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer versionId;
    private String name;
    private String numberOfDiplom;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> specialization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<LocalDate, Appointment> scheduleToDate;

    @Embedded
    private Diplom diplom;


    public Doctor(Integer id, String name, String...specialization) {
        this.id = id;
        this.name = name;
        this.specialization =  Arrays.asList(specialization);
    }



}