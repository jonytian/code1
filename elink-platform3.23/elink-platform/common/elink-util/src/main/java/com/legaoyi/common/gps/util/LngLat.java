package com.legaoyi.common.gps.util;

public class LngLat {

    /**
     * 经度值
     */
    private double lng;

    /**
     * 纬度值
     */
    private double lat;

    public LngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public final double getLng() {
        return lng;
    }

    public final void setLng(double lng) {
        this.lng = lng;
    }

    public final double getLat() {
        return lat;
    }

    public final void setLat(double lat) {
        this.lat = lat;
    }

}
