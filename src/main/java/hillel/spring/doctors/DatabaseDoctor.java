package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Primary
public class DatabaseDoctor {

    private Map<Integer, Doctor> intToDoctor;
    private AtomicInteger id;


    public DatabaseDoctor() {
        intToDoctor = new ConcurrentHashMap<>();
        intToDoctor.put(1, new Doctor(1, "Amosov", "cardiologist"));
        intToDoctor.put(2, new Doctor(2, "Pirogovskiy", "surgeon"));
        intToDoctor.put(3, new Doctor(3, "Dr. Sklifasovskiy", "surgeon"));

        id = new AtomicInteger(intToDoctor.size());
    }

    AtomicInteger getId() {
        return id;
    }

    Map<Integer, Doctor> getDoctors() {
        return intToDoctor;
    }


}
