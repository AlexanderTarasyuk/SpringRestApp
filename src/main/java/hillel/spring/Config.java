package hillel.spring;

import hillel.spring.database.Database;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class Config {

    @Bean
    public Database getDatabase() {
        return new Database();
    }

    @Bean
    public Random getRandom() {
        return new Random();
    }
}
