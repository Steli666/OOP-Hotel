public class RoomNotFoundException extends Exception {
    public RoomNotFoundException() {
        super("Such room does not exist");
    }
}