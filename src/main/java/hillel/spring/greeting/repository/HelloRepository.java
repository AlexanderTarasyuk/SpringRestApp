package hillel.spring.greeting.repository;

import hillel.spring.greeting.database.Database;
import hillel.spring.greeting.database.model.HelloModel;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository

public class HelloRepository {

    private Database database;

    public HelloRepository(Database database) {
        this.database = database;
    }
    public void setDatabase(Database database) {
        this.database = database;
    }


    public Set<HelloModel> getLanguagesHello() {
        return database.getLanguagesHello();
    }

}
