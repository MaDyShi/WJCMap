package com.wjc.wjcmap.bean;

import android.os.Parcelable;

import java.io.Serializable;

public class Bean implements Serializable {


    /**
     * content : 2008羽毛球奥运场馆，现在用于师生活动
     * distance : 0.0
     * lat : 39.878684
     * lon : 116.490973
     * title : 奥运馆
     */

    private String content;
    private double distance;
    private String lat;
    private String lon;
    private String title;
    private String addressId;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "content='" + content + '\'' +
                ", distance=" + distance +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", title='" + title + '\'' +
                ", addressId='" + addressId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
