package kubsu.timetable.News;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import kubsu.timetable.R;

public class NewsFragment extends Fragment implements NewsView {
    private View view;
    private ProgressBar progressBar, bottomProgressBar;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private Button buttonReturnToTop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsPresenter presenter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (presenter==null){
            presenter = new NewsPresenter(this,new NewsRepository());
        }
        init();
        presenter.setNews();

    }

    private void init() {
        recyclerView =  view.findViewById(R.id.rv_news);
        progressBar = view.findViewById(R.id.progress_bar);
        bottomProgressBar = view.findViewById(R.id.progress_bar_bottom);
        linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initSwipeRefresh();
        initScrollListener();
        initButtonReturnToTop();
    }

    private void initButtonReturnToTop() {
        buttonReturnToTop =  view.findViewById(R.id.button_return_to_top);
        buttonReturnToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goTop();
            }
        });
    }

    private void initScrollListener() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.scrollStateChanged(newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recycle, int dx, int dy) {
                super.onScrolled(recycle, dx, dy);
                presenter.scrolled(linearLayoutManager);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateNews();
            }
        });
    }

    @Override
    public void updateNews(List<ItemNew> itemNews) {
        adapter.clear();
        adapter.addNews(itemNews);
    }

    @Override
    public void setProgressbarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void setBottomProgressbarVisibility(int visibility) {
        bottomProgressBar.setVisibility(visibility);
    }

    @Override
    public void setAdapter(List<ItemNew> itemNews) {
        adapter = new NewsAdapter(itemNews);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setRefreshing(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void setBottomTopVisibility(int visibility) {
        buttonReturnToTop.setVisibility(visibility);
    }

    @Override
    public void goTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void addNews(List<ItemNew> itemNews) {
        adapter.addNews(itemNews);
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }
}
