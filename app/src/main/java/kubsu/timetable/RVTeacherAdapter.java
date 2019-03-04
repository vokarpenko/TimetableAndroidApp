package kubsu.timetable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RVTeacherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.t_date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        //switch (viewType) {
            /*case 0:
                v = LayoutInflater.from(viewType.getContext()).inflate(R.layout.item_card_freeday, viewType, false);
                return new RVStudentAdapter.FreeDayViewHolder(v);
                */
            //case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_teacher, viewGroup, false);
                return new DayViewHolder(v);
            //default:
               // return new RVStudentAdapter.FreeDayViewHolder(null);
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //DayViewHolder.
    }

    @Override
    public int getItemCount() {
        return 14;
    }
}
