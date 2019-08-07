package hillel.spring.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    @Query("select pet from Pet as pet where pet.name=:name and pet.age=:age")
    List<Pet> findByNameAndAge(@Param("name") String name, @Param("age") Integer age);

    List<Pet> findByName(String name);

    List<Pet> findByAge(Integer age);

}

