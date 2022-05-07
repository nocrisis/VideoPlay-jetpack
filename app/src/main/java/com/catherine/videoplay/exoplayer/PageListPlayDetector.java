package com.catherine.videoplay.exoplayer;

import android.graphics.Point;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PageListPlayDetector {
    private List<IPlayTarget> mTargets = new ArrayList<>();
    private RecyclerView mRecycleview;
    private IPlayTarget playingTarget;

    public void addTarget(IPlayTarget target) {
        mTargets.add(target);
    }

    public void removeTarget(IPlayTarget target) {
        mTargets.remove(target);
    }

    public PageListPlayDetector(LifecycleOwner owner, RecyclerView recyclerView) {
        mRecycleview = recyclerView;
        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    recyclerView.getAdapter().unregisterAdapterDataObserver(mDataObserver);
                    owner.getLifecycle().removeObserver(this);
                }
            }
        });
        recyclerView.getAdapter().registerAdapterDataObserver(mDataObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlay();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (playingTarget != null && playingTarget.isPlaying() && !isTargetInBounds(playingTarget)) {
                    //滑动时检测到不在屏幕内关闭播放
                    playingTarget.lossActive();
                }
            }
        });
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            autoPlay();

        }
    };

    private void autoPlay() {
        if (mTargets.size() <= 0 || mRecycleview.getChildCount() <= 0) {
            return;
        }
        //如果正在播放的还处于屏幕中心播放中
        if (playingTarget != null && playingTarget.isPlaying() && isTargetInBounds(playingTarget)) {
            return;
        }
        //检测新的处在屏幕内的要播放的
        IPlayTarget activeTarget = null;
        for (IPlayTarget target : mTargets) {
            boolean inBounds = isTargetInBounds(target);
            if (inBounds) {
                activeTarget = target;
                break;
            }
        }
        //检测到新的active,关闭上一个正在播放的
        if (activeTarget != null) {
            if (playingTarget != null && playingTarget.isPlaying()) {
                //关闭视频播放
                playingTarget.lossActive();
            }
            playingTarget = activeTarget;
            activeTarget.focusOnActive();
        }
    }

    //是否有足够的面积处于屏幕内
    private boolean isTargetInBounds(IPlayTarget target) {

        ViewGroup owner = target.getOwner();
        ensureRecyclerViewLocation();
        if (!owner.isShown() || !owner.isAttachedToWindow()) {
            return false;
        }
        int[] location = new int[2];
        owner.getLocationOnScreen(location);
        int center = location[1] + owner.getHeight() / 2;
        return center >= rvLocation.x && center <= rvLocation.y;
    }

    private Point rvLocation = null;

    private Point ensureRecyclerViewLocation() {
        if (rvLocation == null) {
            int[] location = new int[2];
            mRecycleview.getLocationOnScreen(location);
            int top = location[1];
            int bottom = top + mRecycleview.getHeight();
            rvLocation = new Point(top, bottom);
        }
        return rvLocation;
    }

    public void onPause() {
        if (playingTarget != null) {
            playingTarget.lossActive();
        }
    }

    public void onResume() {
        if (playingTarget != null) {
            playingTarget.focusOnActive();
        }
    }
}
