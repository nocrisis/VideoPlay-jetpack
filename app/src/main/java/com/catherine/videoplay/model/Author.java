package com.catherine.videoplay.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Objects;

public class Author implements Serializable {

    /**
     * id : 1250
     * userId : 1578919786
     * name : 、蓅哖╰伊人为谁笑
     * avatar : http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100
     * description : 这是一只神秘的jetpack
     * likeCount : 3
     * topCommentCount : 0
     * followCount : 0
     * followerCount : 2
     * qqOpenId : FE41683AD4ECF91B7736CA9DB8104A5C
     * expires_time : 1586695789903
     * score : 0
     * historyCount : 222
     * commentCount : 9
     * favoriteCount : 0
     * feedCount : 0
     * hasFollow : false
     */

    private int id;
    private int userId;
    private String name;
    private String avatar;
    private String description;
    private int likeCount;
    private int topCommentCount;
    private int followCount;
    private int followerCount;
    private String qqOpenId;
    private long expires_time;
    private int score;
    private int historyCount;
    private int commentCount;
    private int favoriteCount;
    private int feedCount;
    private boolean hasFollow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getTopCommentCount() {
        return topCommentCount;
    }

    public void setTopCommentCount(int topCommentCount) {
        this.topCommentCount = topCommentCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public long getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(long expires_time) {
        this.expires_time = expires_time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(int feedCount) {
        this.feedCount = feedCount;
    }

    public boolean isHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return id == author.id &&
                userId == author.userId &&
                likeCount == author.likeCount &&
                topCommentCount == author.topCommentCount &&
                followCount == author.followCount &&
                followerCount == author.followerCount &&
                expires_time == author.expires_time &&
                score == author.score &&
                historyCount == author.historyCount &&
                commentCount == author.commentCount &&
                favoriteCount == author.favoriteCount &&
                feedCount == author.feedCount &&
                hasFollow == author.hasFollow &&
                TextUtils.equals(name, author.name) &&
                TextUtils.equals(avatar, author.avatar) &&
                TextUtils.equals(description, author.description) &&
                TextUtils.equals(qqOpenId, author.qqOpenId);
    }

}
