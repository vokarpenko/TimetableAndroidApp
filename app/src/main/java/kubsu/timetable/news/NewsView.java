package kubsu.timetable.news;

import java.util.List;

import kubsu.timetable.utility.ErrorToast;

interface NewsView extends ErrorToast {
    void updateNews(List<ItemNew> itemNews);

    void setProgressbarVisibility(int visibility);

    void setBottomProgressbarVisibility(int visibility);

    void setAdapter(List<ItemNew> itemNews);

    void setRefreshing(boolean refresh);

    void setButtonTopVisibility(int visibility);

    void goTop();

    void addNews(List<ItemNew> itemNews);
}
