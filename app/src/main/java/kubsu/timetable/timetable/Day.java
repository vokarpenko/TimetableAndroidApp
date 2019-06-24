package kubsu.timetable.timetable;

import java.util.List;

public class Day  {

    private List<Lesson> lessons;
    private String date;

    List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    Day(List<Lesson> lessons, String date) {
        this.lessons = lessons;
        this.date = date;
    }
}
