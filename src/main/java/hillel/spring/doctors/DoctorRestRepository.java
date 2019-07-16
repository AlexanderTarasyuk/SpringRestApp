package hillel.spring.doctors;


import hillel.spring.doctors.model.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class DoctorRestRepository {

    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();
    private static int id;

    @Bean
    public AtomicInteger getAtomicInteger() {
        return new AtomicInteger(doctors.size());
    }

    {
        doctors.add(new Doctor(1, "Amosov", "cardiologist"));
        doctors.add(new Doctor(2, "Pirogovskiy", "surgeon"));
        doctors.add(new Doctor(3, "Dr. Sklifasovskiy", "surgeon"));

        id = doctors.size();
    }

    public List<Doctor> findAllDoctors() {
        return doctors;
    }

    public Integer createDoctor(Doctor doctor) {
        id++;
        doctors.add(new Doctor(id, doctor.getName(), doctor.getSpecialization()));
        return id;
    }

    public Optional<Doctor> findDoctorByID(Integer id) {
        return doctors.stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst();
    }

    public List<Doctor> findDoctorBySpecialization(String specialization) {
        return doctors.stream()
                .filter(doctorModel -> doctorModel.getSpecialization().equals(specialization))
                .collect(Collectors.toList());
    }

    public List<Doctor> findDoctorsByFirstLetter(String letter) {
        return doctors.stream()
                .filter(doctorModel -> doctorModel.getName().toLowerCase().startsWith(letter))
                .collect(Collectors.toList());
    }

    public void updateDoctor(Integer id, Doctor doctor) {
        doctors.stream()
                .filter(doc -> doc.getId().equals(id))
                .map(doctorModel -> new Doctor(doctorModel.getId(), doctorModel.getName(), doctorModel.getSpecialization()));

    }

    public void deleteDoctor(Integer id) {
        doctors.removeIf(doc -> doc.getId().equals(id));
    }
}
