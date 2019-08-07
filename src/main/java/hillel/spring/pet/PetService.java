package hillel.spring.pet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    public Optional<Pet> findById(Integer id) {
        return petRepository.findById(id);
    }

    public List<Pet> findAll(Optional<String> name, Optional<Integer> age) {
        if (name.isPresent() && age.isPresent()) {
            return petRepository.findByNameAndAge(name.get(), age.get());
        }
        if (name.isPresent()) {
            return petRepository.findByName(name.get());
        }
        if (age.isPresent()) {
            return petRepository.findByAge(age.get());
        }
        return petRepository.findAll();
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public void update(Pet pet) {
        petRepository.save(pet);
    }

    public void delete(Integer id) {
        petRepository.deleteById(id);
    }

}
