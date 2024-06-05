import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Hotel {

    private ArrayList<Room> rooms;

    public Hotel(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void clearRooms() {
        rooms.clear();
    }

    public boolean availability(LocalDate from, LocalDate to) {
        int br = 0;
        for (Room room : rooms) {
            ArrayList<Reservations> listOfReservations = room.getListOfLists();
            if (listOfReservations != null && !listOfReservations.isEmpty()) {
                for (Reservations reservations : listOfReservations) {
                    if (from.isBefore(reservations.getDate2()) || from.isEqual(reservations.getDate2()) && to.isAfter(reservations.getDate1()) || to.isEqual(reservations.getDate1())) {
                        br++;
                        break;
                    }
                }
            }
        }
        if (br == rooms.size()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean availabilityForRoom(LocalDate from, LocalDate to, Room room1) {
        boolean availability = true;
        for (Room room : rooms) {
            if(room.getRoom() == room1.getRoom()) {
                ArrayList<Reservations> listOfReservations = room.getListOfLists();
                if (listOfReservations != null && !listOfReservations.isEmpty()) {
                    for (Reservations reservations : listOfReservations) {
                        if (from.isBefore(reservations.getDate2()) && to.isAfter(reservations.getDate1())) {
                            availability = false;
                        }
                    }
                }
            }
        }
        return availability;
    }

    public static void noOverlap(Room room, String note, LocalDate from, LocalDate to, int roomNumber,int guests) throws SuccessfulCheckin{
        ArrayList<Reservations> listDates = room.getListOfLists();
        int reservationId = room.getNextReservationId();
        listDates.add(new Reservations(reservationId, from,to, guests, note));
        room.setListOfLists(listDates);
        throw new SuccessfulCheckin(room, roomNumber);
    }

    public void checkin(int roomNumber, LocalDate from, LocalDate to, String note, int guests)
          throws RoomNotFoundException, TooManyGuestsException, BeforeFirstReservationException, BookedRoomException, SuccessfulCheckin {

        boolean roomFound = false;

        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                roomFound = true;
                if (availabilityForRoom(from, to, room)) {
                    if (guests == 0) {
                        guests = room.getBeds();
                    } else if (guests > room.getBeds()) {
                        throw new TooManyGuestsException();
                    }
                    boolean overlap = false;
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        for (Reservations reservations : room.getListOfLists()) {
                            if (!(to.isBefore(reservations.getDate1()) || from.isAfter(reservations.getDate2()))) {
                                overlap = true;
                                break;
                            } else if (from.isBefore(room.getListOfLists().get(0).getDate1())) {
                                throw new BeforeFirstReservationException();
                            }
                        }
                    }
                    if (overlap) {
                        throw new BookedRoomException(roomNumber);
                    }

                    if (!overlap) {
                        noOverlap(room, note, from, to, roomNumber,guests);
                    }
                } else {
                    throw new BookedRoomException(roomNumber);
                }
            }
        }
        if (!roomFound) {
            throw new RoomNotFoundException();
        }
    }

    public static boolean isDateAvailable(LocalDate date, Room room) {
        ArrayList<Reservations> listOfReservations = room.getListOfLists();
        if (listOfReservations != null && !listOfReservations.isEmpty()) {
            for (Reservations reservations : listOfReservations) {
                if (!(date.isAfter(reservations.getDate2()) || date.isBefore(reservations.getDate1()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void availability(LocalDate date) throws NoAvailableException {
        boolean availableRooms = false;
        for (Room room : rooms) {
            if (isDateAvailable(date, room)) {
                availableRooms = true;
                System.out.println("Room " + room.getRoom());
            }
        }
        if (!availableRooms) {
            throw new NoAvailableException();
        }
    }

    public void matchedDatesAndRoomsForReport(ArrayList<Room> listOfRooms, ArrayList<Reservations> matchedDates, LocalDate from, LocalDate to) {
        for (Room room : rooms) {
            ArrayList<Reservations> listOfReservations = room.getListOfLists();
            if (listOfReservations != null && !listOfReservations.isEmpty()) {
                for (Reservations reservations : listOfReservations) {
                    if (from.compareTo(reservations.getDate1()) >= 0 && to.compareTo(reservations.getDate2()) <= 0) {
                        matchedDates.add(reservations);
                        listOfRooms.add(room);
                    }
                }
            }
        }
    }

    public void report(LocalDate from, LocalDate to) throws NotMeetingRequirmentException {
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Reservations> matchedDates = new ArrayList<>();
        matchedDatesAndRoomsForReport(listOfRooms, matchedDates, from, to);
        for (int i = 0; i < listOfRooms.size(); i++) {
            int numberOfDays = (int) ChronoUnit.DAYS.between(matchedDates.get(i).getDate1(), matchedDates.get(i).getDate2());
            System.out.println("Room number: " + listOfRooms.get(i).getRoom() + ", Dates: " + matchedDates.get(i).getDate1()
                    + " " + matchedDates.get(i).getDate2() + ", Number of days: " + numberOfDays);
        }
        if (listOfRooms.isEmpty()) {
            throw new NotMeetingRequirmentException();
        }
    }

    public static boolean isRoomAvailableForDates(Room room, LocalDate from, LocalDate to) {
        ArrayList<Reservations> listOfReservations = room.getListOfLists();
        if (listOfReservations == null) {
            return true;
        }

        if (!listOfReservations.isEmpty()) {
            for (Reservations reservations : listOfReservations) {
                LocalDate date1 = reservations.getDate1();
                LocalDate date2 = reservations.getDate2();

                if ((from.isBefore(date2) || from.isEqual(date2)) && (to.isAfter(date1) || to.isEqual(date1))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void find(int beds, LocalDate from, LocalDate to) throws NoRoomWithBedsException {

        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room room1, Room room2) {
                return Integer.compare(room1.getBeds(), room2.getBeds());
            }
        });

        boolean bedsFound = false;
        for (Room room : rooms) {
            if (isRoomAvailableForDates(room, from, to) && room.getBeds() >= beds) {
                    System.out.println(room.getRoom());
                    bedsFound = true;
                    break;
            }
        }
        if (!bedsFound) {
            throw new NoRoomWithBedsException();
        }
    }

    public static boolean setUnavailableRoom(Room room, LocalDate from, LocalDate to, String note, boolean unavailableCheck, int roomNumber) {
        ArrayList<Reservations> listDates = room.getListOfLists();
        int reservationId = room.getNextReservationId();
        listDates.add(new Reservations(reservationId, from, to, room.getBeds(), note));
        room.setListOfLists(listDates);
        unavailableCheck = true;
        return unavailableCheck;
    }

    public void unavailable(int roomNumber, LocalDate from, LocalDate to, String note) throws BeforeFirstReservationException, RoomNotFoundException, BookedRoomException{
        boolean foundRoom = false;
        boolean unavailableCheck = false;
        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                foundRoom = true;
                if (isRoomAvailableForDates(room, from, to)) {
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        if (from.isBefore(room.getListOfLists().get(0).getDate1())) {
                            throw new BeforeFirstReservationException();
                        }else{
                            unavailableCheck = setUnavailableRoom(room, from, to, note, unavailableCheck, roomNumber);
                            System.out.println("Room " + roomNumber + " will be unavailable from " + from + " to " + to);
                        }
                    }
                }

            }
        }
        if (!foundRoom) {
            throw new RoomNotFoundException();
        } else if (!unavailableCheck) {
            throw new BookedRoomException(roomNumber);
        }
    }

    public static boolean daysToCheckout(Room room, int idReservation, boolean falseID, Scanner sc, int roomNumber) throws TooEarlyException{
        for (Reservations reservation : room.getListOfLists()) {
            if(reservation.getId() == idReservation) {
                falseID = true;
                System.out.println("Type how many days earlier you want to checkout: ");
                String days = sc.nextLine();
                int daysCount = Integer.parseInt(days);
                int numberOfDays = 0;
                numberOfDays = (int) ChronoUnit.DAYS.between(reservation.getDate1(), reservation.getDate2());
                if (numberOfDays > daysCount) {
                    LocalDate newEndDate = reservation.getDate2().minusDays(daysCount);
                    reservation.setDateTo(newEndDate);
                    System.out.println("You have checked out of room " + roomNumber);
                } else {
                    throw new TooEarlyException();
                }
            }
        }
        return falseID;
    }

    public void checkout(int roomNumber) throws RoomNotFoundException, TooEarlyException, FalseIdException {
        Scanner sc = new Scanner(System.in);
        boolean roomFound = false;
        boolean falseID = false;
        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                System.out.println("Type which reservation you want to checkout early: ");
                String id = sc.nextLine();
                int idReservation = Integer.parseInt(id);
                falseID = daysToCheckout(room, idReservation, falseID, sc, roomNumber);
                roomFound = true;
            }
        }
        if (!falseID){
            throw new FalseIdException();
        }
        if (!roomFound) {
            throw new RoomNotFoundException();
        }
    }

    public static void listOfRoomsWithMoreBedsThanGuests(ArrayList<Room> rooms,
         ArrayList<Reservations> listOfReservations, ArrayList<Room> listOfRooms, LocalDate from, LocalDate to) {
        for(Room room : rooms) {
            ArrayList<Reservations> listOfLists = room.getListOfLists();
            if (listOfLists != null && !listOfLists.isEmpty()) {
                for (Reservations reservations : listOfLists) {
                    if(from.compareTo(reservations.getDate1()) >= 0 && to.compareTo(reservations.getDate2()) <= 0) {
                        if(room.getBeds() > reservations.getGuests()) {
                            listOfRooms.add(room);
                            listOfReservations.add(reservations);
                        }
                    }
                }
            }
        }
    }

    public static Room getRoomByReservation(ArrayList<Room> rooms, Reservations res) {
        for (Room room : rooms) {
            if (room.getListOfLists().contains(res)) {
                return room;
            }
        }
        return null;
    }

    public static boolean MovedGuests(ArrayList<Room> listOfRooms, ArrayList<Reservations> listOfReservations, int beds, boolean moved) {
        for (Reservations res : listOfReservations) {
            Room room = getRoomByReservation(listOfRooms, res); // get the room for the current reservation
            if (room.getBeds() >= beds && res.getGuests() > 0) {
                for (Room otherRoom : listOfRooms) {
                    for (Reservations otherRes : otherRoom.getListOfLists()) {
                        if (otherRoom.getBeds() > room.getBeds() && otherRes.getGuests() < otherRoom.getBeds()) {
                            int availableSpace = otherRoom.getBeds() - otherRes.getGuests();
                            int guestsToMove = Math.min(availableSpace, res.getGuests());
                            otherRes.setGuests(otherRes.getGuests() + guestsToMove);
                            res.setGuests(res.getGuests() - guestsToMove);
                            res.setGuests(beds);
                            moved = true;
                            System.out.println("Moved " + guestsToMove + " guests from room " + room.getRoom() + " to room " + otherRoom.getRoom());
                            return moved;
                        }
                    }
                }
            }
        }
        return moved;
    }

    public void findVip(int beds, LocalDate from, LocalDate to) throws AvailableRoomsException, NoOptionAvailableException{
        boolean available = availability(from,to);
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Reservations> listOfReservations = new ArrayList<>();
        if(!available) {
            listOfRoomsWithMoreBedsThanGuests(rooms, listOfReservations, listOfRooms, from, to);
            boolean moved = false;
            moved = MovedGuests(listOfRooms, listOfReservations, beds, moved);
            if(!moved)
            {
                throw new NoOptionAvailableException();
            }
        } else {
            throw new AvailableRoomsException();
        }
    }
}