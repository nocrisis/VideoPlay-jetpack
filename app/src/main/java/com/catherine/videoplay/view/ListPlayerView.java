package com.catherine.videoplay.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.catherine.libcommon.PixUtils;
import com.catherine.videoplay.R;
import com.catherine.videoplay.exoplayer.IPlayTarget;
import com.catherine.videoplay.exoplayer.PageListPlay;
import com.catherine.videoplay.exoplayer.PageListPlayManager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

public class ListPlayerView extends FrameLayout implements IPlayTarget,
        PlayerControlView.VisibilityListener,
        Player.EventListener {
    //底部进度条
    private View bufferView;
    private BindImageView cover, blur;
    private AppCompatImageView playBtn;
    private String mCategory;
    private String mVideoUrl;
    private boolean isPlaying;
    private int curWindowIndex = 0;
    private long curPos = 0;

    public ListPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        /*<merge/>只能作为XML布局的根标签使用
        当Inflate以< merge />开头的布局文件时，必须指定一个父ViewGroup，并且必须设定attachToRoot为true。 必须为TRUE，是因为MERGE标签里没有可用的根结点
LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true);
由此，可以看出merge标签能够将该标签中的所有控件直接连在上一级布局上面，从而减少布局层级，也就是说会直接将<merge />内的元素添加到<merge />的父元素里*/
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true);
        //进度条
        bufferView = findViewById(R.id.buffer_view);
        //封面view
        cover = findViewById(R.id.cover);
        //高斯模糊背景图,防止出现两边留黑
        blur = findViewById(R.id.blur_background);
        playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    Log.d("lossActive","isPlaying="+isPlaying());
                    lossActive();
                } else {
                    Log.d("focusOnActive","isPlaying="+isPlaying());

                    focusOnActive();
                }
            }
        });
    }

    public void bindData(String category, int widthPx, int heightPx, String coverUrl, String videoUrl) {

        mCategory = category;
        mVideoUrl = videoUrl;

        cover.setImageUrl(coverUrl);
        if (widthPx < heightPx) {
            //竖屏视频两边不够才需要加载高速模糊背景
            BindImageView.setBlurImgUrl(blur, coverUrl, 10);
            blur.setVisibility(VISIBLE);
        } else {//横屏不需要填充
            blur.setVisibility(INVISIBLE);
        }
        setSize(widthPx, heightPx);
        Log.d("bindData", "videoUrl=" + videoUrl);

    }

    //宽高等比缩放
    private void setSize(int widthPx, int heightPx) {
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = maxWidth;
        //组件包括黑色填充的宽高
        int layoutWidth = maxWidth;
        int layoutHeight = 0;
        //封面的宽高等于视频实际的宽高
        int coverWidth;
        int coverHeight;
        //横屏视频
        if (widthPx >= heightPx) {
            coverWidth = maxWidth;
            //宽占满，缩放高度等比例自适应
            layoutHeight = coverHeight = (int) (heightPx / (widthPx * 1.0f / layoutWidth));
            //竖屏视频，整个组件正方形
        } else {
            layoutHeight = coverHeight = maxHeight;
            //高占满组件，缩放宽度等比例自适应
            coverWidth = (int) (widthPx / (heightPx * 1.0f / coverHeight));
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = layoutWidth;
        params.height = layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurParams = blur.getLayoutParams();
        blurParams.width = layoutWidth;
        blurParams.height = layoutHeight;
        blur.setLayoutParams(blurParams);

        FrameLayout.LayoutParams coverParams = (LayoutParams) cover.getLayoutParams();
        coverParams.width = coverWidth;
        coverParams.height = coverHeight;
        coverParams.gravity = Gravity.CENTER;
        cover.setLayoutParams(coverParams);

        FrameLayout.LayoutParams playBtnParams = (LayoutParams) playBtn.getLayoutParams();
        playBtnParams.gravity = Gravity.CENTER;
        playBtn.setLayoutParams(playBtnParams);
    }


    @Override
    public ViewGroup getOwner() {
        return this;
    }

    @Override
    public void focusOnActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        PlayerView playerView = pageListPlay.playerView;
        PlayerControlView controlView = pageListPlay.controlView;
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;
        if (playerView == null) {
            return;
        }
        ViewParent parent = playerView.getParent();
        if (parent != this) {
            //把展示视频画面的View添加到ItemView的容器上
            if (parent != null) {
                //如果不为空，代表playerView元素已经被添加到其它的ListPlayView上了,需要先移除
                ((ViewGroup) parent).removeView(playerView);
            }
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            //parent View index=0
            this.addView(playerView, 1, layoutParams);
        }
        ViewParent ctrlParent = controlView.getParent();
        if (ctrlParent != this) {
            //把视频控制器 添加到ItemView的容器上
            if (ctrlParent != null) {
                ((ViewGroup) ctrlParent).removeView(controlView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            this.addView(controlView);
            controlView.addVisibilityListener(this);
        }
        //显示控制器进度条
        controlView.show();

        //如果是同一个视频资源,则不需要从重新创建mediaSource。
        //但需要onPlayerStateChanged 否则不会触发onPlayerStateChanged()
        exoPlayer.seekTo(curWindowIndex, curPos);
        curPos = pageListPlay.exoPlayer.getCurrentPosition();
        curWindowIndex = pageListPlay.exoPlayer.getCurrentWindowIndex();
        Log.d("focusOnActive", "curPos=" + curPos + ", curWindowIndex=" + curWindowIndex);
        if (TextUtils.equals(pageListPlay.playUrl, mVideoUrl)) {
            onPlaybackStateChanged(Player.STATE_READY);
            onPlayWhenReadyChanged(true, Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST);
        } else {
            MediaSource mediaSource = PageListPlayManager.createMediaSource(mVideoUrl);
            exoPlayer.setMediaSource(mediaSource);
            exoPlayer.prepare();
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            pageListPlay.playUrl = mVideoUrl;
        }
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isPlaying = false;
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
        Log.d("onDetachedFromWindow", "videoUrl=" + mVideoUrl);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击视频区，底部控制器显示出来，并且锁死其他区域不能点击
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        pageListPlay.controlView.show();
        return true;
    }

    @Override
    public void lossActive() {
        //暂停播放并让 封面图 和 开始播放按钮 显示出来
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);

        if (pageListPlay.exoPlayer == null || pageListPlay.controlView == null)
            return;
        pageListPlay.exoPlayer.setPlayWhenReady(false);
        pageListPlay.exoPlayer.removeListener(this);
        pageListPlay.controlView.removeVisibilityListener(this);
        playBtn.setVisibility(VISIBLE);
        cover.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);


    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        playBtn.setVisibility(visibility);
        playBtn.setImageResource(isPlaying() ? R.drawable.icon_video_pause : R.drawable.icon_video_play);

    }

    @Override
    public void onPlaybackStateChanged(int state) {
        Log.d("onPlaybackStateChanged", "state=" + state);
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;
        long bufferedPosition = exoPlayer.getBufferedPosition();
        Log.d("onPlaybackStateChanged", "bufferedPosition=" + bufferedPosition);

        if (state == Player.STATE_READY) {
            Log.d("onPlaybackStateChanged", "state=Player.STATE_READY");
            if (exoPlayer.getPlayWhenReady()) {
                //playing
                Log.d("onPlaybackStateChanged", "state=Player.STATE_READY,isPlayerWhenReady---play");
//                exoPlayer.play();
            } else {
                //PAUSED
                Log.d("onPlaybackStateChanged", "state=Player.STATE_READY,notPlayerWhenReady---pause");

//                exoPlayer.pause();
            }
            if (bufferedPosition != 0) {
                cover.setVisibility(GONE);
                bufferView.setVisibility(GONE);
            }
        } else if (state == Player.STATE_BUFFERING) {
            Log.d("onPlaybackStateChanged", "state=Player.STATE_BUFFERING");
            bufferView.setVisibility(VISIBLE);
        } else {
            Log.d("onPlaybackStateChanged", "state=" + state);

        }

    }


    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
        Log.d("onPlayWhenReadyChanged", "reason=" + reason);

        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;
        Log.d("onPlayWhenReadyChanged", "Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST=" + Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST);
        Log.d("onPlayWhenReadyChanged", "exoPlayer.getBufferedPosition()=" + exoPlayer.getBufferedPosition());
        curPos = pageListPlay.exoPlayer.getCurrentPosition();
        curWindowIndex = pageListPlay.exoPlayer.getCurrentWindowIndex();
        Log.d("onPlayWhenReadyChanged", "curPos=" + curPos + ", curWindowIndex=" + curWindowIndex);
        isPlaying = playWhenReady
                && reason == Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST;
//                &&exoPlayer.getBufferedPosition() != 0;
        playBtn.setImageResource(isPlaying ? R.drawable.icon_video_pause : R.drawable.icon_video_play);

    }
}
