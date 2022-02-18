package com.catherine.videoplay.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.alibaba.fastjson.annotation.JSONField;
import com.catherine.videoplay.BR;

import java.io.Serializable;

public class UGC extends BaseObservable implements Serializable {

    /**
     * likeCount : 1001
     * shareCount : 12
     * commentCount : 504
     * hasFavorite : false
     * hasLiked : false
     * hasDissed : false
     */

    private int likeCount;
    private int shareCount;
    private int commentCount;
    private boolean hasFavorite;
    private boolean hasLiked;
    @JSONField(name = "hasdiss")
    private boolean hasDissed;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    @Bindable
    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        if (this.hasLiked == hasLiked) {
            return;
        }
        if (hasLiked) {
            likeCount = likeCount + 1;
            setHasDissed(false);
        } else {
            likeCount = likeCount - 1;
        }
        this.hasLiked = hasLiked;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public boolean isHasDissed() {
        return hasDissed;
    }

    public void setHasDissed(boolean hasDissed) {
        if (this.hasDissed == hasDissed) {
            return;
        }
        if(hasDissed){
            setHasLiked(false);
        }
        this.hasDissed = hasDissed;
        notifyPropertyChanged(BR._all);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UGC)) return false;
        UGC ugc = (UGC) o;
        return likeCount == ugc.likeCount &&
                shareCount == ugc.shareCount &&
                commentCount == ugc.commentCount &&
                hasFavorite == ugc.hasFavorite &&
                hasLiked == ugc.hasLiked &&
                hasDissed == ugc.hasDissed;
    }

}



