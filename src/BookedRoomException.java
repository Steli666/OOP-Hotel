public class BookedRoomException extends Exception {
    public BookedRoomException(int roomNumber) {
        super("Cannot check in. Room " + roomNumber + " is already booked for this period or part of it.");
    }
}