package com.legaoyi.common.gps.util;

import java.util.ArrayList;
import java.util.List;

/***
 * 实现经纬度坐标是否在范围内的算法.需求是:一个点(经纬度)是否在一个多边形内部,多边形有多个点构成,每个点是一个实际的经纬度坐标,有多个点构成一个多边形， 算法数学上实现思路:
 * 判断一个点是在一个多边形内部的集中情况 第一:目标点在多边形的某一个顶点上,我们认为目标点在多边形内部 第二:目标点在多边形的任意一天边上,我们认为目标点在多边形内部
 * 第三:这种情况就比较复杂了,
 * 不在某天边上，也不和任何一个顶点重合.这时候就需要我们自己去算了，解决方案是将目标点的Y坐标与多边形的每一个点进行比较，我们会得到一个目标点所在的行与多边形边的交点的列表
 * 。如果目标点的两边点的个数都是奇数个则该目标点在多边形内，否则在多边形外。
 * 这种算法适合凸多边形也适合凹多边形，所以是一种通用的算法，同时也解决了多边形的点的顺序不同导致的形状不同，比如一个五边形，可以是凸五边形，也可以是一个凹五边形，这个根据点的位置和顺序决定的。
 * 
 * @author gaoshengbo
 *
 */
public class PolygonUtil {

    /**
     * 是否有 横断<br/>
     * 参数为四个点的坐标
     * 
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @param px3
     * @param py3
     * @param px4
     * @param py4
     * @return
     */
    public static boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4, double py4) {
        boolean flag = false;
        double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
        if (d != 0) {
            double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
            double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
            if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 目标点是否在目标边上边上<br/>
     * 
     * @param px0 目标点的经度坐标
     * @param py0 目标点的纬度坐标
     * @param px1 目标线的起点(终点)经度坐标
     * @param py1 目标线的起点(终点)纬度坐标
     * @param px2 目标线的终点(起点)经度坐标
     * @param py2 目标线的终点(起点)纬度坐标
     * @return
     */
    public static boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2) {
        boolean flag = false;
        double ESP = 1e-9;// 无限小的正数
        if ((Math.abs(multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0) && ((py0 - py1) * (py0 - py2) <= 0)) {
            flag = true;
        }
        return flag;
    }

    public static double multiply(double px0, double py0, double px1, double py1, double px2, double py2) {
        return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
    }

    /**
     * 判断目标点是否在多边形内(由多个点组成)<br/>
     * 
     * @param px 目标点的经度坐标
     * @param py 目标点的纬度坐标
     * @param list 多边形的经纬度坐标集合
     * @return
     */
    public static boolean isPointInPolygon(double px, double py, List<LngLat> list) {
        boolean isInside = false;
        double ESP = 1e-9;
        int count = 0;
        double linePoint1x;
        double linePoint1y;
        double linePoint2x = 180;
        double linePoint2y;

        linePoint1x = px;
        linePoint1y = py;
        linePoint2y = py;

        for (int i = 0; i < list.size() - 1; i++) {
            LngLat LngLat1 = list.get(i);
            double cx1 = LngLat1.getLng();
            double cy1 = LngLat1.getLat();

            LngLat LngLat2 = list.get(i + 1);
            double cx2 = LngLat2.getLng();
            double cy2 = LngLat2.getLat();
            // 如果目标点在任何一条线上
            if (isPointOnLine(px, py, cx1, cy1, cx2, cy2)) {
                return true;
            }
            // 如果线段的长度无限小(趋于零)那么这两点实际是重合的，不足以构成一条线段
            if (Math.abs(cy2 - cy1) < ESP) {
                continue;
            }
            // 第一个点是否在以目标点为基础衍生的平行纬度线
            if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                // 第二个点在第一个的下方,靠近赤道纬度为零(最小纬度)
                if (cy1 > cy2)
                    count++;
            }
            // 第二个点是否在以目标点为基础衍生的平行纬度线
            else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                // 第二个点在第一个的上方,靠近极点(南极或北极)纬度为90(最大纬度)
                if (cy2 > cy1)
                    count++;
            }
            // 由两点组成的线段是否和以目标点为基础衍生的平行纬度线相交
            else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                count++;
            }
        }
        if (count % 2 == 1) {
            isInside = true;
        }

        return isInside;
    }

    public static void main(String[] args) {
        LngLat a1 = new LngLat(113.941853, 22.530777);
        LngLat a3 = new LngLat(113.94788, 22.527597);
        LngLat a2 = new LngLat(113.940487, 22.527789);
        LngLat a4 = new LngLat(113.947925, 22.530618);
        LngLat a5 = new LngLat(113.941772, 22.530727);
        List<LngLat> areas = new ArrayList<LngLat>();
        areas.add(a1);
        areas.add(a2);
        areas.add(a3);
        areas.add(a4);
        areas.add(a5);

        Boolean flag = PolygonUtil.isPointInPolygon(113.947871, 22.52804, areas);
        System.out.println(flag);
    }
}
