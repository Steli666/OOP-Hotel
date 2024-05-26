import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {

    private int room; //room number
    private int beds; //bed number
    private ArrayList<Reservations> listOfLists;
    private int reservationCounter;

    public void setListOfLists(ArrayList<Reservations> listOfLists) {
        this.listOfLists = listOfLists;
    }
    public int getNextReservationId() {
        return ++reservationCounter; // increment and return the counter
    }
    public ArrayList<Reservations> getListOfLists() {
        return this.listOfLists;
    }
    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {
        if(room > 0) {
            this.room = room;
        } else {
            System.out.println("Invalid room number (there can't be a room with a negative number)");
        }
    }
    public int getBeds() {
        return beds;
    }
    public void setBeds(int beds) {
        this.beds = beds;
    }
    Room(int room, String note, int guests, int beds) {
        setRoom(room);
        setBeds(beds);
        listOfLists = new ArrayList<>();
        reservationCounter = 0;
    }
}