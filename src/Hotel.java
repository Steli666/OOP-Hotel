import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

public class Hotel
{
        public static void checkin(int roomNumber, String note, int guests, LocalDate from, LocalDate to, ArrayList<Room> rooms)
        {
            for(Room room : rooms)
            {
                if(room.isAvailability())
                {
                    if(room.getRoom() == roomNumber)
                    {
                        room.setAvailability(false);
                        room.setFrom(from);
                        room.setTo(to);
                        room.setNote(note);
                        ArrayList<ArrayList<Dates>> listOfLists = new ArrayList<>();
                        ArrayList<Dates> listDates = new ArrayList<>();
                        listDates.add(new Dates(from, to));
                        listOfLists.add(listDates);
                        room.setListOfLists(listOfLists);

                        ArrayList<ArrayList<Dates>> roomListOfLists = room.getListOfLists();
                        for (ArrayList<Dates> datesList : roomListOfLists)
                        {
                            for (Dates dates : datesList)
                            {
                                System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                            }
                        }
                        if (guests == 0) {
                            room.setGuests(room.getBeds());
                        }
                    }
                }
                else
                {
                    System.out.println("There is no such available room");
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

    public static void report(ArrayList<Room> rooms, LocalDate from, LocalDate to)
    {
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Dates> matchedDates= new ArrayList<>();
        for (Room room : rooms) {
            ArrayList<ArrayList<Dates>> listOfLists = room.getListOfLists();
            if (listOfLists != null) {
                //System.out.println(listOfLists);
                for (ArrayList<Dates> datesList : listOfLists) {
                    if (!datesList.isEmpty()) {
                        for (Dates dates : datesList) {
                            //System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                            if(from.compareTo(dates.getDate1())>=0 && to.compareTo(dates.getDate2())<=0)
                            {
                                matchedDates.add(dates);
                                listOfRooms.add(room);
                            }
                        }
                    }
                }
            }
        }
        for(int i = 0; i <listOfRooms.size();i++)
        {
            System.out.println(listOfRooms.get(i).getRoom() + " " + matchedDates.get(i).getDate1() + " " + matchedDates.get(i).getDate2());
            int numberOfDays = (int) ChronoUnit.DAYS.between(matchedDates.get(i).getDate1(),matchedDates.get(i).getDate2());
            System.out.println(numberOfDays);
            //да си форматирам данните
        }

    }
    public static void find(int beds, LocalDate from, LocalDate to, ArrayList<Room> rooms)
    {
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room room1, Room room2)
            {
                return Integer.compare(room1.getBeds(),room2.getBeds());
            }
        });
        for(Room room : rooms)
        {
            //System.out.println(room.getBeds());
            if(room.isAvailability())
            {
                if (room.getBeds() == beds)
                {
                    System.out.println(room.getRoom());
                }
            }
        }
    }
    public static void unavailable(int roomNumber, LocalDate from, LocalDate to,String note, ArrayList<Room> rooms)
    {
        for(Room room : rooms)
        {
            if(room.getRoom() == roomNumber)
            {
                room.setAvailability(false);
                room.setFrom(from);
                room.setTo(to);
                room.setNote(note);
            }
        }
    }
}