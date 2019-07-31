package hillel.spring;

import hillel.spring.doctors.fakeDatabase.DatabaseDoctor;
import hillel.spring.greeting.database.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Bean
    public AtomicInteger getAtomicInteger() {
        return new AtomicInteger();
    }

    @Bean
    public DatabaseDoctor getDatabaseDoctor() {
        return new DatabaseDoctor();
    }

}
