import java.time.LocalDate;

public class Room
{
    private int room; //room number
    private String note;
    private int guests; //guest number
    private int beds; //bed number
    private LocalDate from; //first date yyyy-MM-dd
    private LocalDate to; //final date
    private boolean availability;

    public boolean isAvailability() {
        return availability;
    }
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {

        if(room > 0)
        {
            this.room = room;
        }
        else
        {
            System.out.println("Invalid room number");
        }
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public int getGuests() {
        return guests;
    }
    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getBeds() {
        return beds;
    }
    public void setBeds(int beds) {
        this.beds = beds;
    }

    public LocalDate getFrom() {
        return from;
    }
    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }
    public void setTo(LocalDate to) {
        this.to = to;
    }

    Room(int room, String note, int guests, int beds)
    {
        setRoom(room);
        setNote(note);
        setGuests(guests);
        setBeds(beds);
//        setFrom(from);
//        setTo(to);
        setAvailability(true);
    }
}