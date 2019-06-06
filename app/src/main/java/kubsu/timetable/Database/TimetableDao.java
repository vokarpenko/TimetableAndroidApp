package kubsu.timetable.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.Student.GroupEntity;
import kubsu.timetable.Student.TimeTable.Lesson;

@Dao
public interface TimetableDao {

    @Insert
    void insertAllLessons(List<Lesson> lessonList);

    @Query("SELECT * FROM Lesson WHERE Lesson.`group`=:group AND Lesson.subgroup=:subGroup AND Lesson.day=:day")
    List<Lesson> getLessonsDay(String group,String subGroup,String day);

    @Insert
    void insertAllGroups(List<GroupEntity> groupEntities);

    @Query("SELECT * FROM GroupEntity")
    Single<List<GroupEntity>> getAllGroups();
}
