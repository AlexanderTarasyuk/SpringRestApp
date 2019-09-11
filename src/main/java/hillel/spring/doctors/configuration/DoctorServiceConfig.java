package hillel.spring.doctors.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("pet-clinic")
@Component
@Validated
public class DoctorServiceConfig {

    @NotBlank
    private String doctorUrl;

    @NotBlank
    private String diplomUrl;
}
