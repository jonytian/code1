package com.legaoyi.common.gps.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GeoHash implements Serializable {

    private static final long serialVersionUID = 7584031649203542603L;

    public static final int MAX_PRECISION = 52;

    private static final long FIRST_BIT_FLAGGED = 0x8000000000000L;

    private long bits = 0;

    private byte significantBits = 0;

    private GeoHash() {}

    public static GeoHash toGeoHash(LngLat lnglat) {
        return toGeoHash(lnglat, MAX_PRECISION);
    }

    public static GeoHash toGeoHash(double lng, double lat) {
        return toGeoHash(new LngLat(lng, lat), MAX_PRECISION);
    }

    public static GeoHash toGeoHash(double lng, double lat, int precision) {
        return toGeoHash(new LngLat(lng, lat), precision);
    }

    public static GeoHash toGeoHash(LngLat lnglat, int precision) {
        GeoHash geoHash = new GeoHash();
        boolean isEvenBit = true;
        double[] latitudeRange = {-90, 90};
        double[] longitudeRange = {-180, 180};

        while (geoHash.significantBits < precision) {
            if (isEvenBit) {
                divideRangeEncode(geoHash, lnglat.getLng(), longitudeRange);
            } else {
                divideRangeEncode(geoHash, lnglat.getLat(), latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }
        geoHash.bits <<= (MAX_PRECISION - precision);
        return geoHash;
    }

    public static GeoHash toGeoHash(long longValue) {
        return toGeoHash(longValue, MAX_PRECISION);
    }

    public static GeoHash toGeoHash(long longValue, int significantBits) {
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};
        boolean isEvenBit = true;
        GeoHash geoHash = new GeoHash();
        String binaryString = Long.toBinaryString(longValue);
        while (binaryString.length() < MAX_PRECISION) {
            binaryString = "0".concat(binaryString);
        }

        for (int j = 0; j < significantBits; j++) {
            if (isEvenBit) {
                divideRangeDecode(geoHash, longitudeRange, binaryString.charAt(j) != '0');
            } else {
                divideRangeDecode(geoHash, latitudeRange, binaryString.charAt(j) != '0');
            }
            isEvenBit = !isEvenBit;
        }
        geoHash.bits <<= (MAX_PRECISION - geoHash.significantBits);
        return geoHash;
    }

    public List<GeoHash> getAdjacent() {
        GeoHash northern = getNorthernNeighbour();
        GeoHash eastern = getEasternNeighbour();
        GeoHash southern = getSouthernNeighbour();
        GeoHash western = getWesternNeighbour();
        return Arrays.asList(northern, northern.getEasternNeighbour(), eastern, southern.getEasternNeighbour(),
                southern, southern.getWesternNeighbour(), western, northern.getWesternNeighbour(), this);
    }

    public long toLong() {
        return bits;
    }

    @Override
    public String toString() {
        return Long.toBinaryString(bits);
    }

    private GeoHash getNorthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] += 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getSouthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] -= 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getEasternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] += 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash getWesternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] -= 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    private GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
        GeoHash geoHash = new GeoHash();
        boolean isEvenBit = false;
        latBits[0] <<= (MAX_PRECISION - latBits[1]);
        lonBits[0] <<= (MAX_PRECISION - lonBits[1]);
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
            if (isEvenBit) {
                divideRangeDecode(geoHash, latitudeRange, (latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                latBits[0] <<= 1;
            } else {
                divideRangeDecode(geoHash, longitudeRange, (lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                lonBits[0] <<= 1;
            }
            isEvenBit = !isEvenBit;
        }
        geoHash.bits <<= (MAX_PRECISION - geoHash.significantBits);
        return geoHash;
    }

    private long[] getRightAlignedLatitudeBits() {
        long copyOfBits = bits << 1;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
        return new long[] {value, getNumberOfLatLonBits()[0]};
    }

    private long[] getRightAlignedLongitudeBits() {
        long copyOfBits = bits;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[1]);
        return new long[] {value, getNumberOfLatLonBits()[1]};
    }

    private long extractEverySecondBit(long copyOfBits, int numberOfBits) {
        long value = 0;
        for (int i = 0; i < numberOfBits; i++) {
            if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
                value |= 0x1;
            }
            value <<= 1;
            copyOfBits <<= 2;
        }
        value >>>= 1;
        return value;
    }

    private int[] getNumberOfLatLonBits() {
        if (significantBits % 2 == 0) {
            return new int[] {significantBits / 2, significantBits / 2};
        } else {
            return new int[] {significantBits / 2, significantBits / 2 + 1};
        }
    }

    private long maskLastNBits(long value, long number) {
        long mask = 0xffffffffffffffffL;
        mask >>>= (MAX_PRECISION - number);
        return value & mask;
    }

    private static void divideRangeEncode(GeoHash geoHash, double value, double[] range) {
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            geoHash.addOnBitToEnd();
            range[0] = mid;
        } else {
            geoHash.addOffBitToEnd();
            range[1] = mid;
        }
    }

    private static void divideRangeDecode(GeoHash geoHash, double[] range, boolean isOnBit) {
        double mid = (range[0] + range[1]) / 2;
        if (isOnBit) {
            geoHash.addOnBitToEnd();
            range[0] = mid;
        } else {
            geoHash.addOffBitToEnd();
            range[1] = mid;
        }
    }

    private void addOnBitToEnd() {
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    private void addOffBitToEnd() {
        significantBits++;
        bits <<= 1;
    }
}
