package kubsu.timetable.News;

import java.util.List;

import kubsu.timetable.Utility.ErrorToast;

interface NewsView extends ErrorToast {
    void updateNews(List<ItemNew> itemNews);

    void setProgressbarVisibility(int visibility);

    void setBottomProgressbarVisibility(int visibility);

    void setAdapter(List<ItemNew> itemNews);

    void setRefreshing(boolean refresh);

    void setBottomTopVisibility(int visibility);

    void goTop();

    void addNews(List<ItemNew> itemNews);
}
