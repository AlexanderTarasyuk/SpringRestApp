package hillel.spring.card;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class MedicalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer petId;
    private LocalDateTime createdDate;
    @OneToOne(cascade = CascadeType.ALL)
    private SpecialNote specialNote;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MedicalRecord> records;
}