package kubsu.timetable.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import kubsu.timetable.R;

import static kubsu.timetable.utility.Constant.EXTRA_NOTES_SUBJECT;
import static kubsu.timetable.utility.Constant.EXTRA_NOTES_TIMESTAMP;

public class NotesActivity extends AppCompatActivity implements NotesView {
    private NotesPresenter presenter;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private TextView textHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        if (presenter==null) presenter = new NotesPresenter(this,new NotesRepository(this));
        init();
        presenter.setData(getTimeStamp());
    }

    private void init() {
        textHint = findViewById(R.id.notes_hint);
        recyclerView =  findViewById(R.id.rv_notes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);
        initToolbar();
        FloatingActionButton fabAddNote = findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.fabAddNoteClick(getTimeStamp());
            }
        });
    }

    @Override
    public void setSubject() {
        setTitle(getIntent().getStringExtra(EXTRA_NOTES_SUBJECT));
    }

    @Override
    public void setNotes(List<Note> notes) {
        adapter.setNotes(notes);
    }

    @Override
    public void setHintVisibility(int visibility) {
        YoYo.with(Techniques.FadeIn).playOn(textHint);
        textHint.setVisibility(visibility);
    }

    @Override
    public void addNote(Note note) {
        adapter.addNote(note);
    }

    private long getTimeStamp() {
        return getIntent().getLongExtra(EXTRA_NOTES_TIMESTAMP,0);
    }

    private void initToolbar(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }
}
