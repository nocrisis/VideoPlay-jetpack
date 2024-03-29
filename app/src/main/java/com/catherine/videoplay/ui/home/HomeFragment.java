package com.catherine.videoplay.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.catherine.libnavannotation.FragmentDestination;
import com.catherine.videoplay.exoplayer.PageListPlayDetector;
import com.catherine.videoplay.model.Feed;
import com.catherine.videoplay.ui.AbsListFragment;
import com.catherine.videoplay.ui.MutablePageKeyedDataSource;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

@FragmentDestination(asStarter = true, pageUrl = "main/tabs/home")
public class HomeFragment extends AbsListFragment<Feed, HomeViewModel> {

    private PageListPlayDetector playDetector;
    private String feedType;

    public static HomeFragment newInstance(String feedType) {
        Bundle args = new Bundle();
        args.putString("feedType", feedType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getCacheLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                adapter.submitList(feeds);
            }
        });
        playDetector = new PageListPlayDetector(this, mRecyclerview);


    }


    @Override
    public PagedListAdapter getAdapter() {
        feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(), feedType){
            @Override
            public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                playDetector.addTarget(holder.getListPlayerView());
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
                playDetector.removeTarget(holder.getListPlayerView());
            }
        };
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        final PagedList<Feed> currentList = adapter.getCurrentList();
        if (currentList == null || currentList.size() <= 0) {
            finishRefresh(false);
            return;
        }
        Feed feed = currentList.get(adapter.getItemCount() - 1);
        mViewModel.loadAfter(feed.getId(), new ItemKeyedDataSource.LoadCallback<Feed>() {
            @Override
            public void onResult(@NonNull List<Feed> datas) {
                PagedList.Config config = currentList.getConfig();
                if (datas.size() > 0) {
                    MutablePageKeyedDataSource dataSource = new MutablePageKeyedDataSource();
                    dataSource.data.addAll(datas);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    submitList(pagedList);
                }
            }
        });
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详情见：LivePagedListBuilder#compute方法
        mViewModel.getDataSource().invalidate();
    }

    @Override
    public void onPause() {
        playDetector.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        playDetector.onResume();
        super.onResume();
    }
}