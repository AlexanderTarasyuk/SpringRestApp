package hillel.spring.pet.dto;


import hillel.spring.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface PetDtoConverter {

    @Mapping(target = "id", ignore = true)
    Pet toModel(PetInputDto dto);


    Pet toModel(PetInputDto dto, Integer id);
}
