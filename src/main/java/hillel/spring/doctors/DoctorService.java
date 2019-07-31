package hillel.spring.doctors;

import hillel.spring.doctors.exceptions.InvalidSpecializationException;
import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.doctors.model.Doctor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service

public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final List<String> specializations;

    public DoctorService(@Value("${doctors.specializations}") String[] specializations,
                         DoctorRepository doctorRepository) {
        this.specializations=List.of(specializations);
        this.doctorRepository = doctorRepository;
    }


    public Doctor createDoctor(Doctor doctor) {
        checkSpecialization(doctor);
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> findById(Integer id){
        return doctorRepository.findById(id);
    }

    public void update(Doctor doctor)  {
        checkSpecialization(doctor);
        if (doctorRepository.existsById(doctor.getId()))
            doctorRepository.save(doctor);
        else
            throw new NoSuchDoctorException();
    }

    public boolean delete(Integer id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public List<Doctor> findAll(Optional<String> letter, Optional<String> specialization){
        if (specialization.isPresent() && letter.isPresent()){
            return doctorRepository.findBySpecializationInAndNameIgnoreCaseStartingWith(specialization.get(), letter.get());
        }
        if (specialization.isPresent()){
            return doctorRepository.findBySpecializationIn(specialization.get());
        }
        if (letter.isPresent()){
            return doctorRepository.findByNameIgnoreCaseStartingWith(letter.get());
        }
        return doctorRepository.findAll();
    }

    private void checkSpecialization(Doctor doctor) {
        if (!specializations.contains(doctor.getSpecialization()))
            throw new InvalidSpecializationException();
    }

//    public List<Doctor> findAll(Optional<String> letter, Optional<String> specialization) {
//        Optional<Predicate<Doctor>> maybeNamePredicate = letter.map(this::filterByName);
//        Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization.map(this::filterBySpecialization);
//
//
//        Predicate<Doctor> predicate = Stream.of(maybeNamePredicate, maybeSpecializationPredicate)
//                .flatMap(Optional::stream)
//                .reduce(Predicate::and)
//                .orElse(doctor -> true);
//
//        return doctorRepository.findAll()
//                .stream()
//                .filter(predicate)
//                .collect(Collectors.toList());
//    }

    private Predicate<Doctor> filterByName(String letter) {
        return doctor -> doctor.getName().toLowerCase().startsWith(letter.toLowerCase());
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }
}
