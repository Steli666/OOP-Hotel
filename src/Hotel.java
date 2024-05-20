import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Hotel {

    public static boolean availability(LocalDate from, LocalDate to, ArrayList<Room> rooms) {
        boolean availability = true;
        for (Room room : rooms) {
            ArrayList<Reservations> listOfreservations = room.getListOfLists();
            if (listOfreservations != null && !listOfreservations.isEmpty()) {
                for (Reservations reservations : listOfreservations) {
                    if (from.isBefore(reservations.getDate2()) && to.isAfter(reservations.getDate1())) {
                        availability = false;
                    }
                }
            }
        }
        return availability;
    }

    public static boolean availabilityForRoom(LocalDate from, LocalDate to, ArrayList<Room> rooms, Room room1) {
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

    public static void checkin(int roomNumber, LocalDate from, LocalDate to, String note, int guests, ArrayList<Room> rooms) {
        LocalDate today = LocalDate.now();

        if (from.isAfter(today) || to.isAfter(today)) {
            System.out.println("Cannot check in with dates in the future.");
            return;
        }
        boolean roomFound = false;

        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                roomFound = true;
                if (availabilityForRoom(from, to, rooms, room)) {
                    if (guests == 0) {
                        room.setGuests(room.getBeds());
                    } else {
                        if (guests > room.getBeds()) {
                            System.out.println("You can't check in more guests than there are beds");
                            return;
                        } else {
                            room.setGuests(guests);
                        }
                    }
                    boolean overlap = false;
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        for (Reservations reservations : room.getListOfLists()) {
                            if (!(to.isBefore(reservations.getDate1()) || from.isAfter(reservations.getDate2()))) {
                                overlap = true;
                                break;
                            } else if (from.isBefore(reservations.getDate1())) {
                                System.out.println("You cannot checkin before another reservation.");
                                return;
                            }
                        }
                    }
                    if (overlap) {
                        System.out.println("Cannot check in. Room " + roomNumber + " is already booked for part of this period.");
                        break;
                    }

                    if (!overlap) {
                        room.setFrom(from);
                        room.setTo(to);
                        room.setNote(note);

                        ArrayList<Reservations> listDates = room.getListOfLists();
                        listDates.add(new Reservations(from, to));
                        room.setListOfLists(listDates);

                        System.out.println("You have checked in room " + roomNumber);
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

    public static void availability(ArrayList<Room> rooms, LocalDate date) {
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

    public static void report(ArrayList<Room> rooms, LocalDate from, LocalDate to) {
        ArrayList<Room> listOfRooms = new ArrayList<>();
        ArrayList<Reservations> matchedDates = new ArrayList<>();
        //проверява дали датите на стаята са в периода подаден от потребителя
        //ако едната дата съвпада я вкарвам в арейлиста и ѝ взимам номера
        for (Room room : rooms) {
            ArrayList<Reservations> listOfreservations = room.getListOfLists();
            if (listOfreservations != null && !listOfreservations.isEmpty()) {
                //System.out.println(listOfLists);
                for (Reservations reservations : listOfreservations) {
                    //System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                    if (from.compareTo(reservations.getDate1()) >= 0 && to.compareTo(reservations.getDate2()) <= 0) {
                        matchedDates.add(reservations);
                        listOfRooms.add(room);
                    }
                }
            }
        }
        for (int i = 0; i < listOfRooms.size(); i++) {
            int numberOfDays = (int) ChronoUnit.DAYS.between(matchedDates.get(i).getDate1(), matchedDates.get(i).getDate2());
            System.out.println("Room number: " + listOfRooms.get(i).getRoom() + ", Dates: " + matchedDates.get(i).getDate1() + " " + matchedDates.get(i).getDate2() + ", Number of days: " + numberOfDays);
        }
        if (listOfRooms.isEmpty()) {
            System.out.println("There is no room that meets the requirement");
        }
        //for с индекси
        //1ви индекс на едното съвпада с 1ви на другия

    }

    private static boolean isRoomAvailableForDates(Room room, LocalDate from, LocalDate to) {
        ArrayList<Reservations> listOfLists = room.getListOfLists();
        if (listOfLists == null) {
            return true; // Treat as available if there are no booked dates
        }

        if (!listOfLists.isEmpty()) {
            for (Reservations reservations : listOfLists
            ) {
                LocalDate date1 = reservations.getDate1();
                LocalDate date2 = reservations.getDate2();
                // Check if there's any overlap between the date range and the room's booked dates
                if ((from.isBefore(date2) || from.isEqual(date2)) && (to.isAfter(date1) || to.isEqual(date1))) {
                    return false; // Room is booked for at least part of the requested period
                }
            }

        }
        return true; // Room is available for the entire requested period
    }

    public static void find(int beds, LocalDate from, LocalDate to, ArrayList<Room> rooms) {
        //сортирам стаите по легла тука преди фор-а

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

                if (room.getBeds() >= beds) {
                    System.out.println(room.getRoom());
                    bedsFound = true;
                    break;
                }
            }
        }
        if (!bedsFound) {
            System.out.println("There are no available rooms with this number of beds");
        }
    }

    public static void unavailable(int roomNumber, LocalDate from, LocalDate to, String note, ArrayList<Room> rooms) {
        //трябва да сложа вместо на първата дата на последната за да не се буква преди последната резервация

        boolean foundRoom = false;
        boolean unavailableCheck = false;
        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                foundRoom = true;
                if (isRoomAvailableForDates(room, from, to)) {
                    if (room.getListOfLists() != null && !room.getListOfLists().isEmpty()) {
                        if (from.isBefore(room.getTo())) {
                            System.out.println("You cannot checkin before another reservation.");
                            return;
                        }
                    }
                    room.setFrom(from);
                    room.setTo(to);
                    room.setNote(note);
                    ArrayList<Reservations> listDates = room.getListOfLists();
                    listDates.add(new Reservations(from, to));
                    room.setListOfLists(listDates);
                    unavailableCheck = true;
                    System.out.println("Room " + roomNumber + " will be unavailable from " + room.getFrom() + " to " + room.getTo());
                }
            }
        }
        if (!foundRoom) {
            System.out.println("Such room does not exist");
        } else if (!unavailableCheck) {
            System.out.println("The room is booked for this period");
        }
    }

    public static void checkout(int roomNumber, ArrayList<Room> rooms) {
        Scanner sc = new Scanner(System.in);
        boolean roomFound = false;
        for (Room room : rooms) {
            if (room.getRoom() == roomNumber) {
                System.out.println("Type how many days earlier you want to checkout: ");
                String days = sc.nextLine();
                int daysCount = Integer.parseInt(days);
                int numberOfDays = 0;
                if (room.getFrom() != null && room.getTo() !=null) {
                    numberOfDays = (int) ChronoUnit.DAYS.between(room.getFrom(), room.getTo());
                }else{
                    System.out.println("The room does not have reservations.");
                    return;
                }
                if (numberOfDays > daysCount) {
                    // Modify the end date of the last reservation
                    ArrayList<Reservations> listOfReservations = room.getListOfLists();
                    if (listOfReservations != null && !listOfReservations.isEmpty()) {
                        Reservations lastReservation = listOfReservations.get(listOfReservations.size() - 1);
                        LocalDate newEndDate = room.getTo().minusDays(daysCount);
                        lastReservation.setDateTo(newEndDate);
                        room.setTo(newEndDate);
                        System.out.println("You have checked out of room " + roomNumber);
                    }
                } else {
                    System.out.println("You cannot checkout this early.");
                }
                roomFound = true;
            }
        }
        if (!roomFound) {
            System.out.println("Such room does not exist.");
        }
    }

    public static void findVip(int beds, LocalDate from, LocalDate to, ArrayList<Room> rooms)
    {
        boolean available = availability(from,to,rooms);
        ArrayList<Room> listOfRooms = new ArrayList<>();
        if(!available) {
            for(Room room : rooms) {
                ArrayList<Reservations> listOfLists = room.getListOfLists();
                if (listOfLists != null && !listOfLists.isEmpty()) {
                    for (Reservations reservations : listOfLists) {
                        //System.out.println("From: " + dates.getDate1() + ", To: " + dates.getDate2());
                        if(from.compareTo(reservations.getDate1())>=0 && to.compareTo(reservations.getDate2())<=0) {
                            if(room.getBeds() > room.getGuests()) {
                                listOfRooms.add(room);
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
}