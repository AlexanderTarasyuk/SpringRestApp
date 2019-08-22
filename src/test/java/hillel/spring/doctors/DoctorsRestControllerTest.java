package hillel.spring.doctors;

import hillel.spring.doctors.model.Appointment;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.pet.Pet;
import hillel.spring.pet.PetRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@TestRunner
public class DoctorsRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    DoctorService doctorService;

    @Before
    @Rollback
    @Transactional
    public void init() {

        doctorRepository.save(new Doctor(1, "Amosov","cardiologist"));
        doctorRepository.save(new Doctor(2, "Pirogovskiy", "surgeon"));
        doctorRepository.save(new Doctor(3, "Sklifasovskiy", "surgeon"));

        petRepository.save(new Pet(null,  "Tom", "Cat", 2, "Vasya"));
        petRepository.save(new Pet(null, "Jerry", "Mouse", 1, "Vasya"));
    }

    @After
    public void cleanUp() {
        doctorRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    public void shouldFindDoctorById() throws Exception {

        mockMvc.perform(get("/doctors/{id}", 1))
                .andExpect(jsonPath("$.name", is("Amosov")))
                .andExpect(jsonPath("$.specializations[0]", is("cardiologist")));


    }

    @Test
    public void shouldCreateDoctor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/doctors")
                .contentType("application/json")
                .content(fromResource("hillel/spring/doctors/create-doctor.json")))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/doctors/")))
                .andReturn().getResponse();

        Integer id = Integer.parseInt(response.getHeader("location")
                .replace("http://localhost/doctors/", ""));

        assertThat(doctorRepository.findById(id)).isPresent();


    }

    @Test
    public void shouldFindAllDoctors() throws Exception {

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().json(fromResource("hillel/spring/doctors/all-doctors.json"), false))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[1].name", is("Pirogovskiy")))
                .andExpect(jsonPath("$[2].name", is("Sklifasovskiy")))
                .andExpect(jsonPath("$[0].specializations[0]", is("cardiologist")))
                .andExpect(jsonPath("$[1].specializations[0]", is("surgeon")))
                .andExpect(jsonPath("$[2].specializations[0]", is("surgeon")));

    }


    @Test
    public void shouldNotFindDoctorById() throws Exception {

        mockMvc.perform(get("/doctors/{id}", 5))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldFindAllSurgeons() throws Exception {


        mockMvc.perform(get("/doctors")
                .param("specialization", "surgeon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specializations[0]", is("surgeon")))
                .andExpect(jsonPath("$[1].specializations[0]", is("surgeon")));

    }


    @Test
    public void shouldFindAllNamesStartingWithA() throws Exception {


        mockMvc.perform(get("/doctors")
                .param("letter", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", startsWith("A")));

    }


    @Test
    public void shouldFindAllSurgeonsStartingWithA() throws Exception {


        mockMvc.perform(get("/doctors")

                .param("letter", "A")
                .param("specialization", "cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[0].specializations[0]", is("cardiologist")));
    }


    @Test
    public void shouldNotDeleteDoctor() throws Exception {

        mockMvc.perform(delete("/doctors/{id}", 1000))
                .andExpect(status().isNotFound());

    }


    @Test
    public void shouldCheckSpecialization() throws Exception {
        doctorRepository.deleteAll();
        mockMvc.perform(post("/doctors")
                .contentType("application/json")
                .content(fromResource("hillel/spring/doctors/doctor-wrong spec.json")))
                .andExpect(status().isBadRequest());

        assertThat(doctorRepository.findAll()).isEmpty();
    }

    @Test
    public void shouldFindByFirstLettersNevethelessRegistrIs() throws Exception {


        mockMvc.perform(get("/doctors")
                .param("letter", "AMOS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void shouldFindByFirstLettersAndSpecializationRegardlessRegister() throws Exception {

        mockMvc.perform(get("/doctors")
                .param("letter", "AM")
                .param("specialization", "cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[0].specializations[0]", is("cardiologist")));
    }

    @Test
    public void shouldSchedulePet() throws Exception {
        Integer doctorId = doctorRepository.save(new Doctor(1, "Ivan Ivanov", "surgeon")).getId();
        Integer petId = petRepository.save(new Pet(null,  "Tom", "Cat", 2, "Ivan")).getId();

        mockMvc.perform(post("/doctors/{id}/schedule/2010-01-01/10", doctorId)
                .contentType("application/json")
                .content("{ \"petId\" : \"" + petId + "\"}"))
                .andExpect(status().isOk());

        Doctor doctor = doctorRepository.findById(doctorId).get();
        Appointment appointment = doctor.getScheduleToDate().get(LocalDate.of(2010, 01, 01));
        assertThat(appointment.getHourToPetId().size()).isEqualTo(1);
        assertThat(appointment.getHourToPetId().containsKey(10)).isTrue();
        assertThat(appointment.getHourToPetId().containsValue(petId)).isTrue();
    }


    @Test
    public void shouldFindAppointmentInTheSchedule() throws Exception {
        Integer doctorId = doctorRepository.save(new Doctor(1, "Ivan Ivanov", "surgeon")).getId();
        Integer petId = 1;
        Doctor doctor = doctorRepository.findById(doctorId).get();
        LocalDate localDate = LocalDate.of(2010, 01, 01);
        Appointment appointment = new Appointment();
        doctorService.putToSchedule(appointment, 10, petId);
        doctor.getScheduleToDate().put(localDate, appointment);
        doctorRepository.save(doctor);

        mockMvc.perform(get("/doctors/{id}/schedule/2010-01-01", doctorId))
                .andExpect(jsonPath("$.hourToPetId.10", is(petId)));
    }

    @Test
    public void shouldNotAppointPetToWrongDoctor() throws Exception {
        Integer doctorId = doctorRepository.save(new Doctor(1, "Ivan Ivanov", "cardiologist")).getId();
        Integer petId = petRepository.save(new Pet(null, "Tom", "Cat", 2, "Vasya")).getId();

        mockMvc.perform(post("/doctors/{id}/schedule/2010-01-01/10", doctorId + 100)
                .contentType("application/json")
                .content("{ \"petId\" : \"" + petId + "\"}"))
                .andExpect(status().isNotFound())
        ;
    }


    @Test
    public void shouldNotAppointPetToWrongSchedule() throws Exception {
        Integer doctorId = doctorRepository.save(new Doctor(1, "Ivan Ivanov", "surgeon")).getId();
        Integer petId = petRepository.save(new Pet(null, "Tom", "Cat", 2, "Vasya")).getId();

        mockMvc.perform(post("/doctors/{id}/schedule/2010-01-01/1000", doctorId)
                .contentType("application/json")
                .content("{ \"petId\" : \"" + petId + "\"}"))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void shouldNotAppointPetIfScheduleIsBusy() throws Exception {
        Integer doctorId = doctorRepository.save(new Doctor(1, "Ivan Ivanov", "surgeon")).getId();
        Integer catId = 1;
        Integer mouseId = 2;

        mockMvc.perform(post("/doctors/{id}/schedule/2010-01-01/14", doctorId)
                .contentType("application/json")
                .content("{ \"petId\" : \"" + catId + "\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/doctors/{id}/schedule/2010-01-01/14", doctorId)
                .contentType("application/json")
                .content("{ \"petId\" : \"" + mouseId + "\"}"))
                .andExpect(status().isBadRequest());

        Doctor doctor = doctorRepository.findById(doctorId).get();
        Appointment appointment = doctor.getScheduleToDate().get(LocalDate.of(2010, 01, 01));
        assertThat(appointment.getHourToPetId().size()).isEqualTo(1);
        assertThat(appointment.getHourToPetId().containsKey(14)).isTrue();
        assertThat(appointment.getHourToPetId().containsValue(catId)).isTrue();
    }

    @Test
    public void shouldMoveAppointmentIfDoctorsAndscheduleArePresent() throws Exception {

        Integer petId = 1;
        Doctor doctorFromWhomWeGetAppointment = doctorRepository.findById(1).get();
        Doctor doctorToWhomWeGiveAppointment = doctorRepository.findById(2).get();
        LocalDate localDate = LocalDate.of(2010, 01, 01);
        Appointment appointment = new Appointment();
        doctorService.putToSchedule(appointment, 12, petId);
        doctorFromWhomWeGetAppointment.getScheduleToDate().put(localDate, appointment);
        doctorRepository.save(doctorFromWhomWeGetAppointment);

        mockMvc.perform(post("/doctors/move-schedule/2010-01-01/{doctorIdFrom}/{doctorIdTo}",
                doctorFromWhomWeGetAppointment.getId(),
                doctorToWhomWeGiveAppointment.getId()))
                .andExpect(status().isOk());

        assertThat(doctorToWhomWeGiveAppointment.getScheduleToDate().get(localDate).getHourToPetId()
                .equals(doctorFromWhomWeGetAppointment.getScheduleToDate().get(localDate).getHourToPetId())).isTrue();
        doctorFromWhomWeGetAppointment = doctorRepository.findById(doctorFromWhomWeGetAppointment.getId()).get();
        assertThat(doctorFromWhomWeGetAppointment.getScheduleToDate().get(localDate).getHourToPetId()).isEmpty();
    }

    @Test
    public void shouldNotMoveAppointmentIfFreeTimeIsAbsent() throws Exception {
        Doctor doctorFromWhomWeGetAppointment = doctorRepository.findById(1).get();
        Doctor doctorToWhomWeGiveAppointment = doctorRepository.findById(2).get();
        Integer petId1 = 1;
        Integer petId2 = 2;

        LocalDate localDate = LocalDate.of(2010, 01, 01);

        Appointment appointmentFromDoctorWhomWeGetAppointment = new Appointment();
        doctorService.putToSchedule(appointmentFromDoctorWhomWeGetAppointment, 12, petId1);
        doctorFromWhomWeGetAppointment.getScheduleToDate().put(localDate, appointmentFromDoctorWhomWeGetAppointment);
        doctorRepository.save(doctorFromWhomWeGetAppointment);

        Appointment appointmentToDoctorWhomWeGetAppointment = new Appointment();
        doctorService.putToSchedule(appointmentToDoctorWhomWeGetAppointment, 12, petId2);
        doctorToWhomWeGiveAppointment.getScheduleToDate().put(localDate, appointmentToDoctorWhomWeGetAppointment);
        doctorToWhomWeGiveAppointment = doctorRepository.save(doctorToWhomWeGiveAppointment);
        appointmentToDoctorWhomWeGetAppointment = doctorToWhomWeGiveAppointment.getScheduleToDate().get(localDate);

        mockMvc.perform(post("/doctors/move-schedule/2010-01-01/{doctorIdFrom}/{doctorIdTo}",
                doctorFromWhomWeGetAppointment.getId(),
                doctorToWhomWeGiveAppointment.getId()))
                .andExpect(status().isBadRequest());
        doctorToWhomWeGiveAppointment = doctorRepository.findById(doctorToWhomWeGiveAppointment.getId()).get();
        assertThat(doctorToWhomWeGiveAppointment.getScheduleToDate().get(localDate).equals(appointmentToDoctorWhomWeGetAppointment)).isTrue();
        assertThat(doctorFromWhomWeGetAppointment.getScheduleToDate().get(localDate).equals(appointmentFromDoctorWhomWeGetAppointment)).isTrue();
    }

    public String fromResource(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            return Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}