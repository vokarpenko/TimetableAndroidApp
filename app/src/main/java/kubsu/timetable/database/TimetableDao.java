package kubsu.timetable.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.student.GroupEntity;
import kubsu.timetable.timetable.Lesson;

@Dao
public interface TimetableDao {

    @Insert
    void insertAllLessons(List<Lesson> lessonList);

    @Query("SELECT * FROM Lesson WHERE Lesson.`group`=:group AND Lesson.subgroup=:subGroup AND Lesson.day=:day")
    List<Lesson> getLessonsDayStudent(String group, String subGroup, String day);

    @Query("SELECT * FROM Lesson WHERE Lesson.teacher=:teacher AND Lesson.day=:day")
    List<Lesson> getLessonsDayTeacher(String teacher, String day);

    @Insert
    void insertAllGroups(List<GroupEntity> groupEntities);

    @Query("SELECT * FROM GroupEntity")
    Single<List<GroupEntity>> getAllGroups();

    @Query("DELETE FROM Lesson")
    void deleteLessons();
}
