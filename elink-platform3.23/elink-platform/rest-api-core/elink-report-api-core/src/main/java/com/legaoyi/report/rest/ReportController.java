package com.legaoyi.report.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.legaoyi.common.excel.util.ExcelUtil;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.common.util.MappedBiggerFileReader;
import com.legaoyi.common.util.Result;
import com.legaoyi.persistence.jpa.exception.IllegalEntityException;
import com.legaoyi.persistence.jpa.exception.IllegalEntityFieldException;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.rest.BaseController;
import com.legaoyi.report.service.ReportService;

/**
 * @author gaoshengbo
 */
@RestController("reportController")
@RequestMapping(produces = {"application/json"})
public class ReportController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    @Qualifier("reportService")
    private ReportService reportService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @RequestMapping(value = "/enterpriseDailyAlarmOverview/{date}", method = RequestMethod.GET)
    public Result enterpriseDailyAlarmOverview(@PathVariable String date, @RequestParam(required = false) String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        if (StringUtils.isBlank(deviceId)) {
            return new Result(this.reportService.enterpriseDailyAlarmOverview(date, enterpriseId));
        }
        return new Result(this.reportService.deviceDailyAlarmOverview(date, enterpriseId, deviceId));
    }

    @RequestMapping(value = "/deviceDailyAlarmOverviewList/{date}", method = RequestMethod.GET)
    public Result deviceDailyAlarmOverviewList(@PathVariable String date, @RequestParam(required = false) String deviceId, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo,
            @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.reportService.deviceDailyAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize));
    }

    @RequestMapping(value = "/enterpriseHistoryDailyAlarmOverview/{date}", method = RequestMethod.GET)
    public Result enterpriseHistoryDailyAlarmOverview(@PathVariable String date, @RequestParam(required = false) String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        if (StringUtils.isBlank(deviceId)) {
            return new Result(this.reportService.enterpriseHistoryDailyAlarmOverview(date, enterpriseId));
        }
        return new Result(this.reportService.deviceHistoryDailyAlarmOverview(date, enterpriseId, deviceId));
    }

    @RequestMapping(value = "/enterpriseMonthAlarmOverview/{date}", method = RequestMethod.GET)
    public Result enterpriseMonthAlarmOverview(@PathVariable String date, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.enterpriseMonthAlarmOverview(date, enterpriseId, isParent));
    }

    @RequestMapping(value = "/deviceMonthAlarmOverview/{deviceId}/{date}", method = RequestMethod.GET)
    public Result deviceMonthAlarmOverview(@PathVariable String deviceId, @PathVariable String date, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.reportService.deviceMonthAlarmOverview(date, enterpriseId, deviceId));
    }

    @RequestMapping(value = "/deviceMonthAlarmOverviewList/{date}", method = RequestMethod.GET)
    public Result deviceMonthAlarmOverviewList(@PathVariable String date, @RequestParam(required = false) String deviceId, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo,
            @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.reportService.deviceMonthAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize));
    }

    @RequestMapping(value = "/monthGpsInfoOverview/{deviceId}/{date}", method = RequestMethod.GET)
    public Result monthGpsInfoOverview(@PathVariable String deviceId, @PathVariable String date) throws Exception {
        return new Result(this.reportService.monthGpsInfoOverview(date, deviceId));
    }

    @RequestMapping(value = "/deviceOverView", method = RequestMethod.GET)
    public Result deviceOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.reportService.deviceOverView(enterpriseId));
    }

    @GetMapping(value = "/deviceBizTypeOverView")
    public Result deviceBizTypeOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.deviceBizTypeOverView(enterpriseId, isParent));
    }

    @GetMapping(value = "/deviceTypeOverView")
    public Result deviceTypeOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.deviceTypeOverView(enterpriseId, isParent));
    }
    
    @GetMapping(value = "/deviceProtocolOverView")
    public Result deviceProtocolOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.deviceProtocolOverView(enterpriseId, isParent));
    }

    @GetMapping(value = "/deviceTypeAndBizTypeOverView")
    public Result deviceTypeAndBizTypeOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.deviceTypeAndBizTypeOverView(enterpriseId, isParent));
    }

    @GetMapping(value = "/provinceCarOverView")
    public Result provinceCarOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.provinceCarOverView(enterpriseId, isParent));
    }

    @GetMapping(value = "/cityCarOverView")
    public Result cityCarOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestParam String provinceCode) throws Exception {
        return new Result(this.reportService.cityCarOverView(enterpriseId, isParent, provinceCode));
    }

    @GetMapping(value = "/carBiztypeOverView")
    public Result carBiztypeOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.carBiztypeOverView(enterpriseId, isParent));
    }

    @GetMapping(value = "/carBiztypeAndBizStateOverView")
    public Result carBiztypeAndBizStateOverView(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        return new Result(this.reportService.carBiztypeAndBizStateOverView(enterpriseId, isParent));
    }

    /**
     * 条件查询
     *
     * @param entityName
     * @param select
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param countable
     * @param enterpriseId
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/query/{entityName}"}, method = {RequestMethod.POST})
    public Result query(@PathVariable String entityName, @RequestParam(required = false) String select, @RequestParam(required = false) String orderBy, @RequestParam(required = false) boolean desc, @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(required = false) boolean countable, @RequestParam(required = false) boolean isParent, @RequestHeader(name = "_enterpriseId") String enterpriseId,
            @RequestBody Map<String, Object> form) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }

        // 要求每个entity都必须包含enterpriseId属性，否则报错
        boolean isExistEnterpriseId = false;
        try {
            isExistEnterpriseId = this.service.isExistField(entityName, "enterpriseId");
        } catch (IllegalEntityException e) {
            isExistEnterpriseId = true;
        } catch (IllegalEntityFieldException e) {
            isExistEnterpriseId = false;
        }

        if (isExistEnterpriseId) {
            boolean bool = false;
            for (String key : form.keySet()) {
                if (key.startsWith("enterpriseId.")) {// 是否下级企业
                    String postEnterpriseId = (String) form.get(key);
                    if (StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId)) {
                        bool = true;
                        break;
                    }
                }
            }
            if (!bool) {
                if (isParent) {
                    String postEnterpriseId = (String) form.get("enterpriseId");
                    // 上级企业可以查询下级企业数据
                    enterpriseId = StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId) ? postEnterpriseId : enterpriseId;
                    form.put("enterpriseId.rlike", enterpriseId);
                } else {
                    form.put("enterpriseId.eq", enterpriseId);
                }
            }
        }

        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            return new Result(extendService.query(selectFields, orderBy, desc, pageSize, pageNo, countable, form));
        }

        if (countable) {
            return new Result(this.service.pageFind(entityName, selectFields, orderBy, desc, pageSize, pageNo, form));
        }
        return new Result(this.service.find(entityName, selectFields, orderBy, desc, pageSize, pageNo, form));
    }

    @GetMapping(value = {"/sum/{entityName}/{date}"})
    public Result sum(@PathVariable String entityName, @PathVariable String date, @RequestParam String select, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        } else {
            return new Result(Result.RESP_CODE_ERROR, "参数不合法！", null);
        }

        return new Result(this.reportService.sumByEnterprise(entityName, date, selectFields, enterpriseId, null, isParent));
    }

    @PostMapping(value = {"/sum/{entityName}"})
    public Result sum(@PathVariable String entityName, @RequestParam String select, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestBody Map<String, Object> form)
            throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        } else {
            return new Result(Result.RESP_CODE_ERROR, "参数不合法！", null);
        }
        String recordDate = (String) form.get("recordDate");
        return new Result(this.reportService.sumByEnterprise(entityName, recordDate, selectFields, enterpriseId, form, isParent));
    }

    @PostMapping(value = {"/count/{entityName}"})
    public Result count(@PathVariable String entityName, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestBody Map<String, Object> form) throws Exception {
        String recordDate = (String) form.get("recordDate");
        return new Result(this.reportService.countByEnterprise(entityName, recordDate, enterpriseId, form, isParent));
    }

    @PostMapping(value = {"/addAndsum/{entityName}"})
    public Result addAndsum(@PathVariable String entityName, @RequestParam String sum, @RequestParam String group, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "1") int pageNo,
            @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestBody Map<String, Object> form) throws Exception {
        if (StringUtils.isBlank(sum) || StringUtils.isBlank(group)) {
            return new Result(Result.RESP_CODE_ERROR, "参数不合法！", null);
        }

        String[] sumFields = sum.split(",");
        String recordDate = (String) form.get("recordDate");
        return new Result(this.reportService.addAndSumByEnterprise(entityName, recordDate, group, sumFields, pageSize, pageNo, enterpriseId, form, isParent));
    }

    @PostMapping(value = {"/distinctCount/{entityName}"})
    public Result distinctCount(@PathVariable String entityName, @RequestParam String distinct, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestBody Map<String, Object> form)
            throws Exception {
        String recordDate = (String) form.get("recordDate");
        return new Result(this.reportService.distinctCountByEnterprise(entityName, recordDate, distinct, enterpriseId, form, isParent));
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthOnlineTime/{date}", method = RequestMethod.GET)
    public String exportMonthOnlineTime2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("deviceId.eq", deviceId);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("onlineTimeDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        long hour = 1000 * 60 * 60;
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    long time = Long.parseLong(String.valueOf(map.get(key)));
                    map.put(key, String.format("%.2f", ((double) time / (double) hour)));
                }
            }
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Long total = (Long) map.get("total");
            if (total == null) {
                total = 0L;
            }
            map.put("total", String.format("%.2f", ((double) total / (double) hour)));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("total", "总计(小时)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_online_time.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("在线时长统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthAccTime/{date}", method = RequestMethod.GET)
    public String exportMonthAccTime2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("deviceId.eq", deviceId);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("accTimeDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        long hour = 1000 * 60 * 60;
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    long time = Long.parseLong(String.valueOf(map.get(key)));
                    map.put(key, String.format("%.2f", ((double) time / (double) hour)));
                }
            }
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Long total = (Long) map.get("total");
            if (total == null) {
                total = 0L;
            }
            map.put("total", String.format("%.2f", ((double) total / (double) hour)));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("total", "总计(小时)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_online_time.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("行驶时长统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthParkingTime/{date}", method = RequestMethod.GET)
    public String exportMonthParkingTime2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("deviceId.eq", deviceId);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("parkingTimeDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        long hour = 1000 * 60 * 60;
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    long time = Long.parseLong(String.valueOf(map.get(key)));
                    map.put(key, String.format("%.2f", ((double) time / (double) hour)));
                }
            }
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Long total = (Long) map.get("total");
            if (total == null) {
                total = 0L;
            }
            map.put("total", String.format("%.2f", ((double) total / (double) hour)));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("total", "总计(小时)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_parking_time.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("停车时长统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthOilmass/{date}", method = RequestMethod.GET)
    public String exportMonthOilmassDetail2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("deviceId.eq", deviceId);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("gpsInfoDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "totalOilmass", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "totalOilmass", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    Map<?, ?> item = (Map<?, ?>) map.get(key);
                    Double oilmass = (Double) item.get("oilmass");
                    if (oilmass != null) {
                        map.put(key, String.format("%.2f", oilmass));
                    } else {
                        map.put(key, "-");
                    }
                }
            }
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Double totalOilmass = (Double) map.get("totalOilmass");
            if (totalOilmass == null) {
                totalOilmass = 0d;
            }
            map.put("totalOilmass", String.format("%.2f", totalOilmass));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("totalOilmass", "总计(L)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_oilmass.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("月度油耗统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthMileage/{date}", method = RequestMethod.GET)
    public String exportMonthMileageDetail2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("deviceId.eq", deviceId);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("gpsInfoDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "totalMileage", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "totalMileage", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    Map<?, ?> item = (Map<?, ?>) map.get(key);
                    Double mileage = (Double) item.get("mileage");
                    if (mileage != null) {
                        map.put(key, String.format("%.0f", mileage));
                    } else {
                        map.put(key, "-");
                    }
                }
            }
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Double totalMileage = (Double) map.get("totalMileage");
            if (totalMileage == null) {
                totalMileage = 0d;
            }
            map.put("totalMileage", String.format("%.0f", totalMileage));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("totalMileage", "总计(km)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_mileage.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("月度里程统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/oilmass/{date}", method = RequestMethod.GET)
    public String exportMonthOilmass2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }

        int pageSize = 1000;
        int pageNo = 1;
        Map<String, Object> form = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(deviceId)) {
            form.put("deviceId.eq", deviceId);
        }

        String entityName = "gpsInfoDayReport";
        String[] selectFields = new String[] {"deviceId", "totalMileage", "totalOilmass", "totalAvgOilmass"};
        ExtendService extendService = getExtendService(entityName);

        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(selectFields, "totalMileage", true, pageSize, pageNo, false, form);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(selectFields, "totalMileage", true, pageSize, pageNo, false, form);
            exportList.addAll(list);
        }

        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        for (Map<String, Object> map : exportList) {
            deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }

            Double totalMileage = (Double) map.get("totalMileage");
            if (totalMileage == null) {
                totalMileage = 0d;
            }
            map.put("totalMileage", String.format("%.2f", totalMileage));
            Double totalOilmass = (Double) map.get("totalOilmass");
            if (totalOilmass == null) {
                totalOilmass = 0d;
            }
            map.put("totalOilmass", String.format("%.2f", totalOilmass));
            String avgOilmass = "-";
            if (totalOilmass > 0 && totalMileage > 0) {
                avgOilmass = String.format("%.2f", totalOilmass / totalMileage);
            }
            map.put("avgOilmass", avgOilmass);
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("totalMileage", "总里程(km)");
        header.put("totalOilmass", "总油耗(L)");
        header.put("avgOilmass", "平均油耗(L/km)");

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_month_oilmass.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("油耗统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthAlarm/{date}", method = RequestMethod.GET)
    public String exportMonthAlarm2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }

        int pageSize = 1000;
        int pageNo = 1;

        List<Map<String, Object>> list = (List<Map<String, Object>>) this.reportService.deviceMonthAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) this.reportService.deviceMonthAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize);
            exportList.addAll(list);
        }

        formatAlarmExcelDatalList(exportList);

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_month_alarm.xls")));
        exportExcel(file, getAlarmExcelHeader(), exportList);
        outputStream(file, date.concat("告警统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/todayAlarm/{date}", method = RequestMethod.GET)
    public String exportTodayAlarm2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }

        int pageSize = 1000;
        int pageNo = 1;

        List<Map<String, Object>> list = (List<Map<String, Object>>) this.reportService.deviceDailyAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) this.reportService.deviceDailyAlarmOverviewList(date, enterpriseId, deviceId, pageNo, pageSize);
            exportList.addAll(list);
        }

        formatAlarmExcelDatalList(exportList);

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_month_alarm.xls")));
        exportExcel(file, getAlarmExcelHeader(), exportList);
        outputStream(file, date.concat("告警统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/dailyAlarm/{date}", method = RequestMethod.GET)
    public String exportDailyAlarm2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String deviceId, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }

        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date.substring(0, 6));
        condition.put("enterpriseId.eq", enterpriseId);
        Map<String, Object> conditions = Maps.newHashMap();
        if (!StringUtils.isBlank(deviceId)) {
            conditions.put("deviceId.eq", deviceId);
        }
        conditions.put("date.eq", Integer.parseInt(date));
        condition.put("conditions", conditions);
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("alarmDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "deviceId", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "deviceId", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }

        formatAlarmExcelDatalList(exportList);

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_month_alarm.xls")));
        exportExcel(file, getAlarmExcelHeader(), exportList);
        outputStream(file, date.concat("告警统计结果.xls"), response);
        return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/excel/monthAttendance/{date}", method = RequestMethod.GET)
    public String exportMonthAttendance2Excel(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String qualification, @PathVariable String date, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("recordDate", date);
        condition.put("enterpriseId.eq", enterpriseId);
        if (!StringUtils.isBlank(qualification)) {
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("qualification.eq", qualification);
            condition.put("conditions", conditions);
        }
        int pageSize = 1000;
        int pageNo = 1;
        ExtendService extendService = getExtendService("attendanceDayReport");
        List<Map<String, Object>> list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
        List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
        exportList.addAll(list);
        while (list != null && list.size() == pageSize) {
            pageNo++;
            list = (List<Map<String, Object>>) extendService.query(null, "total", true, pageSize, pageNo, false, condition);
            exportList.addAll(list);
        }
        String lastDay = DateUtils.getMonthLastDay(new SimpleDateFormat("yyyyMM").parse(date));
        int max = Integer.parseInt(lastDay.split("-")[2]);
        long hour = 1000 * 60 * 60;
        for (Map<String, Object> map : exportList) {
            for (int i = 1; i <= max; i++) {
                String key;
                if (i < 10) {
                    key = "d0" + i;
                } else {
                    key = "d" + i;
                }
                if (!map.containsKey(key)) {
                    map.put(key, "-");
                } else {
                    long time = Long.parseLong(String.valueOf(map.get(key)));
                    map.put(key, String.format("%.2f", ((double) time / (double) hour)));
                }
            }
            qualification = (String) map.get("qualification");
            // 获得司机名称
            // Map<String, Object> condition1 = Maps.newHashMap();
            // condition1.put("qualCert.eq", qualification);
            // List<?> drivers = this.service.find(Driver.ENTITY_NAME, null, false, condition1);
            // if (drivers != null && !drivers.isEmpty()) {
            // Driver driver = (Driver) drivers.get(0);
            // map.put("driverName", driver.getName());
            // }

            Long total = (Long) map.get("total");
            if (total == null) {
                total = 0L;
            }
            map.put("total", String.format("%.2f", ((double) total / (double) hour)));
        }

        Map<String, String> header = new LinkedHashMap<>();
        header.put("driverName", "司机名称");
        header.put("qualification", "从业资格证");
        header.put("total", "总计(小时)");
        for (int i = 1; i <= max; i++) {
            String val;
            if (i < 10) {
                val = "0" + i;
            } else {
                val = "" + i;
            }
            header.put("d".concat(val), val);
        }

        File file = new File(path.concat(File.separator).concat(enterpriseId.concat(date).concat("_attendance.xls")));
        exportExcel(file, header, exportList);
        outputStream(file, date.concat("司机考勤统计结果.xls"), response);
        return null;
    }

    @GetMapping(value = "/car/alarmNotifyOverview")
    public Result alarmNotifyOverview(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestHeader(name = "_userId") String userId) throws Exception {
        return new Result(this.reportService.alarmNotifyOverview(enterpriseId, userId));
    }

    private Map<String, String> getAlarmExcelHeader() {
        Map<String, String> header = new LinkedHashMap<>();
        header.put("deviceName", "设备名称");
        header.put("total", "总计");
        header.put("a32", "原地设防");
        header.put("a20", "围栏告警");
        header.put("a1", "超速告警");
        header.put("a21", "路线告警");
        header.put("a2", "疲劳驾驶");
        header.put("a0", "紧急报警");
        header.put("a11", "摄像头故障");
        header.put("a25", "油量异常");
        header.put("a26", "车辆被盗");
        header.put("a27", "非法点火");
        header.put("a30", "侧翻预警");
        header.put("a29", "碰撞预警");
        header.put("a31", "非法开车门");
        header.put("a7", "终端主电源欠压");
        header.put("a24", "车辆VSS");
        header.put("other", "其他");
        return header;
    }

    private void formatAlarmExcelDatalList(List<Map<String, Object>> list) throws Exception {
        ExtendService deviceExtendService = getExtendService(Device.ENTITY_NAME);
        for (Map<String, Object> map : list) {
            String deviceId = (String) map.get("deviceId");
            Device device = (Device) deviceExtendService.get(deviceId);
            if (device != null) {
                map.put("deviceName", device.getName());
            } else {
                map.put("deviceName", "终端已被删除");
            }
            int total = 0;
            for (int i = 0; i < 50; i++) {
                Integer val = (Integer) map.get("a" + i);
                if (val != null) {
                    total += val;
                }
            }
            map.put("total", total);

            int[] a = new int[] {1, 32, 33, 34, 35, 36, 37, 39, 2, 0, 11, 21, 23, 25, 26, 27, 30, 29, 31, 7, 24, 20};
            int other = total;
            for (int i = 0; i < a.length; i++) {
                Integer val = (Integer) map.get("a" + a[i]);
                if (val != null) {
                    other -= val;
                }
            }
            map.put("other", other);

            a = new int[] {20, 33, 34};
            resetAlarm(a, map);

            a = new int[] {1, 35, 36, 39};
            resetAlarm(a, map);

            a = new int[] {21, 23, 37, 38};
            resetAlarm(a, map);
        }
    }

    private void resetAlarm(int[] a, Map<String, Object> map) {
        int total = 0;
        for (int i = 0; i < a.length; i++) {
            Integer val = (Integer) map.get("a" + a[i]);
            if (val != null) {
                total += val;
            }
        }
        map.put("a" + a[0], total);
    }

    private void exportExcel(File file, Map<String, String> header, List<Map<String, Object>> dataList) throws Exception {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            ExcelUtil.exportExcel(header, dataList, out);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {

            }
        }
    }

    private void outputStream(File file, String attachmentName, HttpServletResponse response) {
        MappedBiggerFileReader reader = null;
        try {
            reader = new MappedBiggerFileReader(file);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-xls");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(attachmentName, "UTF-8"));
            ServletOutputStream responseOutputStream = response.getOutputStream();
            while (reader.read() != -1) {
                responseOutputStream.write(reader.getArray());
            }
            responseOutputStream.flush();
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
                file.delete();
            }
        }
    }
}
