package hillel.spring.doctors;

import hillel.spring.doctors.exceptions.AppointmentIsBusy;
import hillel.spring.doctors.exceptions.InvalidSpecializationException;
import hillel.spring.doctors.exceptions.NoSuchDoctorException;
import hillel.spring.doctors.exceptions.ScheduleIsAlreadyBusy;
import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Diplom;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.pet.NoSuchPetException;
import hillel.spring.pet.Pet;
import hillel.spring.pet.PetService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j

public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final List<String> specializations;

    private final PetService petService;
    private static String LOG = DoctorService.class.getCanonicalName();

    public DoctorService(@Value("${doctors.specializations}") String[] specializations,
                         DoctorRepository doctorRepository,
                         PetService petService) {
        this.specializations = List.of(specializations);
        this.doctorRepository = doctorRepository;
        this.petService = petService;

    }


    public Doctor createDoctor(Doctor doctor, Diplom diplom) {

        log.debug(LOG, "creating doctor");
        doctor.setDiplom(diplom);
        log.debug(LOG, " doctor is created");
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> findById(Integer id) {
        return doctorRepository.findById(id);
    }

    public void update(Doctor doctor) {
        log.debug(LOG, "updating doctor with id: " +doctor.getId());

        if (doctorRepository.existsById(doctor.getId())) {
            doctorRepository.save(doctor);
        } else {
            throw new NoSuchDoctorException();
        }
        log.debug(LOG, "updated doctor with id: " +doctor.getId());

    }

    public boolean delete(Integer id) {

        log.debug(LOG, "deleting doctor with id: "+ id);

        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            log.debug(LOG, "deleted doctor with id: "+ id);
            return true;
        }
        log.debug(LOG, "not deleted doctor with id: "+ id);
        return false;
    }


    public Page<Doctor> findAll(Optional<String> letter, Optional<List<String>> specialization, Pageable pageable) {
        if (specialization.isPresent() && letter.isPresent()) {
            log.debug(LOG, " doctor is found with letter and specialization: "+ letter +" " + specialization);
            return doctorRepository.findBySpecializationInAndNameIgnoreCaseStartingWith(specialization.get(), letter.get(), pageable);
        }
        if (specialization.isPresent()) {
            log.debug(LOG, " doctor is found  with specialization: " +" " + specialization);
            return doctorRepository.findBySpecializationIn(specialization.get(), pageable);
        }
        if (letter.isPresent()) {
            log.debug(LOG, " doctor is found with letter : "+ letter);
            return doctorRepository.findByLetterIgnoreCaseStartingWith(letter.get(), pageable);
        }
        return (Page<Doctor>) doctorRepository.findAll();
    }


    public Appointment findOrCreateSchedule(Integer doctorId, LocalDate date) {

        log.debug(LOG, "finding or creating schedule" );
        val mayBeDoctor = findById(doctorId);
        Doctor doctor = mayBeDoctor.orElseThrow(NoSuchDoctorException::new);
        log.debug(LOG, "found or created schedule" );
        return doctor.getScheduleToDate().computeIfAbsent(date, k -> new Appointment());
    }

    @Transactional
    @Retryable(StaleObjectStateException.class)
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


    @Transactional
    public void moveAppointment(LocalDate date, Integer fromDoctorId, Integer toDoctorId) {
        Optional<Doctor> fromMaybeDoctor = doctorRepository.findById(fromDoctorId);
        if (fromMaybeDoctor.isEmpty())
            throw new NoSuchDoctorException(fromDoctorId);
        Optional<Doctor> toMaybeDoctor = doctorRepository.findById(toDoctorId);
        if (toMaybeDoctor.isEmpty())
            throw new NoSuchDoctorException(toDoctorId);
        var fromDoctor = fromMaybeDoctor.get();
        var toDoctor = toMaybeDoctor.get();

        var scheduleFrom = fromDoctor.getScheduleToDate().get(date);
        var scheduleTo = findOrCreateSchedule(toDoctor.getId(), date);

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


    private Predicate<Doctor> filterByName(String letter) {
        return doctor -> doctor.getName().toLowerCase().startsWith(letter.toLowerCase());
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }
}
