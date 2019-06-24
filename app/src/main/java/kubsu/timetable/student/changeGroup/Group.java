package kubsu.timetable.student.changeGroup;

import java.util.List;

public class Group{
    private String group;
    private List<String> subgroups;
    private String course;

    public String getGroup() {
        return group;
    }

    public List<String> getSubgroups() {
        return subgroups;
    }

    String getCourse() {
        return course;
    }

    public Group(String group, List<String> subgroups, String course) {
        this.group = group;
        this.subgroups = subgroups;
        this.course = course;
    }
}
