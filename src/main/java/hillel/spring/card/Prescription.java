package hillel.spring.card;

import lombok.Data;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class Prescription {
    private String medicineName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer timesPerDay;
}