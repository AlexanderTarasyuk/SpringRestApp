package hillel.spring.greeting.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HelloModel {

    private final String languageName;
    private final String greeting;

}
