
package com.catherine.videoplay.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class User implements Serializable {


    private String avatar;
    private Long commentCount;
    private String description;
    @JSONField(name = "expires_time")
    private Long expiresTime;
    
    private Long favoriteCount;
    
    private Long feedCount;
    
    private Long followCount;
    
    private Long followerCount;
    
    private Boolean hasFollow;
    
    private Long historyCount;
    
    private Long id;
    
    private Long likeCount;
    
    private String name;
    
    private String qqOpenId;
    
    private Long score;
    
    private Long topCommentCount;
    
    private Long userId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(Long feedCount) {
        this.feedCount = feedCount;
    }

    public Long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Long followCount) {
        this.followCount = followCount;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
    }

    public Boolean getHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(Boolean hasFollow) {
        this.hasFollow = hasFollow;
    }

    public Long getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(Long historyCount) {
        this.historyCount = historyCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getTopCommentCount() {
        return topCommentCount;
    }

    public void setTopCommentCount(Long topCommentCount) {
        this.topCommentCount = topCommentCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
