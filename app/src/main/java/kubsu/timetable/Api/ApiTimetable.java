package kubsu.timetable.Api;

import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.Login.LoginRepository;
import kubsu.timetable.Registration.RegistrationRepository;
import kubsu.timetable.Student.GroupEntity;
import kubsu.timetable.Student.TimeTable.Lesson;
import kubsu.timetable.Student.StudentRepository;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static kubsu.timetable.Utility.Constant.GET_ALL_CLASSES_URL;
import static kubsu.timetable.Utility.Constant.LIST_GROUP_URL;
import static kubsu.timetable.Utility.Constant.LOGIN_URL;
import static kubsu.timetable.Utility.Constant.REGISTER_URL;

public interface ApiTimetable {
    @GET(GET_ALL_CLASSES_URL)
    Single<List<Lesson>> getAllClasses();

    @FormUrlEncoded
    @POST(REGISTER_URL)
    Single<RegistrationRepository.ResponseRegister> register( @Field("SignUpForm") String jsonSignUp);

    @FormUrlEncoded
    @POST(LOGIN_URL)
    Single<LoginRepository.ResponseLogin> login(@Field("loginForm") String jsonLogin);

    @GET("/rest/admins")
    Single<LoginRepository.Test> test(@Header("Authorization") String token);

    @GET(LIST_GROUP_URL)
    Single<List<GroupEntity>> getListGroup();

}
