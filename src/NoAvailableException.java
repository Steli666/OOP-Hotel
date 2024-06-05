public class NoAvailableException extends Exception {
    public NoAvailableException() {
        super("There are no available rooms on this date");
    }
}
