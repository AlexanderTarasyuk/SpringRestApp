package hillel.spring.doctors;

import hillel.spring.doctors.model.Doctor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Database {

    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();
}
