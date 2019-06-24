package kubsu.timetable.api;

import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.notes.Note;
import kubsu.timetable.login.LoginRepository;
import kubsu.timetable.registration.RegistrationRepository;
import kubsu.timetable.student.GroupEntity;
import kubsu.timetable.student.StudentRepository;
import kubsu.timetable.timetable.Lesson;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static kubsu.timetable.utility.Constant.ADD_NEW_NOTE_URL;
import static kubsu.timetable.utility.Constant.GET_ALL_CLASSES_URL;
import static kubsu.timetable.utility.Constant.LIST_GROUP_URL;
import static kubsu.timetable.utility.Constant.LIST_NOTES_URL;
import static kubsu.timetable.utility.Constant.LOGIN_URL;
import static kubsu.timetable.utility.Constant.REGISTER_URL;
import static kubsu.timetable.utility.Constant.USER_INFO_URL;

public interface ApiTimetable {
    @GET(GET_ALL_CLASSES_URL)
    Single<List<Lesson>> getAllClasses();

    @FormUrlEncoded
    @POST(REGISTER_URL)
    Single<RegistrationRepository.ResponseRegister> register( @Field("SignUpForm") String jsonSignUp);

    @FormUrlEncoded
    @POST(LOGIN_URL)
    Single<LoginRepository.ResponseLogin> login(@Field("loginForm") String jsonLogin);

    @GET(USER_INFO_URL)
    Single<StudentRepository.UserInfo> getUserInfo(@Header("Authorization") String token);

    @GET(LIST_GROUP_URL)
    Single<List<GroupEntity>> getListGroup();

    @FormUrlEncoded
    @POST(ADD_NEW_NOTE_URL)
    Single<LoginRepository.ResponseLogin> addNote(@Header("Authorization") String token, @Field("Notes") String jsonNote);

    @FormUrlEncoded
    @POST(LIST_NOTES_URL)
    Single<List<Note>> getNotes(@Header("Authorization") String token, @Field("Notes") String jsonNote);
}

