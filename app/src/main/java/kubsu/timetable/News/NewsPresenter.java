package kubsu.timetable.News;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static kubsu.timetable.News.NewsRepository.FETCH_DATA;
import static kubsu.timetable.News.NewsRepository.SET_INVISIBLE_BUTTON_TOP;
import static kubsu.timetable.News.NewsRepository.SET_VISIBLE_BUTTON_TOP;
import static kubsu.timetable.Utility.Constant.INTERNET_ERROR;

class NewsPresenter {
    private NewsRepository repository;
    private NewsView view;

    NewsPresenter( NewsView view, NewsRepository repository) {
        this.repository = repository;
        this.view = view;
    }


    void setNews() {
        view.setProgressbarVisibility(ProgressBar.VISIBLE);
        view.setBottomProgressbarVisibility(ProgressBar.INVISIBLE);
        repository.getNewsPage(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ItemNew>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<ItemNew> itemNews) {
                        view.setProgressbarVisibility(ProgressBar.INVISIBLE);
                        view.setAdapter(itemNews);

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(INTERNET_ERROR);
                    }
                });
    }

    void updateNews() {
        repository.setCountPages(0);
        repository.getNewsPage(repository.getCountPages())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ItemNew>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ItemNew> itemNews) {
                        view.setRefreshing(false);
                        view.updateNews(itemNews);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(INTERNET_ERROR);
                    }
                });
    }

    void scrollStateChanged(int newState) {
        repository.scrollStateChanged(newState);
    }

    void scrolled(LinearLayoutManager linearLayoutManager) {
        switch (repository.scrolled(linearLayoutManager)){
            case SET_INVISIBLE_BUTTON_TOP:
                view.setBottomTopVisibility(View.INVISIBLE);
                break;
            case SET_VISIBLE_BUTTON_TOP:
                view.setBottomTopVisibility(View.VISIBLE);
                break;
            case FETCH_DATA:
                view.setBottomProgressbarVisibility(ProgressBar.VISIBLE);
                repository.setScrolling(false);
                repository.getNewsPage(repository.getCountPages())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<ItemNew>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }
                            @Override
                            public void onSuccess(List<ItemNew> itemNews) {
                                view.addNews(itemNews);
                                repository.setCountPages(repository.getCountPages()+1);
                                view.setBottomProgressbarVisibility(ProgressBar.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                view.showErrorMessage(INTERNET_ERROR);
                                view.setBottomProgressbarVisibility(ProgressBar.INVISIBLE);
                            }
                        });
                break;
        }
    }

    void goTop() {
        view.goTop();
    }
}
