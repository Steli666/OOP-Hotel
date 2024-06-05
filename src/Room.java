import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {

    private int roomNumber; //room number
    private int beds; //bed number
    private ArrayList<Reservations> listOfLists;
    private int reservationCounter;

    public void setListOfLists(ArrayList<Reservations> listOfLists) {
        this.listOfLists = listOfLists;
    }
    public int getNextReservationId() {
        return ++reservationCounter;
    }
    public ArrayList<Reservations> getListOfLists() {
        return this.listOfLists;
    }
    public int getRoom() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getBeds() {
        return beds;
    }
    public void setBeds(int beds) {
        this.beds = beds;
    }
    Room(int roomNumber, int beds) {
        setRoomNumber(roomNumber);
        setBeds(beds);
        listOfLists = new ArrayList<>();
        reservationCounter = 0;
    }
}