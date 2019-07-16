package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class DoctorRestService {
    private final DoctorRestRepository doctorRestAPIRepo;

    public List<Doctor> findAllDoctors() {
        return doctorRestAPIRepo.findAllDoctors();
    }

    public Integer createDoctor(Doctor doctor) {
        return doctorRestAPIRepo.createDoctor(doctor);
    }

    public Optional<Doctor> findDoctorByID(Integer id) {
        return doctorRestAPIRepo.findDoctorByID(id);
    }

    public List<Doctor> findDoctorBySpecialization(String specialization) {
        return doctorRestAPIRepo.findDoctorBySpecialization(specialization);
    }

    public List<Doctor> findDoctorsByFirstLetter(String name) {
        return doctorRestAPIRepo.findDoctorsByFirstLetter(name);
    }

    public void updateDoctor(Integer id, Doctor doctor) {
        doctorRestAPIRepo.updateDoctor(id, doctor);
    }

    public void deleteDoctor(Integer id) {
        doctorRestAPIRepo.deleteDoctor(id);
    }
}
