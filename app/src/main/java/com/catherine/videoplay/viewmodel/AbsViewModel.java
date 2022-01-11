package com.catherine.videoplay.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class AbsViewModel<T> extends ViewModel {

    protected PagedList.Config config;
    private DataSource dataSource;

    private LiveData<PagedList<T>> pageData;

    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public AbsViewModel() {
         config = new PagedList.Config.Builder()
                .setPageSize(10)
                //大于页码数以流出滑动控件不会显示立刻出发加载下一页
                .setInitialLoadSizeHint(12)
/*设置距离最后还有多少个item时,即寻呼库开始加载下一页的数据*/
//                .setPrefetchDistance()
                .build();
        pageData = new LivePagedListBuilder(factory, config)
                //初始化数据需要传递的参数
                .setInitialLoadKey(0)
//                .setFetchExecutor()异步执行任务的的线程池
                .setBoundaryCallback(callback)
                .build();
    }

    PagedList.BoundaryCallback<T> callback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            //没有数据
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            //有数据了
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            boundaryPageData.postValue(true);
        }
    };

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            dataSource = createDataSource();
            return dataSource;
        }

    };

    public abstract DataSource createDataSource();

    public LiveData<PagedList<T>> getPageData() {

        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }
}
