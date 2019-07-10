package hillel.spring.repository;

import hillel.spring.database.Database;
import org.springframework.stereotype.Repository;

@Repository
public class HelloRepository {

    private final Database database;

    public HelloRepository() {
        database = new Database();

    }

    public Database getDatabase() {
        return database;
    }
}
