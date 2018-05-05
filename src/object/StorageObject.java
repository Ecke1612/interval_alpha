package object;

import java.time.LocalDate;

/**
 * Created by Eike on 20.05.2017.
 */
public class StorageObject {

    private int sec;
    private LocalDate date;
    private String comment;

    public StorageObject(LocalDate date, int seconds, String comment) {
        this.sec = seconds;
        this.date = date;
        this.comment = comment;
    }

    public void setSeconds(int seconds) {
        sec = seconds;
    }

    public int getSec() {
        return sec;
    }

    public int getMin() {
        int minutes = sec / 60;
        return minutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}