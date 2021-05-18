package com.catherine.videoplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.catherine.libcommon.PixUtils;
import com.catherine.videoplay.R;

public class ListPlayerView extends FrameLayout {
    //底部进度条
    private View bufferView;
    private BindImageView cover, blur;
    private AppCompatImageView playBtn;
    private String mCategory;
    private String mVideoUrl;

    public ListPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true);
        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.blur_background);
        playBtn = findViewById(R.id.play_btn);

    }

    public void bindData(String category, int widthPx, int heightPx, String coverUrl, String videoUrl) {

        mCategory = category;
        mVideoUrl = videoUrl;
        cover.setImgUrl(coverUrl);
        if (widthPx < heightPx) {
            //竖屏视频两边不够才需要加载高速模糊背景
            blur.setBlurImgUrl(coverUrl, 10);
            blur.setVisibility(VISIBLE);
        } else {//横屏不需要填充
            blur.setVisibility(INVISIBLE);
        }
        setSize(widthPx, heightPx);
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }
}
