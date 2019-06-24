package kubsu.timetable.news;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.AbsListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import static kubsu.timetable.utility.Constant.KUBSU_URL;
import static kubsu.timetable.utility.Constant.NEWS_URL;

class NewsRepository {
    static final String FETCH_DATA = "fetchData";
    static final String SET_VISIBLE_BUTTON_TOP = "setVisibleButtonTop";
    static final String SET_INVISIBLE_BUTTON_TOP = "setInvisibleButtonTop";
    private boolean isScrolling;
    private int countPages=0;

    String scrolled(LinearLayoutManager linearLayoutManager){
        int currentItems = linearLayoutManager.getChildCount();
        int totalItems = linearLayoutManager.getItemCount();
        int scrollTopOutItems = linearLayoutManager.findFirstVisibleItemPosition();
        if(isScrolling&&((currentItems + scrollTopOutItems) == totalItems)){
            return FETCH_DATA;
        }
        if(scrollTopOutItems >4)
            return SET_VISIBLE_BUTTON_TOP;
        if(scrollTopOutItems <4)
            return SET_INVISIBLE_BUTTON_TOP;
        return "";
    }

    Single<List<ItemNew>> getNewsPage(final int pageNumber) {
         return Single.create(new SingleOnSubscribe<List<ItemNew>>() {
             @Override
             public void subscribe(SingleEmitter<List<ItemNew>> emitter) throws Exception {
                 List<ItemNew> itemNewList= new ArrayList<>();
                 Document response  = Jsoup.connect(NEWS_URL + String.valueOf(pageNumber)).get();
                 Element listNews = response.body().getElementsByClass("view-content").first();
                 String tittle, imageURL, newsURL, date;

                 for (int i = 0; i < 10; i++) {
                     tittle = listNews.getElementsByClass("teaser-title").get(i).select("a").text();
                     newsURL = KUBSU_URL + listNews.getElementsByClass("teaser-title").get(i).select("a").attr("href");
                     imageURL = listNews.getElementsByClass("group-left img-maxwidth").get(i).select("img").attr("src");
                     date = listNews.getElementsByClass("field field-name-post-date field-type-ds field-label-hidden date_with_calendar")
                             .get(i).getElementsByClass("field-item even").text();
                     //добавление в список новой вовости
                     itemNewList.add(new ItemNew(tittle, date, imageURL, newsURL));
                 }
                 emitter.onSuccess(itemNewList);
             }
         });
     }

    void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }


    int getCountPages() {
        return countPages;
    }

    void setCountPages(int countPages) {
        this.countPages = countPages;
    }

    void scrollStateChanged(int newState) {
        if (newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            setScrolling(true);
    }
}
