package hillel.spring.doctors;


import hillel.spring.doctors.dto.DoctorInputDto;
import hillel.spring.doctors.dto.DoctorsDtoMapper;
import hillel.spring.doctors.exceptions.*;
import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.doctors.model.PetId;
import hillel.spring.pet.NoSuchPetException;
import lombok.AllArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
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


    //get methods
    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = service.findById(id);

        return mayBeDoctor.orElseThrow(() -> new DoctorIsNotFoundException(id));
    }

    @GetMapping("/doctors")
    public List<Doctor> findAllDoctors(
            @RequestParam(value = "letter") Optional<String> letter,
            @RequestParam(value = "specialization") Optional<String> specialization) {

        return service.findAll(letter, specialization);
    }

    @GetMapping("/doctors/{doctorId}/schedule/{date}")
    public Appointment findSchedule(@PathVariable Integer doctorId,
                                    @PathVariable
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findOrCreateSchedule(doctorId, date);
    }


    //delete methods
    @DeleteMapping("doctors/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ResponseEntity deleteDoctor(@PathVariable Integer id) {
        service.findById(id).orElseThrow(() -> new DoctorIsNotFoundException(id));
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //put methods
    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto doctorInputDto, @PathVariable Integer id) {
        Doctor doctor = dtoConverter.toModel(doctorInputDto, id);
        service.update(doctor);
        return ResponseEntity.noContent().build();
    }

    //post methods
    @PostMapping("/doctors/{doctorId}/schedule/{date}/{hour}")

    public ResponseEntity<?> schedulePetToDoctor(@PathVariable Integer doctorId,
                                                 @PathVariable
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                 @PathVariable Integer hour,
                                                 @RequestBody PetId id) {
        service.schedulePetToDoctor(doctorId, date, hour, id.getPetId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorInputDto doctorDto) {

        Doctor created = service.createDoctor(dtoConverter.toModel(doctorDto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();

    }

    @PostMapping("/doctors/move-schedule/{date}/{fromDoctorId}/{toDoctorId}")
    public ResponseEntity<?> moveSchedule(@PathVariable
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                          @PathVariable Integer fromDoctorId,
                                          @PathVariable Integer toDoctorId) {
        service.moveAppointment(date, fromDoctorId, toDoctorId);
        return ResponseEntity.ok().build();
    }


    //exception handlers
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
