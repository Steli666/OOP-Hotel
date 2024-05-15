import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Dates implements Serializable
{
        private LocalDate dateFrom;
        private LocalDate dateTo;

        public Dates(LocalDate date1, LocalDate date2)
        {
            this.dateFrom = date1;
            this.dateTo = date2;
        }

        public LocalDate getDate1()
        {
            return dateFrom;
        }

        public LocalDate getDate2()
        {
            return dateTo;
        }
}