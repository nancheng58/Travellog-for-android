package com.code.travellog.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.common.geometry.S2CellId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description: 地理信息
 * @date: 2021/3/20
 */
public class GeoUtil {

    public static Double convertRationalLatLonToFloat(
            String rationalString, String ref) {

        String[] parts = rationalString.split(",");

        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return -result;
        }
        return  result;
    }
    /**
     * 获取指定级别的全部子cell
     *
     * @param root     当前cellId
     * @param desLevel 目标level, > root.level()
     */
    public List<S2CellId> children(S2CellId root, int desLevel) {
        if (root.level() < desLevel) {
            long interval = (root.childEnd().id() - root.childBegin().id()) / 4;
            List<S2CellId> list = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                long id = root.childBegin().id() + interval * i;
                S2CellId cellId = new S2CellId(id);
                List<S2CellId> childrenCellId = children(cellId, desLevel);
                list.addAll(childrenCellId);
            }
            return list;
        } else if (root.level() == desLevel) {
            return Collections.singletonList(root);
        } else {
            return Collections.emptyList();
        }
    }
    /**
     * 获取指定子级的边界Cell（当前cell内部）
     *
     * @param s2CellId 当前cellId
     * @param desLevel 目标cellId的level 需大于当前cellId
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<S2CellId> childrenCellId(S2CellId root, S2CellId s2CellId, int desLevel) throws IOException {
        if (s2CellId.level() < desLevel - 1) {
            long interval = (s2CellId.childEnd().id() - s2CellId.childBegin().id()) / 4;
            List<S2CellId> list = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                long id = s2CellId.childBegin().id() + interval * i;
                S2CellId cellId = new S2CellId(id);

                S2CellId[] neighbors = new S2CellId[4];
                cellId.getEdgeNeighbors(neighbors);

                boolean edge = Arrays.stream(neighbors).anyMatch(neighbor -> !root.contains(neighbor));
                if (edge) {
                    List<S2CellId> childrenCellId = childrenCellId(root, cellId, desLevel);
                    list.addAll(childrenCellId);
                }
            }
            return list;
        } else if (s2CellId.level() == desLevel - 1) {
            long interval = (s2CellId.childEnd().id() - s2CellId.childBegin().id()) / 4;
            List<S2CellId> list = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                long id = s2CellId.childBegin().id() + interval * i;
                S2CellId cellId = new S2CellId(id);

                S2CellId[] neighbors = new S2CellId[4];
                cellId.getEdgeNeighbors(neighbors);

                boolean edge = Arrays.stream(neighbors).anyMatch(neighbor -> !root.contains(neighbor));
                if (edge) {
                    list.add(cellId);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }
    private static final double LAT_OFFSET_0(double x, double y) {
        return -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
    }

    private static final double LAT_OFFSET_1(double x, double y) {
        return (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LAT_OFFSET_2(double x, double y) {
        return (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LAT_OFFSET_3(double x, double y) {
        return (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_0(double x, double y) {
        return 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
    }

    private static final double LON_OFFSET_1(double x, double y) {
        return (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_2(double x, double y) {
        return (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static final double LON_OFFSET_3(double x, double y) {
        return (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
    }

    private static double RANGE_LON_MAX = 137.8347;
    private static double RANGE_LON_MIN = 72.004;
    private static double RANGE_LAT_MAX = 55.8271;
    private static double RANGE_LAT_MIN = 0.8293;

    private static double jzA  = 6378245.0;
    private static double jzEE = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = LAT_OFFSET_0(x, y);
        ret += LAT_OFFSET_1(x, y);
        ret += LAT_OFFSET_2(x, y);
        ret += LAT_OFFSET_3(x, y);
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = LON_OFFSET_0(x, y);
        ret += LON_OFFSET_1(x, y);
        ret += LON_OFFSET_2(x, y);
        ret += LON_OFFSET_3(x, y);
        return ret;
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon < RANGE_LON_MIN || lon > RANGE_LON_MAX)
            return true;
        if (lat < RANGE_LAT_MIN || lat > RANGE_LAT_MAX)
            return true;
        return false;
    }

    public static LatLng gcj02Encrypt(double ggLat, double ggLon) {
        LatLng resPoint = new LatLng();
        double mgLat;
        double mgLon;
        if (outOfChina(ggLat, ggLon)) {
            resPoint.latitude = ggLat;
            resPoint.longitude = ggLon;
            return resPoint;
        }
        double dLat = transformLat(ggLon - 105.0, ggLat - 35.0);
        double dLon = transformLon(ggLon - 105.0, ggLat - 35.0);
        double radLat = ggLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - jzEE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((jzA * (1 - jzEE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (jzA / sqrtMagic * Math.cos(radLat) * Math.PI);
        mgLat = ggLat + dLat;
        mgLon = ggLon + dLon;

        resPoint.latitude = mgLat;
        resPoint.longitude = mgLon;
        return resPoint;
    }

    public static LatLng gcj02Decrypt(double gjLat, double gjLon) {
        LatLng gPt = gcj02Encrypt(gjLat, gjLon);
        double dLon = gPt.longitude - gjLon;
        double dLat = gPt.latitude - gjLat;
        LatLng pt = new LatLng();
        pt.latitude = gjLat - dLat;
        pt.longitude = gjLon - dLon;
        return pt;
    }

    public static LatLng bd09Decrypt(double bdLat, double bdLon) {
        LatLng gcjPt = new LatLng();
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        gcjPt.longitude = z * Math.cos(theta);
        gcjPt.latitude = z * Math.sin(theta);
        return gcjPt;
    }

    public static LatLng bd09Encrypt(double ggLat, double ggLon) {
        LatLng bdPt = new LatLng();
        double x = ggLon, y = ggLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        bdPt.longitude = z * Math.cos(theta) + 0.0065;
        bdPt.latitude = z * Math.sin(theta) + 0.006;
        return bdPt;
    }

    /**
     * @param location 世界标准地理坐标(WGS-84)
     * @return 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @brief 世界标准地理坐标(WGS-84) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
     *
     * ####只在中国大陆的范围的坐标有效，以外直接返回世界标准坐标
     */
    public static LatLng wgs84ToGcj02(LatLng location) {
        return gcj02Encrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 中国国测局地理坐标（GCJ-02）
     * @return 世界标准地理坐标（WGS-84）
     * @brief 中国国测局地理坐标（GCJ-02） 转换成 世界标准地理坐标（WGS-84）
     *
     * ####此接口有1－2米左右的误差，需要精确定位情景慎用
     */
    public static LatLng gcj02ToWgs84(LatLng location) {
        return gcj02Decrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 世界标准地理坐标(WGS-84)
     * @return 百度地理坐标（BD-09)
     * @brief 世界标准地理坐标(WGS-84) 转换成 百度地理坐标（BD-09)
     */
    public static LatLng wgs84ToBd09(LatLng location) {
        LatLng gcj02Pt = gcj02Encrypt(location.latitude, location.longitude);
        return bd09Encrypt(gcj02Pt.latitude, gcj02Pt.longitude);
    }

    /**
     * @param location 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @return 百度地理坐标（BD-09)
     * @brief 中国国测局地理坐标（GCJ-02）<火星坐标> 转换成 百度地理坐标（BD-09)
     */
    public static LatLng gcj02ToBd09(LatLng location) {
        return bd09Encrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 百度地理坐标（BD-09)
     * @return 中国国测局地理坐标（GCJ-02）<火星坐标>
     * @brief 百度地理坐标（BD-09) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
     */
    public static LatLng bd09ToGcj02(LatLng location) {
        return bd09Decrypt(location.latitude, location.longitude);
    }

    /**
     * @param location 百度地理坐标（BD-09)
     * @return 世界标准地理坐标（WGS-84）
     * @brief 百度地理坐标（BD-09) 转换成 世界标准地理坐标（WGS-84）
     *
     * ####此接口有1－2米左右的误差，需要精确定位情景慎用
     */
    public static LatLng bd09ToWgs84(LatLng location) {
        LatLng gcj02 = bd09ToGcj02(location);
        return gcj02Decrypt(gcj02.latitude, gcj02.longitude);
    }

    public static class LatLng {
        public double latitude;
        public double longitude;

        public LatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public LatLng() {
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

}
