import java.util.ArrayList;
import java.time.LocalDate;

public class Hotel
{
        public static void checkin(int roomNumber, String note, int guests, LocalDate from, LocalDate to, ArrayList<Room> rooms)
        {
            for(Room room : rooms)
            {
                if(room.getRoom() == roomNumber)
                {
                    room.setAvailability(false);
                    room.setTo(to);
                    room.setFrom(from);
                    room.setNote(note);
                    if(guests == 0)
                    {
                        room.setGuests(room.getBeds());
                    }
                }
            }
        }
    public static void availability(ArrayList<Room> rooms, LocalDate date)
    {
        System.out.println("Available rooms: ");
        for(Room room : rooms)
        {
            if(room.isAvailability())
            {
                System.out.println(room.getRoom());
            }
            else if(!(room.isAvailability()))
            {
                if (date == null) {
                    date = LocalDate.now();
                }
                if (date.isBefore(room.getFrom()) || date.isAfter(room.getTo()))
                {
                    System.out.println(room.getRoom());
                }
            }
            else
            {
                System.out.println("There are no available rooms on this date");
            }
        }
    }
    public static void checkout(int roomNumber, ArrayList<Room> rooms)
    {
        for(Room room : rooms)
        {
            if(room.getRoom() == roomNumber)
            {
                room.setAvailability(true);
            }
        }
    }
}