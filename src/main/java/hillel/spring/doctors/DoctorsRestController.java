package hillel.spring.doctors;


import hillel.spring.doctors.dto.DoctorInputDto;
import hillel.spring.doctors.dto.DoctorsDtoMapper;
import hillel.spring.doctors.exceptions.DoctorIsNotFoundException;
import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class DoctorsRestController {

    private final DoctorRestService doctorRestService;
    @Autowired
    private final DoctorsDtoMapper dtoConverter;

    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    @GetMapping("/doctors")
    public List<Doctor> findAllDoctors(@RequestParam(value = "letter") Optional<String> letter,
                                       @RequestParam(value = "specialization") Optional<String> specialization) {

        return doctorRestService.findAllDoctors(letter, specialization);
    }


    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = doctorRestService.findDoctorByID(id);

        return mayBeDoctor.orElseThrow(DoctorIsNotFoundException::new);
    }


    @DeleteMapping("/doctors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {

        doctorRestService.findDoctorByID(id).orElseThrow(DoctorIsNotFoundException::new);

        doctorRestService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorInputDto doctorDto) {

        Doctor created = doctorRestService.createDoctor(dtoConverter.toModel(doctorDto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();

    }



    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto doctorInputDto, @PathVariable Integer id) {
        Doctor doctor = dtoConverter.toModel(doctorInputDto, id);
        doctorRestService.updateDoctor(doctor);
        return ResponseEntity.ok().build();
    }




    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchDoctor(DoctorIsNotFoundException ex){
    }


}
