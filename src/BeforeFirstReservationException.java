public class BeforeFirstReservationException extends Exception {
    public BeforeFirstReservationException() {
        super("You cannot checkin before the first reservation.");
    }
}