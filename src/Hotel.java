import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

public class Hotel
{
    public static void checkin(int roomNumber, LocalDate from, LocalDate to, String note, int guests, ArrayList<Room> rooms) {
        boolean roomFound = false;

        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                roomFound = true;
                if (room.isAvailability()) {
                    // Check for overlapping reservations
                    if (guests == 0) {
                        room.setGuests(room.getBeds());
                    } else {
                        if(guests > room.getBeds())
                        {
                            System.out.println("You can't checkin more guests than there are beds");
                            return;
                        }
                        else
                        {
                            room.setGuests(guests);
                        }
                    }
                    boolean overlap = false;
                    if(room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        for (ArrayList<Dates> datesList : room.getListOfLists()) {
                            for (Dates dates : datesList) {
                                if (!(from.isAfter(dates.getDate2()) || to.isBefore(dates.getDate1()))) {
                                    overlap = true;
                                    break;
                                }
                            }
                        }
                    }
                        if (overlap) {
                            System.out.println("Cannot check in. Room " + roomNumber + " is already booked for this period.");
                            break;
                        }

                    if (!overlap) {
                        room.setAvailability(false);
                        room.setFrom(from);
                        room.setTo(to);
                        room.setNote(note);

                        ArrayList<ArrayList<Dates>> listOfLists = new ArrayList<>();
                        ArrayList<Dates> listDates = new ArrayList<>();
                        listDates.add(new Dates(from, to));
                        listOfLists.add(listDates);
                        room.setListOfLists(listOfLists);

                        System.out.println("You have checked in room " + roomNumber);
                    }
                } else {
                    System.out.println("Room " + roomNumber + " is not available.");
                }
            }
        }
        if (!roomFound) {
            System.out.println("Such room does not exist.");
        }
    }

    public static void availability(ArrayList<Room> rooms, LocalDate date) {
        boolean availableRooms = false;
        System.out.println("Available rooms: ");
        for (Room room : rooms) {
            if (room.isAvailability()) {
                boolean roomAvailable = true;
                if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                    for (ArrayList<Dates> datesList : room.getListOfLists()) {
                        for (Dates dates : datesList) {
                            if (!(date.isBefore(dates.getDate1()) || date.isAfter(dates.getDate2()))) {
                                roomAvailable = false;
                                break;
                            }
                        }
                        if (!roomAvailable) {
                            break;
                        }
                    }
                }
                if (roomAvailable) {
                    availableRooms = true;
                    System.out.println("Room " + room.getRoom());
                }
            }
        }
        if (!availableRooms) {
            System.out.println("There are no available rooms on this date");
        }
    }

    public static void checkout(int roomNumber, ArrayList<Room> rooms)
    {
        boolean roomFound = false;
        for(Room room : rooms) {
                if (room.getRoom() == roomNumber) {
                    if(!room.isAvailability()) {
                        room.setAvailability(true);
                        System.out.println("You have checked out of room " + roomNumber);
                    }else {
                        System.out.println("This room is already checked out");
                    }
                    roomFound = true;
                }
            }
        if (!roomFound) {
            System.out.println("Such room does not exist.");
        }
    }

    public static void report(ArrayList<Room> rooms, LocalDate from, LocalDate to)
    {
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Dates> matchedDates = new ArrayList<>();
        for (Room room : rooms) {
            ArrayList<ArrayList<Dates>> listOfLists = room.getListOfLists();
            if (listOfLists != null) {
                //System.out.println(listOfLists);
                for (ArrayList<Dates> datesList : listOfLists) {
                    if (!datesList.isEmpty()) {
                        for (Dates dates : datesList) {
                            //System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                            if(from.compareTo(dates.getDate1())>=0 && to.compareTo(dates.getDate2())<=0) {
                                matchedDates.add(dates);
                                listOfRooms.add(room);
                            }
                        }
                    }
                }
            }
        }
        for(int i = 0; i < listOfRooms.size(); i++) {
            int numberOfDays = (int) ChronoUnit.DAYS.between(matchedDates.get(i).getDate1(),matchedDates.get(i).getDate2());
            System.out.println("Room number: " + listOfRooms.get(i).getRoom() + ", Dates: " + matchedDates.get(i).getDate1() + " " + matchedDates.get(i).getDate2() + ", Number of days: " + numberOfDays);
        }
        if(listOfRooms.isEmpty()){
            System.out.println("There is no room that meets the requirement");
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

        System.out.println("Available rooms: ");
        boolean bedsFound = false;
        for (Room room : rooms) {
            if (room.isAvailability() && room.getBeds() == beds) {
                if (isRoomAvailableForDates(room, from, to)) {
                    if(room.isAvailability()) {
                        if (room.getBeds() == beds) {
                            System.out.println(room.getRoom());
                            bedsFound = true;
                            break;
                        }
                    }
                }
            }
        }
        if(!bedsFound) {
        System.out.println("There are no available rooms with this number of beds");
        }
    }

    private static boolean isRoomAvailableForDates(Room room, LocalDate from, LocalDate to) {
        ArrayList<ArrayList<Dates>> listOfLists = room.getListOfLists();
        if (listOfLists == null) {
            return true;
        }

        for (ArrayList<Dates> datesList : listOfLists) {
            if (!datesList.isEmpty()) {
                for (Dates dates : datesList) {
                    LocalDate date1 = dates.getDate1();
                    LocalDate date2 = dates.getDate2();
                    if ((from.isBefore(date2) || from.isEqual(date2)) && (to.isAfter(date1) || to.isEqual(date1))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void findVip(int beds, LocalDate from, LocalDate to, ArrayList<Room> rooms)
    {
        boolean available = true;
        ArrayList<Room> listOfRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailability()) {
                available = false;
                break;
            }
        }
            if(available) {
                for(Room room : rooms) {
                    ArrayList<ArrayList<Dates>> listOfLists = room.getListOfLists();
                    if (listOfLists != null) {
                        //System.out.println(listOfLists);
                        for (ArrayList<Dates> datesList : listOfLists) {
                            if (!datesList.isEmpty()) {
                                for (Dates dates : datesList) {
                                    //System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                                    if(from.compareTo(dates.getDate1())>=0 && to.compareTo(dates.getDate2())<=0) {
                                        if(room.getBeds() > room.getGuests()) {
                                            listOfRooms.add(room);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                boolean moved = false;
                for (Room room : listOfRooms) {
                    if (room.getBeds() >= beds) {
                        for (Room otherRoom : listOfRooms) {
                            if (otherRoom.getBeds() > room.getBeds() && otherRoom.getGuests() < otherRoom.getBeds()) {
                                int availableSpace = otherRoom.getBeds() - otherRoom.getGuests();
                                int guestsToMove = Math.min(availableSpace, room.getGuests());
                                otherRoom.setGuests(otherRoom.getGuests() + guestsToMove);
                                room.setGuests(room.getGuests() - guestsToMove);
                                moved = true;
                                System.out.println("Moved " + guestsToMove + " guests from room " + room.getRoom() + " to room " + otherRoom.getRoom());
                            }
                        }
                    }
                }
                if(!moved)
                {
                    System.out.println("There's nothing that can be done");
                }
            } else {
                System.out.println("There are available rooms on these dates");
            }
    }

    public static void unavailable(int roomNumber, LocalDate from, LocalDate to,String note, ArrayList<Room> rooms)
    {
        boolean unavailableCheck = false;
        for(Room room : rooms) {
            if(room.getRoom() == roomNumber) {
                room.setAvailability(false);
                room.setFrom(from);
                room.setTo(to);
                room.setNote(note);
                unavailableCheck = true;
                System.out.println("Room " + roomNumber + "will be unavailable from " + room.getFrom() + " to " + room.getTo());
            }
        }
        if(!unavailableCheck) {
            System.out.println("Such room does not exist");
        }
    }
}