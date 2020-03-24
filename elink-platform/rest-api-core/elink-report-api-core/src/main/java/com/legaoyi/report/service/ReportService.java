package com.legaoyi.report.service;

import java.util.List;
import java.util.Map;

/**
 * @author gaoshengbo
 */
public interface ReportService {

    /**
     * 按天统计企业车辆的告警类型
     *
     * @param date yyyyMMdd格式
     * @param enterpriseId 企业id
     * @return List
     * @throws Exception
     */
    public List<?> enterpriseDailyAlarmOverview(String date, String enterpriseId) throws Exception;

    /**
     * 按天统计企业车辆的告警类型
     *
     * @param date yyyyMMdd格式
     * @param enterpriseId 企业id
     * @param deviceId 企业id
     * @return List
     * @throws Exception
     */
    public List<?> deviceDailyAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception;

    /**
     * 按月统计车辆的历史告警类型
     *
     * @param enterpriseId 企业id
     * @param deviceId 终端设备id
     * @param date
     * @param pageNo
     * @param pageSize
     * @return List
     * @throws Exception
     */
    public List<?> deviceDailyAlarmOverviewList(String date, String enterpriseId, String deviceId, int pageNo, int pageSize) throws Exception;

    /**
     * 按月统计企业车辆的历史告警类型
     *
     * @param date yyyyMM格式
     * @param enterpriseId 企业id
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> enterpriseMonthAlarmOverview(String date, String enterpriseId, boolean isParent) throws Exception;

    /**
     * 按月统计车辆的历史告警类型
     *
     * @param enterpriseId 企业id
     * @param deviceId 终端设备id
     * @param date
     * @return List
     * @throws Exception
     */
    public List<?> deviceMonthAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception;

    /**
     * 按月统计车辆的历史告警类型
     *
     * @param enterpriseId 企业id
     * @param deviceId 终端设备id
     * @param date
     * @param pageNo
     * @param pageSize
     * @return List
     * @throws Exception
     */
    public List<?> deviceMonthAlarmOverviewList(String date, String enterpriseId, String deviceId, int pageNo, int pageSize) throws Exception;

    /**
     * 按月统计车辆每天的里程、油耗
     *
     * @param deviceId 终端设备id
     * @param date
     * @return List
     * @throws Exception
     */
    public List<?> monthGpsInfoOverview(String date, String deviceId) throws Exception;

    /**
     * 按天统计企业车辆的历史告警类型
     *
     * @param date yyyyMMdd格式
     * @param enterpriseId 企业id
     * @return List
     * @throws Exception
     */
    public List<?> enterpriseHistoryDailyAlarmOverview(String date, String enterpriseId) throws Exception;

    /**
     * 按天统计企业车辆的历史告警类型
     *
     * @param date yyyyMMdd格式
     * @param enterpriseId 企业id
     * @param deviceId 设备id
     * @return List
     * @throws Exception
     */
    public List<?> deviceHistoryDailyAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @return List
     * @throws Exception
     */
    public List<?> deviceOverView(String enterpriseId) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> deviceTypeOverView(String enterpriseId, boolean isParent) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> deviceBizTypeOverView(String enterpriseId, boolean isParent) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> deviceTypeAndBizTypeOverView(String enterpriseId, boolean isParent) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> carBiztypeOverView(String enterpriseId, boolean isParent) throws Exception;

    /***
     * 企业车辆协议版本统计
     * @param enterpriseId
     * @param isParent
     * @return
     * @throws Exception
     */
    public List<?> deviceProtocolOverView(String enterpriseId, boolean isParent) throws Exception;
    
    /**
     * 企业车辆分布统计（省份）
     * 
     * @param enterpriseId
     * @param isParent
     * @return
     * @throws Exception
     */
    public List<?> provinceCarOverView(String enterpriseId, boolean isParent) throws Exception;

    /**
     * 企业车辆分布统计（城市）
     * 
     * @param enterpriseId
     * @param isParent
     * @return
     * @throws Exception
     */
    public List<?> cityCarOverView(String enterpriseId, boolean isParent, String provinceCode) throws Exception;

    /**
     * 企业车辆统计
     *
     * @param enterpriseId
     * @param isParent
     * @return List
     * @throws Exception
     */
    public List<?> carBiztypeAndBizStateOverView(String enterpriseId, boolean isParent) throws Exception;

    /**
     * 企业用车费用统计
     *
     * @param date
     * @param enterpriseId
     * @return Map
     * @throws Exception
     */
    public Map<?, ?> enterpriseMonthOfficersCarCostOverview(String date, String enterpriseId) throws Exception;

    /**
     * 车辆用车费用统计
     *
     * @param date
     * @param enterpriseId
     * @param deviceId
     * @return Map
     * @throws Exception
     */
    public Map<?, ?> deviceMonthOfficersCarCostOverview(String date, String enterpriseId, String deviceId) throws Exception;

    /**
     * 统计字段总和
     * 
     * @param entityName
     * @param date
     * @param selectFields
     * @param enterpriseId
     * @param form
     * @param isParent
     * @return
     * @throws Exception
     */
    public Map<?, ?> sumByEnterprise(String entityName, String date, String[] selectFields, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception;

    /**
     * sumFields 相加并按groupFields求和
     * 
     * @param entityName
     * @param date
     * @param group
     * @param sumFields
     * @param enterpriseId
     * @param pageSize
     * @param pageNo
     * @param form
     * @param isParent
     * @return
     * @throws Exception
     */
    public List<?> addAndSumByEnterprise(String entityName, String date, String group, String[] sumFields, int pageSize, int pageNo, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception;

    /**
     * 统计记录总数
     * 
     * @param entityName
     * @param date
     * @param enterpriseId
     * @param form
     * @param isParent
     * @return
     * @throws Exception
     */
    public Map<?, ?> countByEnterprise(String entityName, String date, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception;

    /**
     * distinct count for the gaven filed “distinct”
     * 
     * @param entityName
     * @param date
     * @param distinct
     * @param enterpriseId
     * @param form
     * @param isParent
     * @return
     * @throws Exception
     */
    public Map<?, ?> distinctCountByEnterprise(String entityName, String date, String distinct, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception;

    /**
     * 系统提醒消息统计
     * 
     * @param enterpriseId
     * @param userId
     * @return
     * @throws Exception
     */
    public List<?> alarmNotifyOverview(String enterpriseId, String userId) throws Exception;
}
