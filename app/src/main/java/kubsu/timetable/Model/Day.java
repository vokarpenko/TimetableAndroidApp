package kubsu.timetable.Model;
import java.io.Serializable;

public class Day implements Serializable {
    private String[] times;
    private String[] subjects;
    private String[] teachers;
    private String date;

    public String[] getTimes() {
        return times;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public String[] getTeachers() {
        return teachers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Day(String[] times, String[] subjects, String[] teachers, String date) {
        this.times = times;
        this.subjects = subjects;
        this.date = date;
        this.teachers=teachers;
    }
}