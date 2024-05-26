import java.sql.ClientInfoStatus;
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

    public  boolean availability(LocalDate from, LocalDate to) {
        int br = 0;
        for (Room room : rooms) {
            ArrayList<Reservations> listOfReservations = room.getListOfLists();
            if (listOfReservations != null && !listOfReservations.isEmpty()) {
                for (Reservations reservations : listOfReservations) {
                    if (from.isBefore(reservations.getDate2()) && to.isAfter(reservations.getDate1())) {
                        br++;
                        break;
                    }
                }
            }
        }
        if (br == rooms.size()) {
            return false;
        }else {
            return true;
        }
    }

    public  boolean availabilityForRoom(LocalDate from, LocalDate to, Room room1) {
        boolean availability = true;
        for (Room room : rooms) {
            if(room.getRoom() == room1.getRoom()) {
                ArrayList<Reservations> listOfreservations = room.getListOfLists();
                if (listOfreservations != null && !listOfreservations.isEmpty()) {
                    for (Reservations reservations : listOfreservations) {
                        if (from.isBefore(reservations.getDate2()) && to.isAfter(reservations.getDate1())) {
                            availability = false;
                        }
                    }
                }
            }
        }
        return availability;
    }

    public static void noOverlap(Room room, String note, LocalDate from, LocalDate to, int roomNumber,int guests){
        ArrayList<Reservations> listDates = room.getListOfLists();
        int reservationId = room.getNextReservationId();
        listDates.add(new Reservations(reservationId,from, to,guests,note));
        room.setListOfLists(listDates);
        System.out.println("The id of the reservation: " + room.getListOfLists().get(room.getListOfLists().size() - 1).getId());
        System.out.println("You have checked in room " + roomNumber);
    }

    public void checkin(int roomNumber, LocalDate from, LocalDate to, String note, int guests) {

        boolean roomFound = false;

        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                roomFound = true;
                if (availabilityForRoom(from, to, room)) {
                    if (guests == 0) {
                        guests = room.getBeds();
                    } else {
                        if (guests > room.getBeds()) {
                            System.out.println("You can't check in more guests than there are beds");
                            return;
                        }
                    }
                    boolean overlap = false;
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        for (Reservations reservations : room.getListOfLists()) {
                            if (!(to.isBefore(reservations.getDate1()) || from.isAfter(reservations.getDate2()))) {
                                overlap = true;
                                break;

                            } else if (from.isBefore(room.getListOfLists().get(0).getDate1())) {
                                System.out.println("You cannot checkin before the first reservation.");
                                return;
                            }
                        }
                    }
                    if (overlap) {
                        System.out.println("Cannot check in. Room " + roomNumber + " is already booked for part of this period.");
                        break;
                    }

                    if (!overlap) {
                        noOverlap(room, note, from, to, roomNumber,guests);
                    }
                } else {
                    System.out.println("Cannot check in. Room " + roomNumber + " is already booked for this period.");
                }
            }
        }
        if (!roomFound) {
            System.out.println("Such room does not exist.");
        }
    }

    public static boolean isDateAvailable(LocalDate date, Room room) {
        ArrayList<Reservations> listOfReservations = room.getListOfLists();
        if (listOfReservations != null && !listOfReservations.isEmpty()) {
            for (Reservations reservations : listOfReservations) {
                if (!(date.isAfter(reservations.getDate2()) || date.isBefore(reservations.getDate1()))) {
                    // Date overlaps with existing reservation
                    return false;
                }
            }
        }
        return true;
    }

    public void availability(LocalDate date) {
        boolean availableRooms = false;
        System.out.println("Available rooms: ");
        for (Room room : rooms) {
            if (isDateAvailable(date, room)) {
                availableRooms = true;
                System.out.println("Room " + room.getRoom());
            }
        }
        if (!availableRooms) {
            System.out.println("There are no available rooms on this date");
        }
    }

    public void matchedDatesAndRoomsForReport(ArrayList<Room> listOfRooms,ArrayList<Reservations> matchedDates , LocalDate from, LocalDate to) {
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

    public void report(LocalDate from, LocalDate to) {
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Reservations> matchedDates = new ArrayList<>();
        matchedDatesAndRoomsForReport(listOfRooms, matchedDates, from, to);
        for (int i = 0; i < listOfRooms.size(); i++) {
            int numberOfDays = (int) ChronoUnit.DAYS.between(matchedDates.get(i).getDate1(), matchedDates.get(i).getDate2());
            System.out.println("Room number: " + listOfRooms.get(i).getRoom() + ", Dates: " + matchedDates.get(i).getDate1() + " " + matchedDates.get(i).getDate2() + ", Number of days: " + numberOfDays);
        }
        if (listOfRooms.isEmpty()) {
            System.out.println("There is no room that meets the requirement");
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

    public  void find(int beds, LocalDate from, LocalDate to) {

        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room room1, Room room2) {
                return Integer.compare(room1.getBeds(), room2.getBeds());
            }
        });

        System.out.println("Available rooms: ");
        boolean bedsFound = false;
        for (Room room : rooms) {
            if (isRoomAvailableForDates(room, from, to) && room.getBeds() >= beds) {
                    System.out.println(room.getRoom());
                    bedsFound = true;
                    break;
            }
        }
        if (!bedsFound) {
            System.out.println("There are no available rooms with this number of beds");
        }
    }

    public static boolean setUnavailableRoom(Room room, LocalDate from, LocalDate to, String note, boolean unavailableCheck, int roomNumber) {
        ArrayList<Reservations> listDates = room.getListOfLists();
        int reservationId = room.getNextReservationId();
        listDates.add(new Reservations(reservationId,from, to,room.getBeds(),note));
        room.setListOfLists(listDates);
        unavailableCheck = true;
//        System.out.println("Room " + roomNumber + " will be unavailable from " + from + " to " + to);
        return unavailableCheck;
    }

    public  void unavailable(int roomNumber, LocalDate from, LocalDate to, String note) {
        boolean foundRoom = false;
        boolean unavailableCheck = false;
        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                foundRoom = true;
                if (isRoomAvailableForDates(room, from, to)) {
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        if (from.isBefore(room.getListOfLists().get(0).getDate1())) {
                            System.out.println("You cannot checkin before the first reservation.");
                            return;
                        }else{
                            unavailableCheck = setUnavailableRoom(room, from,to,note,unavailableCheck,roomNumber);
                            System.out.println("Room " + roomNumber + " will be unavailable from " + from + " to " + to);
                        }
                    }
                }

            }
        }
        if (!foundRoom) {
            System.out.println("Such room does not exist");
        } else if (!unavailableCheck) {
            System.out.println("The room is booked for this period");
        }
    }

    public static boolean daysToCheckout(Room room, int idReservation, boolean falseID, Scanner sc, int roomNumber) {
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
                    System.out.println("You cannot checkout this early.");
                }
            }
        }
        return falseID;
    }

    public void checkout(int roomNumber) {
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
            System.out.println("There are no reservations with this id");
        }
        if (!roomFound) {
            System.out.println("Such room does not exist.");
        }
    }

    public static void listOfRoomsWithMoreBedsThanGuests(ArrayList<Room> rooms,
         ArrayList<Reservations> listOfReservations,ArrayList<Room> listOfRooms,LocalDate from, LocalDate to) {
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

    /*public static boolean MovedGuests(ArrayList<Room> listOfRooms,ArrayList<Reservations> listOfReservations, int beds, boolean  moved){
        /*for (Room room : listOfRooms) {
            if (room.getBeds() >= beds) {
                for (Room otherRoom : listOfRooms) {
                    if (otherRoom.getBeds() > room.getBeds() && otherRoom.getGuests() < otherRoom.getBeds()) {
                        int availableSpace = otherRoom.getBeds() - otherRoom.getGuests();
                        int guestsToMove = Math.min(availableSpace, room.getGuests());
                        otherRoom.setGuests(otherRoom.getGuests() + guestsToMove);
                        room.setGuests(room.getGuests() - guestsToMove);
                        moved = true;
                        System.out.println("Moved " + guestsToMove + " guests from room " + room.getRoom() + " to room " + otherRoom.getRoom());
                        return moved;
                    }
                }
            }
        }
        for (int i=0; i<listOfRooms.size(); i++) {
            if (listOfRooms.get(i).getBeds() >= beds) {
                for (int j=0; j<listOfRooms.size(); j++) {
                    if (listOfRooms.get(j).getBeds() > listOfRooms.get(i).getBeds() && listOfReservations.get(j).getGuests() < listOfRooms.get(j).getBeds()) {
                        int availableSpace = listOfRooms.get(j).getBeds() - listOfReservations.get(j).getGuests();
                        int guestsToMove = Math.min(availableSpace, listOfReservations.get(i).getGuests());
                        listOfReservations.get(j).setGuests(listOfReservations.get(j).getGuests() + guestsToMove);
                        listOfReservations.get(i).setGuests(listOfReservations.get(i).getGuests() - guestsToMove);
                        moved = true;
                        System.out.println("Moved " + guestsToMove + " guests from room " + listOfRooms.get(i).getRoom() + " to room " + listOfRooms.get(j).getRoom());
                        return moved;
                    }

                }
            }
        }
        return moved;
    }*/
    public static boolean MovedGuests(ArrayList<Room> listOfRooms, ArrayList<Reservations> listOfReservations, int beds, boolean moved) {
        for (Reservations res : listOfReservations) {
            Room room = getRoomByReservation(listOfRooms, res); // Retrieve the room for the current reservation
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

    public static Room getRoomByReservation(ArrayList<Room> rooms, Reservations res) {
        for (Room room : rooms) {
            if (room.getListOfLists().contains(res)) {
                return room;
            }
        }
        return null;
    }

    public void findVip(int beds, LocalDate from, LocalDate to) {
        boolean available = availability(from,to);
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Reservations> listOfReservations = new ArrayList<>();
        if(!available) {
            listOfRoomsWithMoreBedsThanGuests(rooms,listOfReservations,listOfRooms,from,to);
            boolean moved = false;
            moved = MovedGuests(listOfRooms,listOfReservations, beds, moved);
            if(!moved)
            {
                System.out.println("There's nothing that can be done");
            }
        } else {
            System.out.println("There are available rooms on these dates");
        }
    }
}