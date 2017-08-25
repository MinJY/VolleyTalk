package com.teamnexters.volleytalk.team.model;

import android.support.annotation.Nullable;

/**
 * Created by MIN on 2017. 7. 29..
 */

public class TeamDetail {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
    private String imgurl;

    public TeamDetail(String title, String originallink, String link, String description, String pubDate, @Nullable String imgurl) {
        this.title = title;
        this.originallink = originallink;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.imgurl = imgurl;
    }

    public TeamDetail(String title, String pubDate) {
        this.title = title;
        this.pubDate = pubDate;
    }

    public TeamDetail(String title, String description, String pubDate) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginallink() {
        return originallink;
    }

    public void setOriginallink(String originallink) {
        this.originallink = originallink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", originallink='" + originallink + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
