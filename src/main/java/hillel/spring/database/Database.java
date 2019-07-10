package hillel.spring.database;

import hillel.spring.database.model.HelloModel;

import java.util.HashSet;
import java.util.Set;

public class Database {

    //maybe Map is better???
    private final Set<HelloModel> languagesHello;

    public Database() {
        languagesHello = new HashSet<>();
        HelloModel helloModelEngl = new HelloModel("en", "Hello");
        HelloModel helloModelFrance = new HelloModel("fr", "Bonjour");
        HelloModel helloModelItaly = new HelloModel("it", "Ciao");
        HelloModel helloModelUkr = new HelloModel("ua", "Здоровеньки були");

        languagesHello.add(helloModelEngl);
        languagesHello.add(helloModelFrance);
        languagesHello.add(helloModelItaly);
        languagesHello.add(helloModelUkr);
    }

    public Set<HelloModel> getLanguagesHello() {
        return languagesHello;
    }
}
