package com.catherine.videoplay.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Objects;

public class Feed implements Serializable {

    /**
     * id : 428
     * itemId : 1578976510452
     * itemType : 2
     * createTime : 1578977844500
     * duration : 8
     * feeds_text : 2020他来了，就在眼前了
     * authorId : 1578919786
     * activityIcon : null
     * activityText : 2020新年快乐
     * width : 960
     * height : 540
     * url : https://pipijoke.oss-cn-hangzhou.aliyuncs.com/New%20Year%20-%2029212-video.mp4
     * cover : https://pipijoke.oss-cn-hangzhou.aliyuncs.com/2020%E5%B0%81%E9%9D%A2%E5%9B%BE.png
     */
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private int id;
    private long itemId;
    private int itemType;
    private long createTime;
    private int duration;
    private String feeds_text;
    private int authorId;
    private String activityIcon;
    private String activityText;
    private int width;
    private int height;
    private String url;
    private String cover;
    private Author author;
    private Comment topComment;
    private UGC ugc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFeeds_text() {
        return feeds_text;
    }

    public void setFeeds_text(String feeds_text) {
        this.feeds_text = feeds_text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Comment getTopComment() {
        return topComment;
    }

    public void setTopComment(Comment topComment) {
        this.topComment = topComment;
    }

    public UGC getUgc() {
        return ugc;
    }

    public void setUgc(UGC ugc) {
        this.ugc = ugc;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Feed)) return false;//包括了o == null
        if (this == o) return true;
        Feed feed = (Feed) o;
        return id == feed.id &&
                itemId == feed.itemId &&
                itemType == feed.itemType &&
                createTime == feed.createTime &&
                duration == feed.duration &&
                authorId == feed.authorId &&
                width == feed.width &&
                height == feed.height &&
                TextUtils.equals(feeds_text, feed.feeds_text) &&
                TextUtils.equals(activityIcon, feed.activityIcon) &&
                TextUtils.equals(activityText, feed.activityText) &&
                TextUtils.equals(url, feed.url) &&
                TextUtils.equals(cover, feed.cover) &&
                (author != null && author.equals(feed.author)) &&
                (topComment != null && topComment.equals(feed.topComment)) &&
                (ugc != null && ugc.equals(feed.ugc));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, itemType, createTime, duration, feeds_text, authorId, activityIcon, activityText, width, height, url, cover, author, topComment, ugc);
    }
}
