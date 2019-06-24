package kubsu.timetable.timetable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kubsu.timetable.notes.NotesActivity;
import kubsu.timetable.R;
import kubsu.timetable.utility.L;

import static kubsu.timetable.utility.Constant.EXTRA_NOTES_SUBJECT;
import static kubsu.timetable.utility.Constant.EXTRA_NOTES_TIMESTAMP;

public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Day> days;
    int timetableType;

    TimetableAdapter(int timetableType) {
        this.timetableType = timetableType;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView time1;
        TextView time2;
        TextView time3;
        TextView time4;
        TextView time5;
        TextView time6;
        TextView subject1;
        TextView subject2;
        TextView subject3;
        TextView subject4;
        TextView subject5;
        TextView subject6;
        TextView teacher1;
        TextView teacher2;
        TextView teacher3;
        TextView teacher4;
        TextView teacher5;
        TextView teacher6;
        TextView date;
        RelativeLayout lesson0, lesson1, lesson2, lesson3,lesson4,lesson5;
        CardView itemCardLayout;

        DayViewHolder(final View itemView) {
            super(itemView);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);
            time3 = itemView.findViewById(R.id.time3);
            time4 = itemView.findViewById(R.id.time4);
            time5 = itemView.findViewById(R.id.time5);
            time6 = itemView.findViewById(R.id.time6);
            subject1 = itemView.findViewById(R.id.subject1);
            subject2 = itemView.findViewById(R.id.subject2);
            subject3 = itemView.findViewById(R.id.subject3);
            subject4 = itemView.findViewById(R.id.subject4);
            subject5 = itemView.findViewById(R.id.subject5);
            subject6 = itemView.findViewById(R.id.subject6);
            teacher1 = itemView.findViewById(R.id.teacher1);
            teacher2 = itemView.findViewById(R.id.teacher2);
            teacher3 = itemView.findViewById(R.id.teacher3);
            teacher4 = itemView.findViewById(R.id.teacher4);
            teacher5 = itemView.findViewById(R.id.teacher5);
            teacher6 = itemView.findViewById(R.id.teacher6);
            date = itemView.findViewById(R.id.date);
            itemCardLayout = itemView.findViewById(R.id.item_cardview_timetable_student);
            lesson0 = itemView.findViewById(R.id.line1);
            lesson1 = itemView.findViewById(R.id.line2);
            lesson2 = itemView.findViewById(R.id.line3);
            lesson3 = itemView.findViewById(R.id.line4);
            lesson4 = itemView.findViewById(R.id.line5);
            lesson5 = itemView.findViewById(R.id.line6);
        }
    }

    static class FreeDayViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        CardView itemCardLayout;
        FreeDayViewHolder(final View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.free_date);
            itemCardLayout = itemView.findViewById(R.id.item_cardview_freeday);
        }
    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case 0:
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_freeday, viewGroup, false);
            return new FreeDayViewHolder(v);
            case 1:
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_student, viewGroup, false);
            return new DayViewHolder(v);
            default:
                return new FreeDayViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_freeday, viewGroup, false));
        }
    }

    private void setListener(final RelativeLayout lesson, final int position, final int lessonNumber){
        lesson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NotesActivity.class);
                TextView subjectTextView = lesson.findViewWithTag("subject");
                if (!subjectTextView.getText().toString().equals("")) {
                    intent.putExtra(EXTRA_NOTES_SUBJECT, days.get(position).getLessons().get(lessonNumber).getSubject());
                    intent.putExtra(EXTRA_NOTES_TIMESTAMP,days.get(position).getLessons().get(lessonNumber).getTimeStamp());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        Date date = new Date();
        String currentDate = simpleDateFormat.format(date);
        switch (viewHolder.getItemViewType()) {
            case 1:
                DayViewHolder dayViewHolder = (DayViewHolder) viewHolder;
                YoYo.with(Techniques.FadeIn).playOn(dayViewHolder.itemCardLayout);
                //Listeners
                setListener(dayViewHolder.lesson0,dayViewHolder.getAdapterPosition(),0);
                setListener(dayViewHolder.lesson1,dayViewHolder.getAdapterPosition(),1);
                setListener(dayViewHolder.lesson2,dayViewHolder.getAdapterPosition(),2);
                setListener(dayViewHolder.lesson3,dayViewHolder.getAdapterPosition(),3);
                if (timetableType==0){
                    dayViewHolder.lesson4.setVisibility(View.GONE);
                    dayViewHolder.lesson5.setVisibility(View.GONE);
                }
                /*if (timetableType==1){
                    dayViewHolder.lesson4.setVisibility(View.VISIBLE);
                    dayViewHolder.lesson5.setVisibility(View.VISIBLE);
                }*/
                setListener(dayViewHolder.lesson4,dayViewHolder.getAdapterPosition(),4);
                setListener(dayViewHolder.lesson5,dayViewHolder.getAdapterPosition(),5);
                if (days.get(i).getDate().equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).getDate();
                    dayViewHolder.date.setText(now);
                } else dayViewHolder.date.setText(days.get(i).getDate());
                switch (days.get(i).getLessons().size()){
                    case 1:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        break;
                    case 2:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        break;
                    case 3:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        dayViewHolder.time3.setText(days.get(i).getLessons().get(2).getTime());
                        dayViewHolder.subject3.setText(days.get(i).getLessons().get(2).getSubject());
                        dayViewHolder.teacher3.setText(days.get(i).getLessons().get(2).getTeacher());
                        break;
                    case 4:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        dayViewHolder.time3.setText(days.get(i).getLessons().get(2).getTime());
                        dayViewHolder.subject3.setText(days.get(i).getLessons().get(2).getSubject());
                        dayViewHolder.teacher3.setText(days.get(i).getLessons().get(2).getTeacher());
                        dayViewHolder.time4.setText(days.get(i).getLessons().get(3).getTime());
                        dayViewHolder.subject4.setText(days.get(i).getLessons().get(3).getSubject());
                        dayViewHolder.teacher4.setText(days.get(i).getLessons().get(3).getTeacher());
                        break;
                    case 5:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        dayViewHolder.time3.setText(days.get(i).getLessons().get(2).getTime());
                        dayViewHolder.subject3.setText(days.get(i).getLessons().get(2).getSubject());
                        dayViewHolder.teacher3.setText(days.get(i).getLessons().get(2).getTeacher());
                        dayViewHolder.time4.setText(days.get(i).getLessons().get(3).getTime());
                        dayViewHolder.subject4.setText(days.get(i).getLessons().get(3).getSubject());
                        dayViewHolder.teacher4.setText(days.get(i).getLessons().get(3).getTeacher());
                        dayViewHolder.time5.setText(days.get(i).getLessons().get(4).getTime());
                        dayViewHolder.subject5.setText(days.get(i).getLessons().get(4).getSubject());
                        dayViewHolder.teacher5.setText(days.get(i).getLessons().get(4).getTeacher());
                        break;
                    case 6:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        dayViewHolder.time3.setText(days.get(i).getLessons().get(2).getTime());
                        dayViewHolder.subject3.setText(days.get(i).getLessons().get(2).getSubject());
                        dayViewHolder.teacher3.setText(days.get(i).getLessons().get(2).getTeacher());
                        dayViewHolder.time4.setText(days.get(i).getLessons().get(3).getTime());
                        dayViewHolder.subject4.setText(days.get(i).getLessons().get(3).getSubject());
                        dayViewHolder.teacher4.setText(days.get(i).getLessons().get(3).getTeacher());
                        dayViewHolder.time5.setText(days.get(i).getLessons().get(4).getTime());
                        dayViewHolder.subject5.setText(days.get(i).getLessons().get(4).getSubject());
                        dayViewHolder.teacher5.setText(days.get(i).getLessons().get(4).getTeacher());
                        dayViewHolder.time6.setText(days.get(i).getLessons().get(5).getTime());
                        dayViewHolder.subject6.setText(days.get(i).getLessons().get(5).getSubject());
                        dayViewHolder.teacher6.setText(days.get(i).getLessons().get(5).getTeacher());
                        break;
                }
                break;
            case 0:
                final FreeDayViewHolder freeDayViewHolder = (FreeDayViewHolder) viewHolder;
                YoYo.with(Techniques.FadeIn).playOn(freeDayViewHolder.itemCardLayout);
                if (days.get(i).getDate().equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).getDate();
                    freeDayViewHolder.date.setText(now);
                } else freeDayViewHolder.date.setText(days.get(i).getDate());
                freeDayViewHolder.itemCardLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        YoYo.with(Techniques.Hinge).playOn(freeDayViewHolder.itemCardLayout);
                        Toast.makeText(view.getContext(),"Теперь у вас нет выходного",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int i) {
        if (days.get(i).getLessons().size()==0) return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    void setDays(List<Day> days){
        this.days=days;
        notifyDataSetChanged();
    }
}
