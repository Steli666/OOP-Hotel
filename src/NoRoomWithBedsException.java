public class NoRoomWithBedsException extends Exception {
    public NoRoomWithBedsException() {
        super("There are no available rooms with this number of beds");
    }
}
