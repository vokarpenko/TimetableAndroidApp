package kubsu.timetable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RVStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Day> days;

    RVStudentAdapter(List<Day> days){
        this.days = days;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
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
        RelativeLayout itemCardLayout;

        DayViewHolder(final View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
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
            itemCardLayout = itemView.findViewById(R.id.relative_layout_itemcard);
        }
    }

    static class FreeDayViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        FreeDayViewHolder(final View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.free_date);
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
                return new FreeDayViewHolder(null);
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
                if (days.get(i).date.equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).date;
                    dayViewHolder.date.setText(now);
                } else dayViewHolder.date.setText(days.get(i).date);
                try {
                    dayViewHolder.time1.setText(days.get(i).getTimes()[0]);
                } catch (Exception e) {
                    dayViewHolder.time1.setText("");
                }
                try {
                    dayViewHolder.time2.setText(days.get(i).getTimes()[1]);
                } catch (Exception e) {
                    dayViewHolder.time2.setText("");
                }
                try {
                    dayViewHolder.time3.setText(days.get(i).getTimes()[2]);
                } catch (Exception e) {
                    dayViewHolder.time3.setText("");
                }
                try {
                    dayViewHolder.time4.setText(days.get(i).getTimes()[3]);
                } catch (Exception e) {
                    dayViewHolder.time4.setText("");
                }
                try {
                    dayViewHolder.subject1.setText(days.get(i).getSubjects()[0]);
                } catch (Exception e) {
                    dayViewHolder.subject1.setText("");
                }
                try {
                    dayViewHolder.subject2.setText(days.get(i).getSubjects()[1]);
                } catch (Exception e) {
                    dayViewHolder.subject2.setText("");
                }
                try {
                    dayViewHolder.subject3.setText(days.get(i).getSubjects()[2]);
                } catch (Exception e) {
                    dayViewHolder.subject3.setText("");
                }
                try {
                    dayViewHolder.subject4.setText(days.get(i).getSubjects()[3]);
                } catch (Exception e) {
                    dayViewHolder.subject4.setText("");
                }
                try {
                    dayViewHolder.teacher1.setText(days.get(i).getTeachers()[0]);
                } catch (Exception e) {
                    dayViewHolder.teacher1.setText("");
                }
                try {
                    dayViewHolder.teacher2.setText(days.get(i).getTeachers()[1]);
                } catch (Exception e) {
                    dayViewHolder.teacher2.setText("");
                }
                try {
                    dayViewHolder.teacher3.setText(days.get(i).getTeachers()[2]);
                } catch (Exception e) {
                    dayViewHolder.teacher3.setText("");
                }
                try {
                    dayViewHolder.teacher4.setText(days.get(i).getTeachers()[3]);
                } catch (Exception e) {
                    dayViewHolder.teacher4.setText("");
                }
                break;
            case 0:
                FreeDayViewHolder freeDayViewHolder = (FreeDayViewHolder) viewHolder;
                if (days.get(i).date.equals(currentDate)) {
                    String now = "Cегодня, " + days.get(i).date;
                    freeDayViewHolder.date.setText(now);
                } else freeDayViewHolder.date.setText(days.get(i).date);
                break;
        }
    }

    @Override
    public int getItemViewType(int i) {
        for (int j = 0; j < days.get(i).getTeachers().length; j++)
            if (!days.get(i).getTeachers()[j].equals("")) return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}