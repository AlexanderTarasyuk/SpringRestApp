package hillel.spring.doctors.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

//@Data
//@Configuration
//@ConfigurationProperties("doctors.specializations")
public class SpecializationsConfig {
    private Set<String> specializations;
}