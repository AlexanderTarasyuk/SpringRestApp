package hillel.spring.doctors;


import hillel.spring.doctors.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DoctorRestRepository {

    private DatabaseDoctor database;

    public DoctorRestRepository(DatabaseDoctor database) {
        this.database = database;
    }

    public List<Doctor> findAllDoctors() {
        return new ArrayList<>(database.getDoctors().values());
    }

    public Integer createDoctor(Doctor doctor) {
        int id = database.getId().incrementAndGet();
        database.getDoctors().put(id, new Doctor(id, doctor.getName(), doctor.getSpecialization()));
        return id;
    }

    public Optional<Doctor> findDoctorByID(Integer id) {
        return database.getDoctors().values().stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst();
    }

    public List<Doctor> findDoctorBySpecialization(String specialization) {
        return database.getDoctors().values().stream()
                .filter(doctorModel -> doctorModel.getSpecialization().equals(specialization))
                .collect(Collectors.toList());
    }

    public List<Doctor> findDoctorsByFirstLetter(String letter) {
        return database.getDoctors().values().stream()
                .filter(doctorModel -> doctorModel.getName().toLowerCase().startsWith(letter))
                .collect(Collectors.toList());
    }

    public void updateDoctor(Integer id, Doctor doctor) {

        database.getDoctors().put(id - 1, doctor);
    }

    public void deleteDoctor(Integer id) {
        database.getDoctors().values().removeIf(doc -> doc.getId().equals(id));
    }
}
