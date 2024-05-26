import java.io.Serializable;
import java.time.LocalDate;

public class Reservations implements Serializable {
    private int guests;
    private int id = 0;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String note;

    public Reservations(int id,LocalDate date1, LocalDate date2, int guests,String note) {
        this.id = id;
        this.dateFrom = date1;
        this.dateTo = date2;
        this.guests = guests;
        this.note = note;
    }

    public LocalDate getDate1() {
        return dateFrom;
    }

    public LocalDate getDate2() {
        return dateTo;
    }
    public int getId() {
        return id;
    }
    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
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
}