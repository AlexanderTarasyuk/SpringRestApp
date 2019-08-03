package hillel.spring.doctors.dto;

import hillel.spring.doctors.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DoctorsDtoMapper {


    @Mapping(target = "id", ignore = true)
    Doctor toModel(DoctorInputDto doctorInputDto);

    Doctor toModel(DoctorInputDto doctorInputDto, Integer id);

}



