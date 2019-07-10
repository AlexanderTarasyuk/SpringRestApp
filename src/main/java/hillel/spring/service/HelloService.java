package hillel.spring.service;

import hillel.spring.database.model.HelloModel;
import hillel.spring.repository.HelloRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class HelloService {

    public HelloRepository getHelloRepository() {
        return helloRepository;
    }

    private final HelloRepository helloRepository;
    private final Random random = new Random();

    public HelloService(HelloRepository helloRepository) {

        this.helloRepository = helloRepository;
    }

    public String getHello(String language) {
        return helloRepository.getDatabase().getLanguagesHello().stream()
                .filter(model -> model.getLanguageName().trim().equals(language))
                .findFirst()
                .map(HelloModel::getGreeting)
                .orElse("No Such Language, Sorry");
    }

    public String getRandomHello() {

       int index = random.nextInt(helloRepository.getDatabase().getLanguagesHello().toArray().length);
       HelloModel helloModel = (HelloModel) helloRepository.getDatabase().getLanguagesHello().toArray()[index];
//
        // Why not
//        List languages = List.of(helloRepository.getDatabase().getLanguagesHello().toArray());
//        HelloModel helloModel = (HelloModel) languages.get((int) (System.nanoTime() % languages.size()));

        return helloModel.getGreeting();
    }

    public String getNoSuchLanguageMessage() {
        return "No language is chosen";
    }
}
