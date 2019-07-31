package hillel.spring.doctors;


import hillel.spring.doctors.dto.DoctorInputDto;
import hillel.spring.doctors.dto.DoctorsDtoMapper;
import hillel.spring.doctors.exceptions.DoctorIsNotFoundException;
import hillel.spring.doctors.exceptions.InvalidSpecializationException;
import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DoctorsRestController {

    private final DoctorService doctorRestService;
    @Autowired
    private final DoctorsDtoMapper dtoConverter = Mappers.getMapper(DoctorsDtoMapper.class);
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorInputDto doctorDto) {

        Doctor created = doctorRestService.createDoctor(dtoConverter.toModel(doctorDto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();

    }


    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = doctorRestService.findById(id);

        return mayBeDoctor.orElseThrow(DoctorIsNotFoundException::new);
    }


    @GetMapping("/doctors")
    public List<Doctor> findAllDoctors(
            @RequestParam(value = "letter") Optional<String> letter,
            @RequestParam(value = "specialization") Optional<String> specialization) {

        return doctorRestService.findAll(letter, specialization);
    }


    @DeleteMapping("doctors/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ResponseEntity deleteDoctor(@PathVariable Integer id) {
        doctorRestService.findById(id).orElseThrow(DoctorIsNotFoundException::new);
        doctorRestService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto doctorInputDto, @PathVariable Integer id) {
        Doctor doctor = dtoConverter.toModel(doctorInputDto, id);
        doctorRestService.update(doctor);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchDoctor(NoSuchDoctorException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidSpecialization(InvalidSpecializationException ex) {
    }


}
