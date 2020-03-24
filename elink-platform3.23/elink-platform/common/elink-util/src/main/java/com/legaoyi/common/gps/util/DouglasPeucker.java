package com.legaoyi.common.gps.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DouglasPeucker {

    /**
     * Simplifies the given poly (polyline or polygon) using the Douglas-Peucker decimation
     * algorithm. Increasing the tolerance will result in fewer points in the simplified polyline or
     * polygon.
     *
     * When the providing a polygon as input, the first and last point of the list MUST have the
     * same latitude and longitude (i.e., the polygon must be closed). If the input polygon is not
     * closed, the resulting polygon may not be fully simplified.
     *
     * The time complexity of Douglas-Peucker is O(n^2), so take care that you do not call this
     * algorithm too frequently in your code.
     *
     * @param poly polyline or polygon to be simplified. Polygon should be closed (i.e., first and
     *        last points should have the same latitude and longitude).
     * @param tolerance in meters. Increasing the tolerance will result in fewer points in the
     *        simplified poly.
     * @return a simplified poly produced by the Douglas-Peucker algorithm
     */
    public static List<LngLat> simplify(List<LngLat> poly, double tolerance) {
        final int n = poly.size();
        if (n < 1) {
            throw new IllegalArgumentException("Polyline must have at least 1 point");
        }
        if (tolerance <= 0) {
            throw new IllegalArgumentException("Tolerance must be greater than zero");
        }

        boolean closedPolygon = isClosedPolygon(poly);
        LngLat lastPoint = null;

        // Check if the provided poly is a closed polygon
        if (closedPolygon) {
            // Add a small offset to the last point for Douglas-Peucker on polygons (see #201)
            final double OFFSET = 0.00000000001;
            lastPoint = poly.get(poly.size() - 1);
            // LngLat.latitude and .longitude are immutable, so replace the last point
            poly.remove(poly.size() - 1);
            poly.add(new LngLat(lastPoint.getLat() + OFFSET, lastPoint.getLng() + OFFSET));
        }

        int idx;
        int maxIdx = 0;
        Stack<int[]> stack = new Stack<>();
        double[] dists = new double[n];
        dists[0] = 1;
        dists[n - 1] = 1;
        double maxDist;
        double dist;
        int[] current;

        if (n > 2) {
            int[] stackVal = new int[] {0, (n - 1)};
            stack.push(stackVal);
            while (stack.size() > 0) {
                current = stack.pop();
                maxDist = 0;
                for (idx = current[0] + 1; idx < current[1]; ++idx) {
                    dist = distanceToLine(poly.get(idx), poly.get(current[0]), poly.get(current[1]));
                    if (dist > maxDist) {
                        maxDist = dist;
                        maxIdx = idx;
                    }
                }
                if (maxDist > tolerance) {
                    dists[maxIdx] = maxDist;
                    int[] stackValCurMax = {current[0], maxIdx};
                    stack.push(stackValCurMax);
                    int[] stackValMaxCur = {maxIdx, current[1]};
                    stack.push(stackValMaxCur);
                }
            }
        }

        if (closedPolygon) {
            // Replace last point w/ offset with the original last point to re-close the polygon
            poly.remove(poly.size() - 1);
            poly.add(lastPoint);
        }

        // Generate the simplified line
        idx = 0;
        ArrayList<LngLat> simplifiedLine = new ArrayList<>();
        for (LngLat l : poly) {
            if (dists[idx] != 0) {
                simplifiedLine.add(l);
            }
            idx++;
        }

        return simplifiedLine;
    }

    /**
     * Returns true if the provided list of points is a closed polygon (i.e., the first and last
     * points are the same), and false if it is not
     * 
     * @param poly polyline or polygon
     * @return true if the provided list of points is a closed polygon (i.e., the first and last
     *         points are the same), and false if it is not
     */
    public static boolean isClosedPolygon(List<LngLat> poly) {
        LngLat firstPoint = poly.get(0);
        LngLat lastPoint = poly.get(poly.size() - 1);
        return firstPoint.equals(lastPoint);
    }

    /**
     * Computes the distance on the sphere between the point p and the line segment start to end.
     *
     * @param p the point to be measured
     * @param start the beginning of the line segment
     * @param end the end of the line segment
     * @return the distance in meters (assuming spherical earth)
     */
    public static double distanceToLine(final LngLat p, final LngLat start, final LngLat end) {
        if (start.equals(end)) {
            VincentyGeodesy.distanceInMeters(end, p);
        }

        final double s0lat = Math.toRadians(p.getLat());
        final double s0lng = Math.toRadians(p.getLng());
        final double s1lat = Math.toRadians(start.getLat());
        final double s1lng = Math.toRadians(start.getLng());
        final double s2lat = Math.toRadians(end.getLat());
        final double s2lng = Math.toRadians(end.getLng());

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng) / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return VincentyGeodesy.distanceInMeters(p, start);
        }
        if (u >= 1) {
            return VincentyGeodesy.distanceInMeters(p, end);
        }
        LngLat sa = new LngLat(p.getLat() - start.getLat(), p.getLng() - start.getLng());
        LngLat sb = new LngLat(u * (end.getLat() - start.getLat()), u * (end.getLng() - start.getLng()));
        return VincentyGeodesy.distanceInMeters(sa, sb);
    }
}
