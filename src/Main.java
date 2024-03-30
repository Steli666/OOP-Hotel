import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        Room room1 = new Room(231, "No room service", 4, 5);
        Room room2 = new Room(102, "Ocean view", 2, 1);
        Room room3 = new Room(311, "Pet-friendly", 3, 2);
        Room room4 = new Room(420, "Executive suite", 2, 1);
        Room room5 = new Room(501, "Accessible for disabled guests", 1, 1);
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        rooms.add(room5);
//        Hotel.checkin(102,"",4,LocalDate.of(2020, 6, 12),LocalDate.of(2020, 6, 19), rooms);
//        Hotel.checkin(231,"",4,LocalDate.of(2020, 6, 12),LocalDate.of(2020, 6, 19), rooms);
//        Hotel.checkout(102,rooms);
//        Hotel.availability(rooms, LocalDate.of(2020,6,18));
    }
}