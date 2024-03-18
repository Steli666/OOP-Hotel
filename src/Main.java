import java.time.LocalDate;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        Room room1 = new Room(231, "No room service", 4, 5, LocalDate.of(2020, 2, 2), LocalDate.of(2020, 2, 5));
        Room room2 = new Room(102, "Ocean view", 2, 1, LocalDate.of(2020, 3, 10), LocalDate.of(2020, 3, 15));
        Room room3 = new Room(311, "Pet-friendly", 3, 2, LocalDate.of(2020, 4, 20), LocalDate.of(2020, 4, 25));
        Room room4 = new Room(420, "Executive suite", 2, 1, LocalDate.of(2020, 5, 12), LocalDate.of(2020, 5, 17));
        Room room5 = new Room(501, "Accessible for disabled guests", 1, 1, LocalDate.of(2020, 6, 8), LocalDate.of(2020, 6, 12));
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);
        rooms.add(room5);
    }
}