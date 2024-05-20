import java.io.Serializable;
import java.time.LocalDate;

public class Reservations implements Serializable {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Reservations(LocalDate date1, LocalDate date2) {
        this.dateFrom = date1;
        this.dateTo = date2;
    }

    public LocalDate getDate1() {
        return dateFrom;
    }

    public LocalDate getDate2() {
        return dateTo;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}