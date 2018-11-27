package kubsu.timetable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DayViewHolder>{
    public static class DayViewHolder extends RecyclerView.ViewHolder {
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

        DayViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.i("mytag", "Element " + getAdapterPosition() + " clicked.");
                                            }
                                        });
            cv = itemView.findViewById(R.id.cv);
            time1=itemView.findViewById(R.id.time1);
            time2=itemView.findViewById(R.id.time2);
            time3=itemView.findViewById(R.id.time3);
            time4=itemView.findViewById(R.id.time4);
            subject1=itemView.findViewById(R.id.subject1);
            subject2=itemView.findViewById(R.id.subject2);
            subject3=itemView.findViewById(R.id.subject3);
            subject4=itemView.findViewById(R.id.subject4);
            teacher1=itemView.findViewById(R.id.teacher1);
            teacher2=itemView.findViewById(R.id.teacher2);
            teacher3=itemView.findViewById(R.id.teacher3);
            teacher4=itemView.findViewById(R.id.teacher4);
            date=itemView.findViewById(R.id.date);
        }
    }
    private List<Day> days;

    RVAdapter(List<Day> days){
        this.days = days;
    }
    @Override
    public int getItemCount() {
        return days.size();
    }


        @Override
    @NonNull
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder dayViewHolder, int i) {
        dayViewHolder.date.setText(days.get(i).date);
        try {dayViewHolder.time1.setText(days.get(i).getTimes()[0]);} catch (Exception e){dayViewHolder.time1.setText("");}
        try {dayViewHolder.time2.setText(days.get(i).getTimes()[1]);} catch (Exception e){dayViewHolder.time2.setText("");}
        try {dayViewHolder.time3.setText(days.get(i).getTimes()[2]);} catch (Exception e){dayViewHolder.time3.setText("");}
        try {dayViewHolder.time4.setText(days.get(i).getTimes()[3]);} catch (Exception e){dayViewHolder.time4.setText("");}
        try {dayViewHolder.subject1.setText(days.get(i).getSubjects()[0]);} catch (Exception e){dayViewHolder.subject1.setText("");}
        try {dayViewHolder.subject2.setText(days.get(i).getSubjects()[1]);} catch (Exception e){dayViewHolder.subject2.setText("");}
        try {dayViewHolder.subject3.setText(days.get(i).getSubjects()[2]);} catch (Exception e){dayViewHolder.subject3.setText("");}
        try {dayViewHolder.subject4.setText(days.get(i).getSubjects()[3]);} catch (Exception e){dayViewHolder.subject4.setText("");}
        try {dayViewHolder.teacher1.setText(days.get(i).getTeachers()[0]);} catch (Exception e){dayViewHolder.teacher1.setText("");}
        try {dayViewHolder.teacher2.setText(days.get(i).getTeachers()[1]);} catch (Exception e){dayViewHolder.teacher2.setText("");}
        try {dayViewHolder.teacher3.setText(days.get(i).getTeachers()[2]);} catch (Exception e){dayViewHolder.teacher3.setText("");}
        try {dayViewHolder.teacher4.setText(days.get(i).getTeachers()[3]);} catch (Exception e){dayViewHolder.teacher4.setText("");}
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}