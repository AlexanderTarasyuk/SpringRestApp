package hillel.spring.doctors;


import hillel.spring.doctors.dto.DoctorInputDto;
import hillel.spring.doctors.dto.DoctorsDtoMapper;
import hillel.spring.doctors.exceptions.*;
import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.pet.NoSuchPetException;
import hillel.spring.pet.Pet;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DoctorsRestController {

    private final DoctorService service;
    private final DoctorsDtoMapper dtoConverter = Mappers.getMapper(DoctorsDtoMapper.class);
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorInputDto doctorDto) {

        Doctor created = service.createDoctor(dtoConverter.toModel(doctorDto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();

    }


    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = service.findById(id);

        return mayBeDoctor.orElseThrow(DoctorIsNotFoundException::new);
    }


    @GetMapping("/doctors")
    public List<Doctor> findAllDoctors(
            @RequestParam(value = "letter") Optional<String> letter,
            @RequestParam(value = "specialization") Optional<String> specialization) {

        return service.findAll(letter, specialization);
    }


    @DeleteMapping("doctors/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ResponseEntity deleteDoctor(@PathVariable Integer id) {
        service.findById(id).orElseThrow(DoctorIsNotFoundException::new);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto doctorInputDto, @PathVariable Integer id) {
        Doctor doctor = dtoConverter.toModel(doctorInputDto, id);
        service.update(doctor);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/doctors/{doctorId}/schedule/{date}/{hour}")
    public ResponseEntity<?> schedulePetToDoctor(@PathVariable Integer doctorId,
                                                 @PathVariable
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                 @PathVariable Integer hour,
                                                 @RequestBody Pet pet) {
        service.schedulePetToDoctor(doctorId, date, hour, pet.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/doctors/{doctorId}/schedule/{date}")
    public Appointment findSchedule(@PathVariable Integer doctorId,
                                    @PathVariable
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findOrCreateSchedule(doctorId, date);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchDoctor(NoSuchDoctorException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidSpecialization(InvalidSpecializationException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void scheduleHourAlreadyBusy(ScheduleIsAlreadyBusy ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidHour(WrongHoursException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noSuchPet(NoSuchPetException ex) {
    }



}
