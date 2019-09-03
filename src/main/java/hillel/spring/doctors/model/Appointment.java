package hillel.spring.doctors.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, Integer> hourToPetId = new HashMap<>();
}
