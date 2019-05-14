package object;

public class TodoStorage {

    private String type;
    private String notes;
    private boolean check;
    private String text = "";
    private String rowCount;

    public TodoStorage(String type, String notes, int rowCount) {
        this.type = type;
        this.notes = notes;
        this.rowCount = Integer.toString(rowCount);
    }

    public TodoStorage(String type, boolean check, String text){
        this.check = check;
        this.text = text;
        this.type = type;
        if(text.equals("")) {
            System.out.println("nix drin");
        }
        System.out.println("text: " + text);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRowCount() {
        return Integer.parseInt(rowCount);
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }
}
