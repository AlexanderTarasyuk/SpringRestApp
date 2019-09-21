package hillel.spring.doctors;


import hillel.spring.doctors.configuration.DoctorServiceConfig;
import hillel.spring.doctors.dto.DoctorInputDto;
import hillel.spring.doctors.dto.DoctorsDtoMapper;
import hillel.spring.doctors.exceptions.*;
import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Diplom;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.doctors.model.PetId;
import hillel.spring.pet.NoSuchPetException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class DoctorsRestController {

    private final DoctorService service;
    private final DoctorsDtoMapper dtoConverter = Mappers.getMapper(DoctorsDtoMapper.class);
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    private final RestTemplate restTemplate;

    private final DoctorServiceConfig doctorServiceConfig ;


    //get methods
    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = service.findById(id);

        return mayBeDoctor.orElseThrow(() -> new DoctorIsNotFoundException(id));
    }

    @GetMapping("/doctors")
    public Page<Doctor> findAllDoctors(
            @RequestParam(value = "letter") Optional<String> letter,
            @RequestParam(value = "specialization") Optional <List<String>> specialization,
            Pageable pageable) {

        return service.findAll(letter, specialization, pageable);
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
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody DoctorInputDto doctorInputDto, @PathVariable Integer id) {
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
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorInputDto doctorDto) {

        try {
            log.debug("Asking for diplom service");
            String urlDiplomService = doctorServiceConfig.getDiplomUrl() + "/diplom/" + doctorDto.getDiplomNumber();
            log.debug("Dimplom service URL: {}", urlDiplomService);
            Diplom diplom = restTemplate.getForObject(urlDiplomService, Diplom.class);
            val created = service.createDoctor(dtoConverter.toModel(doctorDto), diplom);
            return ResponseEntity.created(uriBuilder.build(created.getId())).build();
        } catch (Exception e) {
            log.error("Asking for  to diploma service finished with exception", e);
        }
        return null;

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
