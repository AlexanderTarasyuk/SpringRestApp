package hillel.spring.doctors.exceptions;

public class ScheduleIsAlreadyBusy extends RuntimeException {
    public ScheduleIsAlreadyBusy(Integer hour) {
        super("Schedule hour " + hour + " is already occupied: ");
    }
}
