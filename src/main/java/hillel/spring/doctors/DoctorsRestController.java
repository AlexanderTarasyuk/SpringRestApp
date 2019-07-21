package hillel.spring.doctors;


import hillel.spring.doctors.exceptions.DoctorIsNotFoundException;
import hillel.spring.doctors.exceptions.IdIsNotEqualToUpdateDoctorException;
import hillel.spring.doctors.exceptions.IdIsPresentToCreateException;
import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DoctorsRestController {

    private final DoctorRestService doctorRestService;

    @GetMapping("/doctors")
    public List<Doctor> findAllDoctors() {

        return doctorRestService.findAllDoctors();
    }

    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = doctorRestService.findDoctorByID(id);

        return mayBeDoctor.orElseThrow(DoctorIsNotFoundException::new);
    }

    @GetMapping("/doctors/specialization/")
    public ResponseEntity<List<Doctor>> findDoctorBySpecialization(@RequestParam(value = "specialization", required = false) String specialization) {
        return ResponseEntity.ok(doctorRestService.findDoctorBySpecialization(specialization));
    }

    @GetMapping("/doctors/letter/")
    public ResponseEntity<List<Doctor>> findDoctorsByFirstLetter(@RequestParam(value = "letter") String letter) {
        return ResponseEntity.ok(doctorRestService.findDoctorsByFirstLetter(letter.strip().toLowerCase()));
    }

    @DeleteMapping("/doctors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {

        doctorRestService.findDoctorByID(id).orElseThrow(DoctorIsNotFoundException::new);

        doctorRestService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getId() != null) {
            throw new IdIsPresentToCreateException();
        }

        Integer id = doctorRestService.createDoctor(doctor);

        return ResponseEntity.status(HttpStatus.CREATED).body(doctorRestService.createDoctor(doctor));

    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Integer id,
                                          @RequestBody Doctor doctor) {
        if (!id.equals(doctor.getId())) {
            throw new IdIsNotEqualToUpdateDoctorException();
        }

        doctorRestService.findDoctorByID(id).orElseThrow(DoctorIsNotFoundException::new);

        doctorRestService.updateDoctor(id, doctor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
