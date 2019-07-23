package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class DoctorRestService {
    private final DoctorRestRepository doctorRestAPIRepo;

    public List<Doctor> findAllDoctors(Predicate<Doctor> predicate) {
        return doctorRestAPIRepo.findAllDoctors()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRestAPIRepo.createDoctor(doctor);
    }

    public Optional<Doctor> findDoctorByID(Integer id) {
        return doctorRestAPIRepo.findDoctorByID(id);
    }

    public List<Doctor> findDoctorBySpecialization(String specialization) {
        return doctorRestAPIRepo.findDoctorBySpecialization(specialization);
    }

    public List<Doctor> findDoctorsByFirstLetter(String letter) {
        return doctorRestAPIRepo.findDoctorsByFirstLetter(letter);
    }

    public void updateDoctor(Doctor doctor) {
        doctorRestAPIRepo.updateDoctor( doctor);
    }

    public void deleteDoctor(Integer id) {
        doctorRestAPIRepo.deleteDoctor(id);
    }
}
