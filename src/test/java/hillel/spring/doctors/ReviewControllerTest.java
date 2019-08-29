package hillel.spring.doctors;


import hillel.spring.card.MedicalCard;
import hillel.spring.card.MedicalCardRepository;
import hillel.spring.card.MedicalRecord;
import hillel.spring.doctors.model.Doctor;
import hillel.spring.review.ReviewRepository;
import hillel.spring.review.model.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestRunner
public class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    MedicalCardRepository medicalCardRepository;

    @Test
    public void shouldCreateReview() throws Exception{
        Integer doctorId = doctorRepository.save(new Doctor(null, "Ivan Ivanov", "surgeon")).getId();

        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setRecords(new ArrayList<>());
        medicalCard.getRecords().add(new MedicalRecord());
        medicalCardRepository.save(medicalCard);

        MockHttpServletResponse response = mockMvc.perform(post("/doctors/{doctorId}/reviews", doctorId)
                .contentType("application/json")
                .content(fromResource("petclinic/review/create-review.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        Integer id = Integer.parseInt(response.getHeader("location")
                .replace("http://localhost/doctors/"+doctorId+"/reviews/", ""));

        assertThat(reviewRepository.findById(id)).isPresent();

    }


    @Test
    public void shouldUpdateReview() throws Exception{
        Integer doctorId = doctorRepository.save(new Doctor(null, "Ivan Ivanov", "surgeon")).getId();

        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setRecords(new ArrayList<>());
        medicalCard.getRecords().add(new MedicalRecord());
        medicalCardRepository.save(medicalCard);

        Review firstReview = new Review();
        firstReview.setDoctorId(doctorId);
        firstReview.setServiceStars(1);
        firstReview.setEquipmentsStars(2);
        firstReview.setQualificationsStars(3);
        firstReview.setTreatmentStars(4);
        firstReview.setGeneralStars(5);
        firstReview.setComments("Wonderful comment");
        reviewRepository.save(firstReview);
        firstReview.setVersion(1);
        Integer reviewId = reviewRepository.save(firstReview).getId();

        mockMvc.perform(
                put("/doctors/{doctorId}/reviews/{reviewId}", doctorId,reviewId)
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"version\" : 1,\n" +
                                "  \"id\" : " + reviewId + ",\n" +
                                "  \"doctorId\" : " + doctorId + ",\n" +
                                "  \"medicalRecordId\" : 1,\n" +
                                "  \"serviceStars\" : 1,\n" +
                                "  \"equipmentsStars\" : 2,\n" +
                                "  \"qualificationsStars\" : 3,\n" +
                                "  \"treatmentResultsStars\" : 4,\n" +
                                "  \"generalStars\" : 5,\n" +
                                "  \"comment\" : \"Wonderful comment\"\n" +
                                "}\n"))
                .andExpect(status().isNoContent());
        Review reviewUpdated = reviewRepository.findById(reviewId).get();
        assertThat(reviewUpdated.getGeneralStars().get()).isEqualTo(5);
        assertThat(reviewUpdated.getComments().get()).isEqualToIgnoringCase("Wonderful comment");
    }

    @Test
    public void shouldReturnAverageReviews()throws Exception{
        Integer doctorId = doctorRepository.save(new Doctor(null, "Ivan Ivanov", "surgeon")).getId();

        Review firstReview = new Review();
        firstReview.setDoctorId(doctorId);
        firstReview.setServiceStars(1);
        firstReview.setEquipmentsStars(2);
        firstReview.setQualificationsStars(3);
        firstReview.setTreatmentStars(4);
        firstReview.setGeneralStars(5);
        firstReview.setComments("Wonderful comment");
        reviewRepository.save(firstReview);

        Review secondReview = new Review();
        secondReview.setDoctorId(doctorId);
        secondReview.setServiceStars(1);
        secondReview.setEquipmentsStars(2);
        secondReview.setQualificationsStars(3);
        secondReview.setTreatmentStars(4);
        secondReview.setGeneralStars(5);
        secondReview.setComments("Wonderful comment");

        reviewRepository.save(secondReview);


        MockHttpServletResponse response = mockMvc.perform(get("/doctors/{doctorId}/reviews",doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageServiceStars", is(1.0)))
                .andExpect(jsonPath("$.averageEquipmentStars", is(2.0)))
                .andExpect(jsonPath("$.averageQualificationStars", is(3.0)))
                .andExpect(jsonPath("$.averageTreatmentResultsStars", is(4.0)))
                .andExpect(jsonPath("$.averageGeneralStars", is(5.0)))
                .andExpect(jsonPath("$.comments" ,hasSize(2)))
                .andReturn().getResponse();

    }

    @Test
    public void shouldReturnAverageReviewsWithNullValues()throws Exception{
        Integer doctorId = doctorRepository.save(new Doctor(null, "Ivan Ivanov", "surgeon")).getId();
        Review firstReview = new Review();
        firstReview.setDoctorId(doctorId);
        firstReview.setServiceStars(1);
        firstReview.setEquipmentsStars(2);
        firstReview.setQualificationsStars(3);
        firstReview.setTreatmentStars(4);
        firstReview.setGeneralStars(5);
        firstReview.setComments("Wonderful comment");
        reviewRepository.save(firstReview);

        Review secondReview = new Review();
        secondReview.setDoctorId(doctorId);
        secondReview.setComments("another comment");
        reviewRepository.save(secondReview);

        MockHttpServletResponse response = mockMvc.perform(get("/doctors/{doctorId}/reviews",doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageServiceStars", is(1.0)))
                .andExpect(jsonPath("$.averageEquipmentStars", is(2.0)))
                .andExpect(jsonPath("$.averageQualificationStars", is(3.0)))
                .andExpect(jsonPath("$.averageTreatmentResultsStars", is(4.0)))
                .andExpect(jsonPath("$.averageGeneralStars", is(5.0)))
                .andExpect(jsonPath("$.comments" ,hasSize(2)))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());

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
