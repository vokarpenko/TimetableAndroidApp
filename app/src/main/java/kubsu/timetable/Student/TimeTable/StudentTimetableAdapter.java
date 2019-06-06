package kubsu.timetable.Student.TimeTable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kubsu.timetable.LessonNotes.NotesActivity;
import kubsu.timetable.R;
import kubsu.timetable.Utility.L;

public class StudentTimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Day> days;

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView time1;
        TextView time2;
        TextView time3;
        TextView time4;
        TextView subject1;
        TextView subject2;
        TextView subject3;
        TextView subject4;
        TextView teacher1;
        TextView teacher2;
        TextView teacher3;
        TextView teacher4;
        TextView date;
        RelativeLayout lesson1,lesson2,lesson3,lesson4;
        CardView itemCardLayout;

        DayViewHolder(final View itemView) {
            super(itemView);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);
            time3 = itemView.findViewById(R.id.time3);
            time4 = itemView.findViewById(R.id.time4);
            subject1 = itemView.findViewById(R.id.subject1);
            subject2 = itemView.findViewById(R.id.subject2);
            subject3 = itemView.findViewById(R.id.subject3);
            subject4 = itemView.findViewById(R.id.subject4);
            teacher1 = itemView.findViewById(R.id.teacher1);
            teacher2 = itemView.findViewById(R.id.teacher2);
            teacher3 = itemView.findViewById(R.id.teacher3);
            teacher4 = itemView.findViewById(R.id.teacher4);
            date = itemView.findViewById(R.id.date);
            itemCardLayout = itemView.findViewById(R.id.item_cardview_timetable);
            lesson1 = itemView.findViewById(R.id.line1);
            lesson2 = itemView.findViewById(R.id.line2);
            lesson3 = itemView.findViewById(R.id.line3);
            lesson4 = itemView.findViewById(R.id.line4);
            lesson1.setOnClickListener(getListener());
            lesson2.setOnClickListener(getListener());
            lesson3.setOnClickListener(getListener());
            lesson4.setOnClickListener(getListener());
        }

        View.OnClickListener getListener(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(),NotesActivity.class));
                }
            };
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        Date date = new Date();
        String currentDate = simpleDateFormat.format(date);
        switch (viewHolder.getItemViewType()) {
            case 1:
                DayViewHolder dayViewHolder = (DayViewHolder) viewHolder;
                YoYo.with(Techniques.FadeIn).playOn(dayViewHolder.itemCardLayout);
                if (days.get(i).getDate().equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).getDate();
                    dayViewHolder.date.setText(now);
                } else dayViewHolder.date.setText(days.get(i).getDate());

                switch (days.get(i).getLessons().size()){
                    case 2:
                        dayViewHolder.time1.setText(days.get(i).getLessons().get(0).getTime());
                        dayViewHolder.subject1.setText(days.get(i).getLessons().get(0).getSubject());
                        dayViewHolder.teacher1.setText(days.get(i).getLessons().get(0).getTeacher());
                        dayViewHolder.time2.setText(days.get(i).getLessons().get(1).getTime());
                        dayViewHolder.subject2.setText(days.get(i).getLessons().get(1).getSubject());
                        dayViewHolder.teacher2.setText(days.get(i).getLessons().get(1).getTeacher());
                        dayViewHolder.time3.setText("");
                        dayViewHolder.subject3.setText("");
                        dayViewHolder.teacher3.setText("");
                        dayViewHolder.time4.setText("");
                        dayViewHolder.subject4.setText("");
                        dayViewHolder.teacher4.setText("");
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
                        dayViewHolder.time4.setText("");
                        dayViewHolder.subject4.setText("");
                        dayViewHolder.teacher4.setText("");
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
                }
                break;
            case 0:
                FreeDayViewHolder freeDayViewHolder = (FreeDayViewHolder) viewHolder;
                YoYo.with(Techniques.FadeIn).playOn(freeDayViewHolder.itemCardLayout);
                if (days.get(i).getDate().equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).getDate();
                    freeDayViewHolder.date.setText(now);
                } else freeDayViewHolder.date.setText(days.get(i).getDate());
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
