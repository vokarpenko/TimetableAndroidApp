package kubsu.timetable.News.DetailNew;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import kubsu.timetable.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailNewsActivity extends AppCompatActivity {
    TextView mainTextView, tittleTextView,dateTextView;
    ImageView image;
    String newsURL, mainText, tittle, imageURL, date, thumbNailImageURL;
    ViewGroup container;
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        tittle = getIntent().getExtras().getString("Tittle", "");
        date = getIntent().getExtras().getString("Date", "");
        newsURL = getIntent().getExtras().getString("NewsURL", "");
        thumbNailImageURL = getIntent().getExtras().getString("thumbNailImageURL", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        setToolbar();
        mainTextView = findViewById(R.id.detail_news_maintext);
        tittleTextView = findViewById(R.id.detail_news_tittle);
        dateTextView = findViewById(R.id.detail_news_date);
        image = findViewById(R.id.detail_news_image);
        ViewCompat.setTransitionName(image,VIEW_NAME_HEADER_IMAGE);
        container = findViewById(R.id.detail_news_container);
        loadDetailView();
    }

    void setImageAndText(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(newsURL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                DetailNewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),"Нет соединения",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse = response.body() != null ? response.body().string() : "";
                    imageURL = getImageUrl(myResponse);
                    mainText = getMainText(myResponse);
                    DetailNewsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainTextView.setText(mainText);
                            Picasso.get().load(imageURL).fit().noPlaceholder().into(image);
                        }
                    });
                }
            }
        });
    }

    private String getImageUrl(String html){
        Document doc = Jsoup.parse(html);
        return doc.body().getElementsByClass("field field-name-image-with-caption field-type-ds field-label-hidden")
                .first().select("a").attr("href");
    }

    private String getMainText(String html){
        Document doc = Jsoup.parse(html);
        return doc.body().getElementsByClass("field field-name-body field-type-text-with-summary field-label-hidden")
                .first().text();
    }

    private void setToolbar(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_news);
        toolbar.setTitle("Новости");
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

    private void loadDetailView() {
        dateTextView.setText(date);
        tittleTextView.setText(tittle);
        // Set the title TextView to the item's name and author
        setImageAndText();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            Picasso.get().load(imageURL).into(image);
        }
    }
    private void loadThumbnail() {
        Picasso.get()
                .load(thumbNailImageURL)
                .noFade()
                .into(image);
    }
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
            return true;
        }
        return false;
    }
}