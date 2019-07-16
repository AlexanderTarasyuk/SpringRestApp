package hillel.spring.greeting.controller;

import hillel.spring.greeting.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }


    @GetMapping("/greeting/{lang}")
    public String getHello(@PathVariable("lang") String lang) {
        return helloService.getHello(lang);
    }

    @GetMapping("/greeting/")
    public String getEmptyHello() {
        return helloService.getNoSuchLanguageMessage();

    }

    @GetMapping("/greeting/random")
    public String getRandomHello() {
        return helloService.getRandomHello();
    }
}
