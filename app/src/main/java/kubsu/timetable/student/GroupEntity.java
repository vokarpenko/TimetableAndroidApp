package kubsu.timetable.student;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Expose
    @SerializedName("nam_gruppa")
    private String group;
    @Expose
    @SerializedName("subgroup")
    private String subgroup;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

