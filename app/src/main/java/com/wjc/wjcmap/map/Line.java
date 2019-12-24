package com.wjc.wjcmap.map;

public class Line {
    String startPointId;
    String endPointId;
    double distance;

    @Override
    public String toString() {
        return "Line{" +
                "startPointId='" + startPointId + '\'' +
                ", endPointId='" + endPointId + '\'' +
                ", distance=" + distance +
                '}';
    }

    public String getStartPointId() {
        return startPointId;
    }

    public void setStartPointId(String startPointId) {
        this.startPointId = startPointId;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
