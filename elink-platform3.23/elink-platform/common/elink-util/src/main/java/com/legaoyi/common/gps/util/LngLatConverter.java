package com.legaoyi.common.gps.util;

/**
 * 提供百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换
 * 
 * @author gaoshengbo
 *
 */
public class LngLatConverter {

    private static double pi = 3.1415926535897932384626;

    private static double a = 6378245.0;

    private static double ee = 0.00669342162296594323;

    /**
     * wgs84 to 火星坐标系 (GCJ-02)
     * 
     * @param lng
     * @param lat
     * @return
     * @return
     */
    public static LngLat wgs84Togcj02(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return null;
        }
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLng = lng + dLng;
        return new LngLat(mgLng, mgLat);
    }

    /**
     * 火星坐标系 (GCJ-02) to wgs84
     * 
     * @param lng
     * @param lat
     * @return
     */
    public static LngLat gcj02Towgs84(double lng, double lat) {
        LngLat gps = transform(lng, lat);
        double lontitude = lng * 2 - gps.getLng();
        double latitude = lat * 2 - gps.getLat();
        return new LngLat(lontitude, latitude);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     * 
     * @param lng
     * @param lat
     * @return
     */
    public static LngLat gcj02Tobd09(double lng, double lat) {
        double x = lng, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LngLat(bd_lng, bd_lat);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 ,将 BD-09坐标转换成GCJ-02坐标
     * 
     * @param lng
     * @param lat
     * @return
     */
    public static LngLat bd09Togcj02(double lng, double lat) {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LngLat(gg_lng, gg_lat);
    }

    /**
     * 
     * @param lng
     * @param lat
     * @return
     */
    public static LngLat bd09Towgs84(double lng, double lat) {
        LngLat gcj02 = bd09Togcj02(lng, lat);
        LngLat wgs84 = gcj02Towgs84(gcj02.getLng(), gcj02.getLat());
        return wgs84;
    }

    public static boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    private static LngLat transform(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new LngLat(lng, lat);
        }
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLng = lng + dLng;
        return new LngLat(mgLng, mgLat);
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
