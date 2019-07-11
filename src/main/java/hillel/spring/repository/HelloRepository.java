package hillel.spring.repository;

import hillel.spring.database.Database;
import hillel.spring.database.model.HelloModel;
import org.springframework.beans.factory.annotation.Autowired;
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
