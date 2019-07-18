package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Primary
public class DatabaseDoctor {

    private List<Doctor> doctors;
    private AtomicInteger id;


    public DatabaseDoctor() {
        doctors = new CopyOnWriteArrayList<>();
        doctors.add(new Doctor(1, "Amosov", "cardiologist"));
        doctors.add(new Doctor(2, "Pirogovskiy", "surgeon"));
        doctors.add(new Doctor(3, "Dr. Sklifasovskiy", "surgeon"));

        id = new AtomicInteger(doctors.size());
    }

    AtomicInteger getId() {
        return id;
    }

    List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

}
