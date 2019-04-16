package kubsu.timetable.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import kubsu.timetable.Adapter.RVNewsAdapter;
import kubsu.timetable.Model.ItemNew;
import kubsu.timetable.Utility.L;

public class AsyncRequestListNews extends AsyncTask<Void,Void,Void> {
    private ProgressDialog progressDialog;
    private Boolean isScrolling = false;
    private int currentItems, totalItems,scrollOutItems;
    private RecyclerView recyclerView;
    private RVNewsAdapter adapter;
    private ArrayList<ItemNew> itemList = new ArrayList<>();
    private final int countPagesNews = 1;
    public AsyncRequestListNews(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        progressDialog = new ProgressDialog(recyclerView.getContext());
        progressDialog.setMessage("Загрузка...");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        org.jsoup.nodes.Document doc[]=  new Document[countPagesNews];
        String path = "https://www.kubsu.ru/news";
        String baseDomain = "https://www.kubsu.ru";
        for (int i = 0; i <doc.length ; i++) {
            try {
                if (i>0) path=baseDomain+"/ru/news?page="+String.valueOf(i);
                doc[i] = Jsoup.connect(path)
                        .userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com")
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        //получение списка новостей
        for (int j =0;j<doc.length ; j++) {

            Element listNews = doc[j].body().getElementsByClass("view-content").get(0);
            String tittle="", imageURL="", newsURL="", date="";

            for (int i = 0; i < 10; i++) {
                tittle = listNews.getElementsByClass("teaser-title").get(i).select("a").text();

                newsURL = baseDomain + listNews.getElementsByClass("teaser-title").get(i).select("a").attr("href");

                imageURL = listNews.getElementsByClass("group-left img-maxwidth").get(i).select("img").attr("src");

                date = listNews.getElementsByClass("field field-name-post-date field-type-ds field-label-hidden date_with_calendar")
                        .get(i).getElementsByClass("field-item even").text();
                //добавление в список новой вовости
                this.itemList.add(new ItemNew(tittle, date, imageURL, newsURL));
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        progressDialog.dismiss();
        adapter = new RVNewsAdapter(itemList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling&& (currentItems+scrollOutItems==totalItems)){
                    fetchData();
                }
            }
        });
    }

    private void fetchData() {
        isScrolling=false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*itemList.add(new ItemNew("123","123","123","123"));
                itemList.add(new ItemNew("123","123","123","123"));
                itemList.add(new ItemNew("123","123","123","123"));
                itemList.add(new ItemNew("123","123","123","123"));*/
                adapter.notifyDataSetChanged();
            }
        },3000);
        L.log("Конец");
    }
}
