package object;

import java.util.ArrayList;

public class TimeProCommentObject {

    private String comment;
    private int time;

    public TimeProCommentObject(String comment, int time) {
        this.comment = comment;
        this.time = time;
    }

    public void addTime(int min) {
        time += min;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
