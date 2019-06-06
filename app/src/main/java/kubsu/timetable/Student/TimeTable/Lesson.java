package kubsu.timetable.Student.TimeTable;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("class")
    @Expose
    private Integer classNumber;
    @SerializedName("teacher")
    @Expose
    private String teacher;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("subgroup")
    @Expose
    private String subgroup;
    @SerializedName("subject")
    @Expose
    private String subject;

    @Ignore
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
