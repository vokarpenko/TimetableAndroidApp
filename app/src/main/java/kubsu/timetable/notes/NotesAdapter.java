package kubsu.timetable.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import kubsu.timetable.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<Note> noteList;

    NotesAdapter() {
        this.noteList = new ArrayList<>();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView textNote;
        TextView timeNote;
        TextView authorNote;

        NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textNote=itemView.findViewById(R.id.note_text);
            authorNote=itemView.findViewById(R.id.note_author);
            timeNote = itemView.findViewById(R.id.note_time);
        }
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_note, viewGroup, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder viewHolder, int i) {
        viewHolder.authorNote.setText(noteList.get(i).getAuthor());
        viewHolder.textNote.setText(noteList.get(i).getNoteText());
        viewHolder.timeNote.setText(String.valueOf(noteList.get(i).getDate()));
        YoYo.with(Techniques.FadeIn).playOn(viewHolder.itemView);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    void addNote(Note note){
        noteList.add(note);
        notifyDataSetChanged();
    }

    void setNotes(List<Note> notes){
        noteList.clear();
        noteList.addAll(notes);
        notifyDataSetChanged();
    }
}
