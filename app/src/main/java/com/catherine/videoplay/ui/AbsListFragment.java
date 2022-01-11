package com.catherine.videoplay.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.libcommon.AppGlobals;
import com.catherine.libcommon.EmptyView;
import com.catherine.videoplay.R;
import com.catherine.videoplay.databinding.LayoutRefreshViewBinding;
import com.catherine.videoplay.viewmodel.AbsViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class
AbsListFragment<T, M extends AbsViewModel> extends Fragment
        implements OnRefreshListener, OnLoadMoreListener {
    protected LayoutRefreshViewBinding binding;
    protected RecyclerView mRecyclerview;
    protected SmartRefreshLayout mRefreshLayout;
    protected EmptyView mEmptyView;
    protected PagedListAdapter<T, RecyclerView.ViewHolder> adapter;
    protected M mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutRefreshViewBinding.inflate(inflater, container, false);
        mRecyclerview = binding.recyclerView;
        mRefreshLayout = binding.refreshLayout;
        mEmptyView = binding.emptyView;
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        adapter = getAdapter();
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setItemAnimator(null);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
        mRecyclerview.addItemDecoration(decoration);
        genericViewModel();
        return binding.getRoot();
    }
    //必须在onCreateView里就实例化VModel,不能等到onViewCreated,否则子类onViewCreated里使用时为null
    private void genericViewModel() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClz = ((Class) argument).asSubclass(AbsViewModel.class);
  /*          ViewModel viewModel=new ViewModelProvider(this,
                    new ViewModelProvider.AndroidViewModelFactory(AppGlobals.getApplication())).get(modelClz);*/
            mViewModel = (M) new ViewModelProvider(this).get(modelClz);
/*            mViewModel = (M) new ViewModelProvider(this,
                    new ViewModelProvider.AndroidViewModelFactory(AppGlobals.getApplication()))
                    .get(modelClz);*/
            mViewModel.getPageData().observe(getViewLifecycleOwner(), new Observer<PagedList<T>>() {
                @Override
                public void onChanged(PagedList<T> ts) {
                    adapter.submitList(ts);
                }
            });
            mViewModel.getBoundaryPageData().observe(getViewLifecycleOwner(),
                    new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean hasData) {
                            finishRefresh(hasData);
                        }
                    });
        }
    }

    public void submitList(PagedList<T> pagedList) {
        if (pagedList.size() > 0) {
            adapter.submitList(pagedList);
        }
        finishRefresh(pagedList.size() > 0);
    }

    public void finishRefresh(boolean hasData) {
        PagedList<T> currentList = adapter.getCurrentList();
        hasData = hasData || currentList != null && currentList.size() > 0;
        RefreshState state = mRefreshLayout.getState();
        if (state.isFooter && state.isOpening) {
            mRefreshLayout.finishLoadMore();
        } else if (state.isHeader && state.isOpening) {
            mRefreshLayout.finishRefresh();
        }
        if (hasData) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();
}
