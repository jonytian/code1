package com.legaoyi.lbs.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.gps.util.DouglasPeucker;
import com.legaoyi.common.gps.util.GeoRange;
import com.legaoyi.common.gps.util.GeoSearch;
import com.legaoyi.common.gps.util.LngLat;
import com.legaoyi.common.gps.util.PolygonUtil;
import com.legaoyi.common.gps.util.ShapeUtil;
import com.legaoyi.common.util.Constants;
import com.legaoyi.common.util.Result;
import com.legaoyi.lbs.service.GpsInfoService;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.jpa.util.JsonUtil;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.model.DistrictSearchData;
import com.legaoyi.platform.model.Enterprise;
import com.legaoyi.platform.rest.BaseController;
import com.legaoyi.platform.service.DeviceService;

@RestController("gpsInfoController")
@RequestMapping(produces = {"application/json"})
public class GpsInfoController extends BaseController {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Autowired
    @Qualifier("gpsInfoService")
    private GpsInfoService gpsInfoService;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @RequestMapping(value = "/gps/{deviceId}/last", method = RequestMethod.GET)
    public Result getLastGps(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            int status = this.deviceService.getStatus(deviceId, gatewayId);
            if (status != 2 && status != 3) {
                throw new BizProcessException("车辆不存在或者未启用");
            }
            return new Result(this.gpsInfoService.getLastGps(deviceId));
        }
        throw new BizProcessException(Constants.INVALID_REQUEST, "非法参数");
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/gps/topSpeed/{n}", method = RequestMethod.GET)
    public Result getTopSpeed(@PathVariable int n, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        String[] selectFields = new String[] {"id"};
        Map<String, Object> andCondition = new HashMap<String, Object>();
        if (isParent) {
            andCondition.put("enterpriseId.rlike", enterpriseId);
        } else {
            andCondition.put("enterpriseId.eq", enterpriseId);
        }
        andCondition.put("state.gte", 2);
        andCondition.put("state.lte", 3);

        List<?> list = this.service.find(Device.ENTITY_NAME, selectFields, null, true, andCondition);
        if (list != null && !list.isEmpty()) {
            List<Object[]> retList = new ArrayList<Object[]>();
            Object[] item;
            for (Object o : list) {
                String deviceId = (String) o;
                Map<?, ?> map = this.gpsInfoService.getLastGps(deviceId);
                if (map == null || map.isEmpty()) {
                    continue;
                }
                Map<String, Object> maxSpeedOverView = (Map<String, Object>) map.get("maxSpeedOverView");
                if (maxSpeedOverView == null) {
                    continue;
                }

                Map<String, Object> month = (Map<String, Object>) maxSpeedOverView.get("month");
                if (month == null) {
                    continue;
                }

                double maxSpeed = (Double) month.get("speed");
                if (retList.size() < n) {
                    item = new Object[2];
                    item[0] = deviceId;
                    item[1] = maxSpeed;
                    retList.add(item);
                } else {
                    // 找出最小
                    int index = 0;
                    Object[] min = retList.get(0);
                    for (int i = 1; i < n; i++) {
                        Object[] arr = retList.get(i);
                        if ((Double) arr[1] < (Double) min[1]) {
                            index = i;
                            min = arr;
                        }
                    }
                    if ((Double) min[1] < maxSpeed) {
                        item = new Object[2];
                        item[0] = deviceId;
                        item[1] = maxSpeed;
                        retList.set(index, item);
                    }
                }
            }

            Collections.sort(retList, new Comparator<Object[]>() {

                @Override
                public int compare(Object[] arg0, Object[] arg1) {
                    return (Double) arg0[1] > (Double) arg1[1] ? -1 : 1;
                }
            });
            return new Result(retList);
        }
        return new Result();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/gps/{deviceId}/simple", method = RequestMethod.POST)
    public Result simplify(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> form) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            int status = this.deviceService.getStatus(deviceId, gatewayId);
            if (status != 2 && status != 3) {
                throw new BizProcessException("车辆不存在或者未启用");
            }
            Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
            if (conditions == null) {
                conditions = new HashMap<String, Object>();
                form.put("conditions", conditions);
            }
            conditions.put("deviceId.eq", deviceId);
            return new Result(this.gpsInfoService.simplify(form));
        }
        throw new BizProcessException(Constants.INVALID_REQUEST, "非法参数");
    }

    @RequestMapping(value = "/gps/district/simple", method = RequestMethod.POST)
    public Result simplifyDistrict(@RequestBody Map<String, Object> form) throws Exception {
        List<?> bounds = (List<?>) form.get("bounds");
        if (bounds == null || bounds.isEmpty()) {
            return new Result();
        }
        Object toleranceObj = form.get("tolerance");
        double tolerance = toleranceObj == null ? 500 : Double.valueOf(String.valueOf(form.get("tolerance")));
        List<Object> list = Lists.newArrayList();
        for (Object bound : bounds) {
            String path = (String) bound;
            String arr[] = path.split(";");
            List<LngLat> points = Lists.newArrayList();
            for (String s : arr) {
                String a[] = s.split(",");
                LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                points.add(lngLat);
            }
            points.add(points.get(0));
            points = DouglasPeucker.simplify(points, tolerance);
            StringBuilder sb = new StringBuilder();
            for (LngLat point : points) {
                sb.append(";").append(point.getLng()).append(",").append(point.getLat());
            }
            list.add(sb.deleteCharAt(0).toString());
        }
        return new Result(list);
    }

    @RequestMapping(value = "/gps/district", method = RequestMethod.POST)
    public Result saveDistrict(@RequestBody Map<String, Object> form) throws Exception {
        List<?> bounds = (List<?>) form.get("bounds");
        double lat = Double.parseDouble(String.valueOf(form.get("lat")));
        double lng = Double.parseDouble(String.valueOf(form.get("lng")));
        String name = (String) form.get("name");
        String areaCode = (String) form.get("adcode");
        List<LngLat> points = Lists.newArrayList();
        for (Object bound : bounds) {
            String path = (String) bound;
            String arr[] = path.split(";");
            List<LngLat> paths = Lists.newArrayList();
            for (String s : arr) {
                String a[] = s.split(",");
                LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                paths.add(lngLat);
            }
            points.addAll(paths);
        }
        points.add(points.get(0));
        double tolerance = 500;
        String level = (String) form.get("level");
        if (level.equals("province")) {
            tolerance = 2000;
        } else if (level.equals("city")) {
            tolerance = 1000;
        }
        points = DouglasPeucker.simplify(points, tolerance);
        double range = ShapeUtil.getMaxDistance(new LngLat(lng, lat), points);
        DistrictSearchData data = new DistrictSearchData();
        data.setCode(areaCode);
        data.setName(name);
        data.setBounds(JsonUtil.covertObjectToString(bounds));
        data.setCenterLat(lat);
        data.setCenterLng(lng);
        data.setMaxRadius((int) range);
        this.service.persist(data);
        return new Result();
    }

    @GetMapping(value = "/enterprise/devices/position")
    public Result getDevicesPosition(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(defaultValue = "-1", required = false) int state) throws Exception {
        String[] selectFields = new String[] {"id", "name", "state", "bizState"};
        Map<String, Object> andCondition = new HashMap<String, Object>();
        andCondition.put("enterpriseId.rlike", enterpriseId);
        if (state != -1) {
            andCondition.put("state.eq", state);
        } else {
            andCondition.put("state.gte", 2);
            andCondition.put("state.lte", 3);
        }
        List<?> list = this.service.find(Device.ENTITY_NAME, selectFields, null, true, andCondition);
        if (list != null && !list.isEmpty()) {
            List<Object> retList = new ArrayList<Object>();
            Object[] item;
            for (Object o : list) {
                Object[] data = (Object[]) o;
                Map<?, ?> map = this.gpsInfoService.getLastGps((String) data[0]);
                if (map == null || map.isEmpty()) {
                    continue;
                }

                item = new Object[selectFields.length + 3];
                System.arraycopy(data, 0, item, 0, data.length);
                item[selectFields.length] = map.get("lng");
                item[selectFields.length + 1] = map.get("lat");
                item[selectFields.length + 2] = map.get("direction");
                retList.add(item);
            }
            return new Result(retList);
        }
        return new Result();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/area/devices", method = RequestMethod.POST)
    public Result getDevices(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> form) throws Exception {
        List<Map<String, Object>> retList = Lists.newArrayList();
        int type = Integer.parseInt(String.valueOf(form.get("type")));
        Map<String, Object> area = (Map<String, Object>) form.get("area");

        double range = 0, lat = 0, lng = 0;
        List<LngLat> pathList = Lists.newArrayList();
        List<List<LngLat>> bounds = Lists.newArrayList();
        if (type == 1) {
            range = Double.parseDouble(String.valueOf(area.get("radius")));
            lat = Double.parseDouble(String.valueOf(area.get("lat")));
            lng = Double.parseDouble(String.valueOf(area.get("lng")));
        } else if (type == 2) {// 多边形区域
            String path = (String) area.get("path");
            String arr[] = path.split(";");
            for (String s : arr) {
                String a[] = s.split(",");
                LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                pathList.add(lngLat);
            }
            range = ShapeUtil.getMaxDistance(pathList);
            lat = pathList.get(0).getLat();
            lng = pathList.get(0).getLng();
        } else if (type == 3) {// 行政区域查询
            String areaCode = (String) form.get("areaCode");
            if (StringUtils.isBlank(areaCode)) {
                throw new BizProcessException("参数错误,areaCode=".concat(areaCode));
            }
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("code.eq", areaCode);
            List<?> list = this.service.find(DistrictSearchData.ENTITY_NAME, null, false, andCondition);
            if (list == null || list.isEmpty()) {
                throw new BizProcessException("参数错误,areaCode=".concat(areaCode));
            }
            DistrictSearchData district = (DistrictSearchData) list.get(0);
            String json = district.getBounds();
            lat = district.getCenterLat();
            lng = district.getCenterLng();
            range = district.getMaxRadius();
            List<?> boundList = JsonUtil.convertStringToObject(json, List.class);
            for (Object bound : boundList) {
                String path = (String) bound;
                String arr[] = path.split(";");
                List<LngLat> paths = Lists.newArrayList();
                for (String s : arr) {
                    String a[] = s.split(",");
                    LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                    paths.add(lngLat);
                }
                bounds.add(paths);
            }
        } else if (type == 4) {// 行政区域查询
            List<?> list = (List<?>) area.get("bounds");
            Map<String, Object> center = (Map<String, Object>) area.get("center");
            lat = Double.parseDouble(String.valueOf(center.get("lat")));
            lng = Double.parseDouble(String.valueOf(center.get("lng")));
            List<LngLat> points = Lists.newArrayList();
            for (Object bound : list) {
                String path = (String) bound;
                String arr[] = path.split(";");
                List<LngLat> paths = Lists.newArrayList();
                for (String s : arr) {
                    String a[] = s.split(",");
                    LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                    paths.add(lngLat);
                }
                points.addAll(paths);
                bounds.add(paths);
            }
            points.add(points.get(0));
            double tolerance = 500;
            String level = (String) area.get("level");
            if (level.equals("province")) {
                tolerance = 2000;
            } else if (level.equals("city")) {
                tolerance = 1000;
            }
            points = DouglasPeucker.simplify(points, tolerance);
            range = ShapeUtil.getMaxDistance(new LngLat(lng, lat), points);
        }

        List<String> enterpriseIds = new ArrayList<String>();
        // 查询子公司
        String[] selectFields = new String[] {"id"};
        Map<String, Object> andCondition = new HashMap<String, Object>();
        andCondition.put("parentId.eq", enterpriseId);
        List<?> subsidiarys = this.service.find(Enterprise.ENTITY_NAME, selectFields, "id", false, andCondition);
        if (subsidiarys != null && !subsidiarys.isEmpty()) {
            for (Object o : subsidiarys) {
                enterpriseIds.add((String) o);
            }
        }
        enterpriseIds.add(enterpriseId);
        for (String entId : enterpriseIds) {
            String cacheName = Constants.CACHE_NAME_DEVICE_GPS_GEOHASH_CACHE.concat(String.valueOf(entId));
            List<GeoRange> geoRanges = GeoSearch.range(lat, lng, range);
            for (GeoRange geoRange : geoRanges) {
                List<String> list = this.redisService.zRangeByScore(cacheName, String.valueOf(geoRange.getMin()), String.valueOf(geoRange.getMax()));
                // 再次计算是否在范围内
                if (list != null) {
                    for (String deviceId : list) {
                        try {
                            Map<?, ?> map = this.gpsInfoService.getLastGps(deviceId);
                            if (map == null) {
                                continue;
                            }
                            double lng1 = Double.parseDouble(String.valueOf(map.get("lng")));
                            double lat1 = Double.parseDouble(String.valueOf(map.get("lat")));
                            boolean bool = false;
                            // 精度不够，边缘时会有偏差
                            switch (type) {
                                case 1:
                                    bool = ShapeUtil.isInCircle(lng1, lat1, lng, lat, range);
                                    break;
                                case 2:
                                    bool = PolygonUtil.isPointInPolygon(lng1, lat1, pathList);
                                    break;
                                case 3:
                                    for (List<LngLat> paths : bounds) {
                                        bool = PolygonUtil.isPointInPolygon(lng1, lat1, paths);
                                        if (bool) {
                                            break;
                                        }
                                    }
                                    break;
                            }

                            if (bool) {
                                Map<String, Object> data = Maps.newHashMap();
                                data.put("lng", lng1);
                                data.put("lat", lat1);
                                data.put("direction", map.get("direction"));
                                data.put("deviceId", deviceId);
                                retList.add(data);
                            }
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                }
            }
        }
        return new Result(retList);
    }
}
