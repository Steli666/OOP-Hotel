import java.time.LocalDate;

public class SuccessfulCheckin extends Exception {
    public SuccessfulCheckin(Room room, int roomNumber) {
        super("The id of the reservation: " + room.getListOfLists().get(room.getListOfLists().size() - 1).getId()
                + ", You have checked in room " + roomNumber);
    }
}
