package hillel.spring.database;

import hillel.spring.database.model.HelloModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Database {

    //maybe Map is better???
    private final Set<HelloModel> languagesHello;

    public Database() {
        languagesHello = new HashSet<>();
        HelloModel helloModelEngl = new HelloModel("en", "Hello");
        HelloModel helloModelFrance = new HelloModel("fr", "Bonjour");
        HelloModel helloModelItaly = new HelloModel("it", "Ciao");

        languagesHello.add(helloModelEngl);
        languagesHello.add(helloModelFrance);
        languagesHello.add(helloModelItaly);
    }

    public Set<HelloModel> getLanguagesHello() {
        return languagesHello;
    }

}
