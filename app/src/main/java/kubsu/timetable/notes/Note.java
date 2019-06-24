package kubsu.timetable.notes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {
    Note(String name, String surname, String noteText,String  date) {
        this.name = name;
        this.surname = surname;
        this.noteText = noteText;
        this.date = date;
    }

    @SerializedName("createdAt")
    @Expose
    private long timestamp;

    private String date;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("message")
    @Expose
    private String noteText;

    public String getAuthor() {
        return surname+" "+name.charAt(0)+".";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
