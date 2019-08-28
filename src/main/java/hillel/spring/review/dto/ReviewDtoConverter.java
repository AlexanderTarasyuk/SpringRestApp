package hillel.spring.review.dto;


import hillel.spring.review.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper
public interface ReviewDtoConverter {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    Review toModel(ReviewInputDto dto);


    Review toModel(ReviewInputDto dto, Integer id);

    default <T> T unpack(Optional<T> maybe){
        return maybe.orElse(null);
    }
}
