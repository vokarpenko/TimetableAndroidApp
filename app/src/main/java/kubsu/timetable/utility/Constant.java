package kubsu.timetable.utility;

public class Constant {

    //SETTING PREF
    public static final String PREF_FILE = "setting";
    public static final String PREF_GROUP = "group";
    public static final String PREF_SUBGROUP = "subgroup";
    public static final String PREF_TEACHER = "teacher";
    public static final String PREF_AUTH_TOKEN = "auth_token";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_REMEMBER_ME = "rememberMe";
    public static final String PREF_DATA_LESSONS = "prefDataLessons";
    public static final String PREF_DATA_GROUPS = "prefDataGroups";
    public static final String PREF_HAS_VISITED ="has_visited";
    public static final String PREF_USER_NAME ="user_name";
    public static final String PREF_USER_SURNAME ="user_surname";
    public static final String PREF_USER_PATRONYMIC ="user_patronymic";
    public static final String PREF_USER_EMAIL ="user_email";
    public static final String PREF_USER_STATUS = "user_status";

    //URL
    public static final String BASE_URL = "http://yii.timetable-fktpm.ru";
    public static final String GET_ALL_CLASSES_URL ="/admins/tpara/get-all";
    public static final String REGISTER_URL ="/site/signup-mobile";
    public static final String LOGIN_URL = "/rest/login";
    public static final String LIST_GROUP_URL = "/admins/gruppa/get-all";
    public static final String NEWS_URL = "https://www.kubsu.ru/ru/news?page=";
    public static final String KUBSU_URL = "https://www.kubsu.ru";
    public static final String ADD_NEW_NOTE_URL ="/rest/create-note";
    public static final String LIST_NOTES_URL = "/rest/get-notes";
    public static final String USER_INFO_URL = "/rest/get-user";

    //STRING
    public static final String INTERNET_ERROR = "Ошибка соединения";

    //EXTRAS
    public static final String EXTRA_NOTES_DATE = "extraNotesDate";
    public static final String EXTRA_NOTES_SUBJECT = "extraNotesSubject";
    public static final String EXTRA_NOTES_TIMESTAMP = "extraNotesTimestamp";
    public static final String EXTRA_LOGIN_TYPE = "extraLoginType";
    public static final String EXTRA_TIMETABLE_TYPE = "extraTimetableType";



}
