package hillel.spring.card;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MedicalCardController {

    private final MedicalCardRepository repository;

    @GetMapping("/medical-cards")
    public List<MedicalCard> getAll(){
        return repository.findAll();
    }


    @PostMapping("/medical-cards")
    public void create(@RequestBody MedicalCard medicalCard){
        repository.save(medicalCard);
    }
}