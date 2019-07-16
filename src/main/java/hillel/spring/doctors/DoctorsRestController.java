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

//        return "hello";


        return doctorRestService.findAllDoctors();
    }

    @GetMapping("/doctors/{id}")
    public Doctor findDoctorByID(@PathVariable Integer id) {
        Optional<Doctor> mayBeDoctor = doctorRestService.findDoctorByID(id);

        return mayBeDoctor.orElseThrow(DoctorIsNotFoundException::new);
    }

    @GetMapping("/doctors/specialization/")
    public List<Doctor> findDoctorBySpecialization(@RequestParam String specialization) {
        return doctorRestService.findDoctorBySpecialization(specialization);
    }

    @GetMapping("/doctors/letter/")
    public List<Doctor> findDoctorsByFirstLetter(@RequestParam("letter") String letter) {
        return doctorRestService.findDoctorsByFirstLetter(letter.strip().toLowerCase());
    }

    @DeleteMapping("/doctors/{id}")
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

        try {
            Integer id = doctorRestService.createDoctor(doctor);

            return new ResponseEntity<>(new Doctor(id, doctor.getName(), doctor.getSpecialization()), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(" error");
        }
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
