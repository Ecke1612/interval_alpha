package object;

public class ReminderObject {

    private String text;
    private int hour;
    private int min;

    public ReminderObject(String text, int hour, int min) {
        this.text = text;
        this.hour = hour;
        this.min = min;
    }

    public String getText() {
        return text;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }
}
