package hillel.spring.doctors.exceptions;

public class AppointmentIsBusy extends RuntimeException {
    public AppointmentIsBusy(Integer time) {
        super("Appointment is already busy: " + time);

    }
}
