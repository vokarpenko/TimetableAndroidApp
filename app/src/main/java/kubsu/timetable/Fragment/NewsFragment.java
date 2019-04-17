package kubsu.timetable.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import kubsu.timetable.Adapter.RVNewsAdapter;
import kubsu.timetable.Model.ItemNew;
import kubsu.timetable.R;
import kubsu.timetable.Utility.L;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsFragment extends Fragment {
    private Boolean isScrolling = false;
    private int currentItems, totalItems,scrollTopOutItems;
    private View rootView;
    private ProgressBar progressBar,progressBarBottom;
    private RVNewsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<ItemNew> itemList = new ArrayList<>();
    private int countPages=0;
    private LinearLayoutManager linearLayoutManager;
    private Button buttonReturnToTop;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //finds
        recyclerView =  rootView.findViewById(R.id.rv_news);
        progressBar = rootView.findViewById(R.id.progress_bar);
        progressBarBottom = rootView.findViewById(R.id.progress_bar_bottom);
        buttonReturnToTop =  view.findViewById(R.id.button_return_to_top);
        //
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBarBottom.setVisibility(ProgressBar.INVISIBLE);
        //добавление первой страницы новостей
        addPageToListNews(countPages);
        //
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recycle, int dx, int dy) {
                super.onScrolled(recycle, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollTopOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling&& (currentItems+scrollTopOutItems==totalItems)){
                    fetchData();
                }
                if(scrollTopOutItems>4&&buttonReturnToTop.getVisibility()!=View.VISIBLE)
                    buttonReturnToTop.setVisibility(View.VISIBLE);
                if(scrollTopOutItems<4&&buttonReturnToTop.getVisibility()!=View.GONE)
                    buttonReturnToTop.setVisibility(View.GONE);
            }
        });


        buttonReturnToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }


    private void fetchData() {
        progressBarBottom.setVisibility(ProgressBar.VISIBLE);
        isScrolling=false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countPages+=1;
                addPageToListNews(countPages);
            }
        },1);
    }




    private void addPageToListNews(final int pageNumber){
        final String baseDomain = "https://www.kubsu.ru";
        String path;
        if (pageNumber>0) path=baseDomain+"/ru/news?page="+String.valueOf(pageNumber);
        else path = "https://www.kubsu.ru/news";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                L.log(e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Нет соединения",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String html = response.body() != null ? response.body().string() : "";
                    org.jsoup.nodes.Document doc = Jsoup.parse(html);
                    Element listNews = doc.body().getElementsByClass("view-content").first();
                    String tittle, imageURL, newsURL, date;

                    for (int i = 0; i < 10; i++) {
                        tittle = listNews.getElementsByClass("teaser-title").get(i).select("a").text();
                        newsURL = baseDomain + listNews.getElementsByClass("teaser-title").get(i).select("a").attr("href");
                        imageURL = listNews.getElementsByClass("group-left img-maxwidth").get(i).select("img").attr("src");
                        date = listNews.getElementsByClass("field field-name-post-date field-type-ds field-label-hidden date_with_calendar")
                                .get(i).getElementsByClass("field-item even").text();
                        //добавление в список новой вовости
                        itemList.add(new ItemNew(tittle, date, imageURL, newsURL));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pageNumber == 0) {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
                                adapter = new RVNewsAdapter(itemList);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);
                            } else {
                                //adapter.notifyDataSetChanged();

                                adapter.notifyItemRangeInserted(linearLayoutManager.getItemCount(),10);
                                progressBarBottom.setVisibility(ProgressBar.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });
    }
}
