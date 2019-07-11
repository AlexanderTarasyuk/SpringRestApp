package hillel.spring.service;

import hillel.spring.database.model.HelloModel;
import hillel.spring.repository.HelloRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

@Service
public class HelloService {

    private final HelloRepository helloRepository;
    private final Random random;


    public HelloService(HelloRepository helloRepository, Random random)
    {
        this.helloRepository = helloRepository;
        this.random=random;
    }


    public String getHello(String language) {
        return helloRepository.getLanguagesHello().stream()
                .filter(model -> model.getLanguageName().trim().equals(language))
                .findFirst()
                .map(HelloModel::getGreeting)
                .orElse("No Such Language, Sorry");
    }

    public String getRandomHello() {

        int index = random.nextInt(getLanguagesHello().toArray().length);
        HelloModel helloModel = (HelloModel) helloRepository.getLanguagesHello().toArray()[index];
//
        // Why not
//        List languages = List.of(helloRepository.getDatabase().getLanguagesHello().toArray());
//        HelloModel helloModel = (HelloModel) languages.get((int) (System.nanoTime() % languages.size()));

        return helloModel.getGreeting();
    }

    public String getNoSuchLanguageMessage() {
        return "No language is chosen";
    }

    public Set<HelloModel> getLanguagesHello() {
        return helloRepository.getLanguagesHello();
    }

}
