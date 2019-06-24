package kubsu.timetable.notes;

import java.util.List;

import kubsu.timetable.utility.ErrorToast;

interface NotesView extends ErrorToast {

    void setSubject();

    void setNotes(List<Note> notes);

    void setHintVisibility(int visibility);

    void addNote(Note note);
}
