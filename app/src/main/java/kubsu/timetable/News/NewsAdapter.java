package kubsu.timetable.News;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.util.List;

import kubsu.timetable.News.DetailNew.DetailNewsActivity;
import kubsu.timetable.R;

import static kubsu.timetable.News.DetailNew.DetailNewsActivity.VIEW_NAME_HEADER_IMAGE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<ItemNew> listNews;

    NewsAdapter(List<ItemNew> listNews) {
        this.listNews = listNews;
    }

    void clear(){
        listNews.clear();
    }

    void addNews(List<ItemNew> news) {
        listNews.addAll(news);
        notifyItemRangeInserted(listNews.size(), 10);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView tittle;
        AppCompatImageView image;
        CardView cardView;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_news);
            tittle = itemView.findViewById(R.id.tittle_news);
            image = itemView.findViewById(R.id.image_news);
            cardView = itemView.findViewById(R.id.card_news);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_news, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder viewHolder, int position) {
        YoYo.with(Techniques.Landing).playOn(viewHolder.cardView);
        final ItemNew currentItem = listNews.get(position);
        viewHolder.tittle.setText(currentItem.getTittle());
        viewHolder.date.setText(currentItem.getDate());
        Picasso.get().load(currentItem.getImgURL()).into(viewHolder.image);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailNews = new Intent(view.getContext(),DetailNewsActivity.class);
                detailNews.putExtra("NewsURL",currentItem.getNewsURL())
                        .putExtra("Tittle",currentItem.getTittle())
                        .putExtra("Date",currentItem.getDate())
                        .putExtra("thumbNailImageURL",currentItem.getImgURL());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)view.getContext(),
                        new Pair<>(view.findViewById(R.id.image_news),
                                VIEW_NAME_HEADER_IMAGE));
                view.getContext().startActivity(detailNews,options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }
}
