package tran.compbuildbackend.domain.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtility {

    public static LocalDate convertStringToDate(String date) {
        // used to persist the date into the database
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static String convertDateToString(LocalDate date) {
        DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return secondFormatter.format(date);
    }
}
