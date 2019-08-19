package hillel.spring.doctors;

import hillel.spring.doctors.exceptions.AppointmentIsBusy;
import hillel.spring.doctors.exceptions.InvalidSpecializationException;
import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.doctors.exceptions.ScheduleIsAlreadyBusy;
import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.pet.NoSuchPetException;
import hillel.spring.pet.Pet;
import hillel.spring.pet.PetService;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service

public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final List<String> specializations;

    private final PetService petService;

    public DoctorService(@Value("${doctors.specializations}") String[] specializations,
                         DoctorRepository doctorRepository,
                         PetService petService) {
        this.specializations = List.of(specializations);
        this.doctorRepository = doctorRepository;
        this.petService = petService;
    }


    public Doctor createDoctor(Doctor doctor) {
        checkSpecialization(doctor);
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> findById(Integer id) {
        return doctorRepository.findById(id);
    }

    public void update(Doctor doctor) {
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


    public List<Doctor> findAll(Optional<String> letter, Optional<String> specialization) {
        if (specialization.isPresent() && letter.isPresent()) {
            return doctorRepository.findBySpecializationInAndNameIgnoreCaseStartingWith(specialization.get(), letter.get());
        }
        if (specialization.isPresent()) {
            return doctorRepository.findBySpecializationIn(specialization.get());
        }
        if (letter.isPresent()) {
            return doctorRepository.findByNameIgnoreCaseStartingWith(letter.get());
        }
        return doctorRepository.findAll();
    }


    public Appointment findOrCreateSchedule(Integer doctorId, LocalDate date) {
        val mayBeDoctor = findById(doctorId);
        Doctor doctor = mayBeDoctor.orElseThrow(NoSuchDoctorException::new);

        return doctor.getScheduleToDate().computeIfAbsent(date, k -> new Appointment());
    }

    @Transactional
    public void schedulePetToDoctor(Integer doctorId, LocalDate date, Integer hour, Integer petId) {
        Optional<Doctor> mayBeDoctor = findById(doctorId);
        Doctor doctor = mayBeDoctor.orElseThrow(NoSuchDoctorException::new);
        Appointment appointment = doctor.getScheduleToDate().computeIfAbsent(date, k -> new Appointment());
        Optional<Pet> maybePet = petService.findById(petId);
        if (maybePet.isEmpty()) {
            throw new NoSuchPetException();
        } else {
            putToSchedule(appointment, hour, petId);
            doctorRepository.save(doctor);
        }
    }


    public void putToSchedule(Appointment appointment, Integer hour, Integer petId) {

        if (appointment.getHourToPetId().containsKey(hour)) {
            throw new ScheduleIsAlreadyBusy(hour);
        } else {
            appointment.getHourToPetId().put(hour, petId);
        }
    }

    private void checkSpecialization(Doctor doctor) {
        Optional<String> maybeInvalid = doctor.getSpecialization().stream()
                .filter(s -> !specializations.contains(s))
                .findFirst();

        if (maybeInvalid.isPresent()) {
            throw new InvalidSpecializationException(maybeInvalid.get());
        }
    }


    private Predicate<Doctor> filterByName(String letter) {
        return doctor -> doctor.getName().toLowerCase().startsWith(letter.toLowerCase());
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }


    @Transactional
    public void moveAppointment(LocalDate date, Integer fromDoctorId, Integer toDoctorId) {
        Optional<Doctor> fromMaybeDoctor = doctorRepository.findById(fromDoctorId);
        if (fromMaybeDoctor.isEmpty())
            throw new NoSuchDoctorException(fromDoctorId);
        Optional<Doctor> toMaybeDoctor = doctorRepository.findById(toDoctorId);
        if (toMaybeDoctor.isEmpty())
            throw new NoSuchDoctorException(toDoctorId);
        Doctor fromDoctor = fromMaybeDoctor.get();
        Doctor toDoctor = toMaybeDoctor.get();

        Appointment scheduleFrom = fromDoctor.getScheduleToDate().get(date);
        Appointment scheduleTo = findOrCreateSchedule(toDoctor.getId(), date);

        Optional<Integer> mayBeBusyHour = scheduleFrom.getHourToPetId()
                .keySet()
                .stream()
                .filter(hour -> scheduleTo.getHourToPetId().containsKey(hour))
                .findFirst();

        if (mayBeBusyHour.isPresent()) {
            throw new AppointmentIsBusy(mayBeBusyHour.get());
        }

        scheduleTo.getHourToPetId().putAll(scheduleFrom.getHourToPetId());
        scheduleFrom.getHourToPetId().clear();

        doctorRepository.save(toDoctor);
        doctorRepository.save(fromDoctor);
    }
}
