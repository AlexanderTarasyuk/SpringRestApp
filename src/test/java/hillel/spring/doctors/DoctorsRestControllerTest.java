package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorsRestControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    DoctorRepository repository;

    @Before
    @Rollback
    @Transactional
    public void init() {

        repository.save(new Doctor(1, "Amosov", "cardiologist"));
        repository.save(new Doctor(2, "Pirogovskiy", "surgeon"));
        repository.save(new Doctor(3, "Sklifasovskiy", "surgeon"));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldFindDoctorById() throws Exception {

        mockMvc.perform(get("/doctors/{id}", 1))
                .andExpect(jsonPath("$.name", is("Amosov")))
                .andExpect(jsonPath("$.specialization", is("cardiologist")));


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

        assertThat(repository.findById(id)).isPresent();


    }

    @Test
    public void shouldFindAllDoctors() throws Exception {


        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().json(fromResource("hillel/spring/doctors/all-doctors.json"), false))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[1].name", is("Pirogovskiy")))
                .andExpect(jsonPath("$[2].name", is("Sklifasovskiy")))
                .andExpect(jsonPath("$[0].specialization", is("cardiologist")))
                .andExpect(jsonPath("$[1].specialization", is("surgeon")))
                .andExpect(jsonPath("$[2].specialization", is("surgeon")));

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
                .andExpect(jsonPath("$[0].specialization", is("surgeon")))
                .andExpect(jsonPath("$[1].specialization", is("surgeon")));

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
                .andExpect(jsonPath("$[0].specialization", is("cardiologist")));

    }


    @Test
    public void shouldNotDeleteDoctor() throws Exception {

        mockMvc.perform(delete("/doctors/{id}", 1000))
                .andExpect(status().isNotFound());

    }


    @Test
    public void shouldCheckSpecialization() throws Exception {
        repository.deleteAll();
        mockMvc.perform(post("/doctors")
                .contentType("application/json")
                .content(fromResource("hillel/spring/doctors/doctor-wrong spec.json")))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll()).isEmpty();
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
                .andExpect(jsonPath("$[0].specialization", is("cardiologist")));
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