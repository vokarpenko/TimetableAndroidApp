package kubsu.timetable.start;

interface StartView {
    void openTeacherActivity();
    void openStudentActivity();
    void openLoginActivity(int LoginType);
    void setHelloImage(String text, int resourceId);
}
