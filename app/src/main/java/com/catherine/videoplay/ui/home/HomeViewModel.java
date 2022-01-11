package com.catherine.videoplay.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.catherine.libnetwork.ApiResponse;
import com.catherine.libnetwork.ApiService;
import com.catherine.libnetwork.JsonCallback;
import com.catherine.libnetwork.Request;
import com.catherine.videoplay.model.Feed;
import com.catherine.videoplay.ui.MutablePageKeyedDataSource;
import com.catherine.videoplay.viewmodel.AbsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeViewModel extends AbsViewModel<Feed> {
    public static final String tag = "Class: HomeViewModel";
    private volatile boolean withCache = true;
    private String mFeedType;
    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();
    //加载分页（page框架或手动）的同步位标识
    private AtomicBoolean loadAfter = new AtomicBoolean(false);

    @Override
    public DataSource createDataSource() {
        return new FeedDataSource();
    }

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    class FeedDataSource extends ItemKeyedDataSource<Integer, Feed> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            Log.d(tag, "loadInitial:loadData(0, callback)");
            //加载初始化数据的(回调都自动切换到了子线程)
            loadData(0, callback);
            Log.d(tag, "loadInitial:withCache = false");
            withCache = false;
            //先加载缓存，再加载网络数据，网络数据加载成功后再更新缓存
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //加载分页数据的(回调都自动切换到了子线程)
            Log.d(tag, "loadAfter:loadData(" + params.key + ", callback)");

            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //向前加载的，跳到第一页等等(回调都自动切换到了子线程)
            Log.d(tag, "loadBefore:loadData(callback.onResult(Collections.emptyList())");
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.getId();
        }
    }


    public void setFeedType(String feedType) {
        this.mFeedType = feedType;
    }

    // /feed/queryHotFeedsList
    //key=0,上拉分页不需要缓存，key
    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (key > 0) {
            loadAfter.set(true);
        }
        Request request = ApiService.get("/feeds/queryHotFeedsList")
//                .addParam("feedType", mFeedType)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());
        if (withCache) {
            Log.d(tag, "loadData:if (withCache){request.cacheStrategy(Request.CACHE_ONLY);request.execute(new JsonCallback<List<Feed>>()");
            request.cacheStrategy(Request.CACHE_ONLY);
            //之前使用了缓存的的开启一个新的线程，防止阻塞请求
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    Log.d(tag, "loadData:if (withCache){request.execute(){onCacheSuccess: " + JSON.toJSONString(response, SerializerFeature.WriteNullStringAsEmpty) + "}");
                    MutablePageKeyedDataSource<Integer, Feed> dataSource = new MutablePageKeyedDataSource<>();
                    dataSource.data.addAll(response.body);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    Log.d(tag, "loadData:if (withCache){request.execute(new JsonCallback<List<Feed>>(){onCacheSuccess: cacheLiveData.postValue}");
                    cacheLiveData.postValue(pagedList);
                }
            });
        }
        try {
            //如果之前使用了缓存的，clone得到新的缓存对象
            Request netRequest = withCache ? request.clone() : request;
            //下拉刷新需要更新缓存，上拉分页不需要更新缓存
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            Log.d(tag, "loadData:ApiResponse.execute()");
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            Log.d(tag, "loadData:List<Feed> data = response.body or emptyList");
            callback.onResult(data);
            Log.d(tag, "loadData:callback.onResult(data);");
            //上拉加载
            if (key > 0) {
                //不管有没有数据都要通过liveData发送数据通知ui是否需要上拉加载分页动画关闭
                getBoundaryPageData().postValue(data.size() > 0);
                Log.d(tag, "loadData:if (key > 0) {getBoundaryPageData().postValue(data.size() > 0);}");
                //加载完成重置分页标识
                loadAfter.set(false);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Log.e("loadData", "loadData: key:" + key);
    }


    @SuppressLint("RestrictedApi")
    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        Log.d(tag, "loadAfter: ");
        //已经分页过了就不再分页
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, callback);
            }
        });

    }
}