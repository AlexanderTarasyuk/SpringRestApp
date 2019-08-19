package hillel.spring.pet.dto;


import hillel.spring.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper
public interface PetDtoConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Pet toModel(PetInputDto dto);

    @Mapping(target = "version", ignore = true)
    Pet toModel(PetInputDto dto, Integer id);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void update(@MappingTarget Pet pet, PetInputDto dto);
}
