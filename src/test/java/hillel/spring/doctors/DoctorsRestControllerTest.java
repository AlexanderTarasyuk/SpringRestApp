package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorsRestControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    DoctorRestRepository repository;

    @Before
    public void init() {
        repository.createDoctor(new Doctor(1, "Amosov", "cardiologist"));
        repository.createDoctor(new Doctor(2, "Pirogovskiy", "surgeon"));
        repository.createDoctor(new Doctor(3, "Sklifasovskiy", "surgeon"));
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAllDoctors();
    }


    @Test
    public void shouldCreateDoctor() throws Exception {


        mockMvc.perform(post("/doctors")
                .contentType("application/json")
                .content(fromResource("hillel/spring/doctors/create-doctor.json")))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/doctors/4"));

        assertThat(repository.findDoctorByID(4)).isPresent();


    }

    @Test
    public void shouldFindAllDoctors() throws Exception {

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().json(fromResource("hillel/spring/doctors/all-doctors.json"), false))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[1].name", is("Pirogovskiy")))
                .andExpect(jsonPath("$[2].name", is("Sklifasovskiy")))
                .andExpect(jsonPath("$[0].specialization", is("cardiologist")))
                .andExpect(jsonPath("$[1].specialization", is("surgeon")))
                .andExpect(jsonPath("$[2].specialization", is("surgeon")));


    }


    @Test
    public void shouldFindDoctorById() throws Exception {
        mockMvc.perform(get("/doctors/{id}", 1))
                .andExpect(jsonPath("$.name", is("Amosov")))
                .andExpect(jsonPath("$.specialization", is("cardiologist")));
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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].specialization", is("surgeon")))
                .andExpect(jsonPath("$[1].specialization", is("surgeon")));

    }

    @Test
    public void shouldFindAllNamesStartingWithA() throws Exception {

        mockMvc.perform(get("/doctors")
                .param("name", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", startsWith("A")));


    }

    @Test
    public void shouldFindAllSurgeonsStartingWithA() throws Exception {
        mockMvc.perform(get("/doctors")

                .param("name", "A")
                .param("specialization", "cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Amosov")))
                .andExpect(jsonPath("$[0].specialization", is("cardiologist")));
    }

    @Test
    public void shouldUpdateDoctor() throws Exception {
        mockMvc.perform(put("/doctors/{id}", 1)
                .contentType("application/json")
                .content(fromResource("hillel/spring/doctors/update-doctor.json")))
                .andExpect(status().isOk());

        assertThat(repository.findDoctorByID(1).get().getName()).isEqualTo("House");
    }


    @Test
    public void shouldDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/doctors/{id}", 2))
                .andExpect(status().isNoContent());
        assertThat(repository.findDoctorByID(2)).isEmpty();
    }

    @Test
    public void shouldNotDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/doctors/{id}", 5))
                .andExpect(status().isNotFound());

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