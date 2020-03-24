package com.legaoyi.common.gps.util;

import java.util.List;

public class ShapeUtil {

    public static boolean isInCircle(double lng, double lat, double centerlng, double centerlat, double radius) {
        double dDistance = VincentyGeodesy.distanceInMeters(new LngLat(lng, lat), new LngLat(centerlng, centerlat));
        return dDistance <= radius;
    }

    /**
     * 点到直线的最短距离的判断 点（p0Lat,p0Lng） 到由两点组成的线段（p1Lat,p1Lng） ,( p2Lat,p2Lng)
     * 
     * @param p1Lat
     * @param p1Lng
     * @param p2Lat
     * @param p2Lng
     * @param p0Lat
     * @param p0Lng
     * @return
     */
    public static double pointToLine(double p1Lat, double p1Lng, double p2Lat, double p2Lng, double p0Lat, double p0Lng) {
        double a = VincentyGeodesy.distanceInMeters(new LngLat(p1Lng, p1Lat), new LngLat(p2Lng, p2Lat));
        double b = VincentyGeodesy.distanceInMeters(new LngLat(p1Lng, p1Lat), new LngLat(p0Lng, p0Lat));
        double c = VincentyGeodesy.distanceInMeters(new LngLat(p2Lng, p2Lat), new LngLat(p0Lng, p0Lat));
        if (c + b == a) {// 点在线段上
            return 0;
        }

        if (a <= 0.000001) {// 不是线段，是一个点
            return b;
        }

        if (c * c >= a * a + b * b) { // 组成直角三角形或钝角三角形，(p1,p1)为直角或钝角
            return b;
        }

        if (b * b >= a * a + c * c) {// 组成直角三角形或钝角三角形，(p2,p2)为直角或钝角
            return c;
        }
        // 组成锐角三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        return 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
    }

    /**
     * 获得给定的点中，最大距离的两个点之间的距离
     * 
     * @param arr
     * @return
     * @throws Exception
     */
    public static double getMaxDistance(List<LngLat> list) throws Exception {
        if (list == null || list.isEmpty() || list.size() < 2) {
            return 0;
        }
        double max = 0;
        LngLat startPoint = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            LngLat point = list.get(i);
            double distance = VincentyGeodesy.distanceInMeters(startPoint, point);
            if (distance > max) {
                max = distance;
            }
        }
        return max;
    }

    /**
     * 获得给定的点中，最大距离的两个点之间的距离
     * 
     * @param center
     * @param arr
     * @return
     * @throws Exception
     */
    public static double getMaxDistance(LngLat center, List<LngLat> list) throws Exception {
        if (center == null || list == null || list.isEmpty()) {
            return 0;
        }
        double max = 0;
        for (int i = 0; i < list.size(); i++) {
            LngLat point = list.get(i);
            double distance = VincentyGeodesy.distanceInMeters(center, point);
            if (distance > max) {
                max = distance;
            }
        }
        return max;
    }

    /***
     * 判断三个经纬度是否钝角三角形
     * @param pointA
     * @param pointB
     * @param pointC
     * @return
     */
    public static boolean isObtuseTriangle(LngLat pointA, LngLat pointB, LngLat pointC) {
        double a = VincentyGeodesy.distanceInMeters(pointA, pointB);
        double b = VincentyGeodesy.distanceInMeters(pointB, pointC);
        double c = VincentyGeodesy.distanceInMeters(pointC, pointA);
        double max = Math.max(a, b);
        if (max > c) {
            if (max == a) {
                a = c;
            } else {
                b = c;
            }
            c = max;
        }

        // 设三角形三边分别是a b c （假设c是最长的那条边）
        // 如果a^2 + b^2 = c^2,则为直角
        // 如果a^2 + b^2 < c^2, 则为钝角
        // 如果a^2 + b^2 > c^2, 则为锐角
        if (!(a + b > c && Math.abs(a - b) < c)) {
            return false;// "无法构成三角形";
        } else if (a == b || a == c || b == c) {
            return false;// "等腰三角形";
        } else if (a * a + b * b == c * c) {
            return false;// "直角三角形";
        } else if (a * a + b * b < c * c) {
            return true;// "钝角三角形";
        } else {
            return false;// "锐角三角形";
        }
    }
}
