package hillel.spring.greeting.service;

import hillel.spring.greeting.Config;
import hillel.spring.greeting.database.Database;
import hillel.spring.greeting.repository.HelloRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Percentage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HelloService.class, HelloRepository.class, Database.class, Config.class})
public class HelloServiceTest {


    @Autowired
    private HelloService helloService;


    @Before
    public void setUp() throws Exception {
        assertThat(helloService).isNotNull();

    }

    @After
    public void tearDown() throws Exception {
        helloService = null;
        assertThat(helloService).isNull();


    }

    @Test
    public void getHello() {

        assertThat(helloService).isNotNull();

        assertThat(helloService.getHello("en")).isEqualToIgnoringCase("Hello");
        assertThat(helloService.getHello("fr")).isEqualToIgnoringCase("Bonjour");
        assertThat(helloService.getHello("it")).isEqualToIgnoringCase("Ciao");

        //Its terrible. I know
        assertThat(helloService.getHello("^(?!.*?(?:en|fr|it)).*$)"))
                .isEqualToIgnoringCase("No Such Language, Sorry");


    }

    @Test
    public void getRandomGreeting() {

        for (int i = 0; i < 100; i++) {
            assertThat(Arrays.asList(helloService.getRandomHello()))
                    .isSubsetOf(Arrays.asList("Hello", "Bonjour", "Ciao"));
        }

    }

    @Test
    public void getNoSuchLanguageMessage() {

        assertThat(helloService.getNoSuchLanguageMessage()).isEqualToIgnoringCase("No language is chosen");
    }

    @Test
    public void ensureThatGetRandomGreetingHasSmoothCoverage() {

        int numberOfCalls = 1_000_000;
        int valueToExpect = numberOfCalls / helloService.getLanguagesHello().toArray().length;

        Map<String, Long> greetingsCountMap = IntStream.rangeClosed(1, numberOfCalls)
                .mapToObj(i -> helloService.getRandomHello())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        for (Map.Entry<String, Long> entry : greetingsCountMap.entrySet()) {
            Assertions.assertThat(entry.getValue()).isCloseTo(valueToExpect, Percentage.withPercentage(2.0));
        }
    }
}