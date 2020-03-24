package com.legaoyi.common.util;

/**
 * @author gaoshengbo
 */
public class Constants {

    /** 请求成功 **/
    public static final short SUCCESS = 0;

    /** 统繁忙，此时请开发者稍候再试 **/
    public static final short SYSTEM_ERROR = -1;

    /** 非法请求 **/
    public static final short INVALID_REQUEST = 9;

    /** 开放平台accessToken缓存,缓存key："access_token_cache:"+AccessToken.tokenId，缓存内容类型：AccessToken **/
    public static final String CACHE_NAME_ACCESS_TOKEN_CACHE = "access_token_cache";

    /** 开放平台refreshToken缓存,缓存key："refresh_token_cache:"+RefreshToken.tokenId，缓存内容类型：RefreshToken **/
    public static final String CACHE_NAME_REFRESH_TOKEN_CACHE = "refresh_token_cache";

    /** 监管平台鉴权用户信息缓存，缓存key:"aas_user_cache:"+userAccount，缓存内容类型：Map **/
    public static final String CACHE_NAME_AAS_USER_CACHE = "aas_user_cache";

    /** 监管平台用户信息缓存，缓存key:"user_info_cache:"+userId，缓存内容类型：User **/
    public static final String CACHE_NAME_USER_INFO_CACHE = "user_info_cache";

    /** 监管平台用户角色信息缓存，缓存key:"user_role_cache:"+userId，缓存内容类型：Map **/
    public static final String CACHE_NAME_USER_ROLE_CACHE = "user_role_cache";

    /**
     * 开放平台API频率控制配置缓存，缓存key："client_api_limit_config_cache:"+ClientApiLimitConfig.clientId，缓存内容类型：List<ClientApiLimitConfig>
     **/
    public static final String CACHE_NAME_CLIENT_API_LIMIT_CONFIG_CACHE = "client_api_limit_config_cache";

    /** 开放平台客户端配置缓存,缓存key："client_detail_cache:"+ClientDetail.clientId，缓存内容类型：ClientDetail **/
    public static final String CACHE_NAME_CLIENT_DETAIL_CACHE = "client_detail_cache";

    /**
     * 开放平台API默认频率控制配置缓存，缓存key："global_api_limit_config_cache:"+GlobalApiLimitConfig.apiId，缓存内容类型：GlobalApiLimitConfig
     **/
    public static final String CACHE_NAME_GLOBAL_API_LIMIT_CONFIG_CACHE = "global_api_limit_config_cache";

    /** 开放平台白名单配置缓存，缓存key："api_ip_white_list_cache:"+clientId，缓存内容类型：List<String> **/
    public static final String CACHE_NAME_API_IP_WHITE_LIST_CACHE = "api_ip_white_list_cache";

    /** 开放平台API列表缓存，缓存key："api_list_cache:api_list",缓存内容类型：List<String[]> **/
    public static final String CACHE_NAME_API_LIST_CACHE = "api_list_cache";

    /** 开放平台scope缓存，缓存key："api_scope_cache:"+clientId+"_"+apiId+"_"+scope,缓存内容类型：boolean **/
    public static final String CACHE_NAME_API_SCOPE_CACHE = "api_scope_cache";

    /** 开放平台ip黑名单缓存，缓存key："api_ip_black_list_cache:api_ip_black_list"，内容类型：List<String> **/
    public static final String CACHE_NAME_API_IP_BLACK_LIST_CACHE = "api_ip_black_list_cache";

    /** 开放平台动态路由规则缓存，缓存key："zuul_route_rule_cache:"+ZuulRouteRule.id，内容类型：List<ZuulRouteRule> **/
    public static final String CACHE_NAME_ZUUL_ROUTE_RULE_CACHE = "zuul_route_rule_cache";

    public static final String CACHE_NAME_API_VALIDATE_SCHEMA_CACHE = "api_validate_schema_cache";

    public static final String CACHE_NAME_QUERY_CONDITION_SCHEMA_CACHE = "query_condition_schema_cache";

    /**
     * 终端设备信息缓存，包括两种类型的缓存；缓存key："device_info_cache:"+终端设备simCode/终端设备id，内容类型：Map<String,Object>/Device
     **/
    public static final String CACHE_NAME_DEVICE_INFO_CACHE = "device_info_cache";
    
    /**
     * 终端设备信息缓存，包括两种类型的缓存；缓存key："car_info_cache:"+终端设备车辆id，内容类型：Map<String,Object>/Car
     **/
    public static final String CACHE_NAME_CAR_INFO_CACHE = "car_info_cache";

    /** 终端设备位置信息缓存，存放终端设备最后一条位置信息，缓存key："device_gps_cache:"+终端设备id，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_DEVICE_GPS_CACHE = "device_gps_cache";

    /**
     * 终端设备位置geohash编码缓存，存放经纬度geohash编码信息，缓存key："device_gps_geohash_cache:"+企业id，内容类型：geohash编码的有序集合
     **/
    public static final String CACHE_NAME_DEVICE_GPS_GEOHASH_CACHE = "device_gps_geohash_cache:";

    /** 终端设备上下线状态缓存，缓存key："device_state_cache_"+网关id+":"+终端设备id，内容类型：integer **/
    public static final String CACHE_NAME_DEVICE_STATE_CACHE = "device_state_cache_";

    /** 终端设备连接的网关缓存，缓存内容为网关id，缓存key："device_gateway_cache:"+终端设备id，内容类型：String **/
    public static final String CACHE_NAME_DEVICE_GATEWAY_CACHE = "device_gateway_cache";
    
    /** 终端设备在线时长统计缓存，缓存key："device_online_time_static_cache":"+终端设备id，内容类型：integer **/
    public static final String CACHE_NAME_DEVICE_ONLINE_TIME_STATIC_CACHE = "device_online_time_static_cache";

    /** 终端设备状态状态缓存，缓存key："device_biz_state_cache:"+终端设备id，内容类型：integer **/
    public static final String CACHE_NAME_DEVICE_BIZ_STATE_CACHE = "device_biz_state_cache";

    /** 平台告警设置缓存，缓存key："device_gateway_cache:"+终端设备id，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_DEVICE_ALARM_SETTING_CACHE = "device_alarm_setting_cache";

    /** 上次平台告警缓存，缓存key："device_gateway_cache:"+终端设备id，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_LAST_ALARM_CACHE = "last_alarm_cache";

    /** 终端设备下行消息流水号缓存，缓存key："device_down_message_seq:"+终端设备simCode，内容类型：long **/
    public static final String CACHE_NAME_DEVICE_DOWN_MESSAGE_SEQ_CACHE_PREFIX = "device_down_message_seq:";

    /** 企业一级菜单导航缓存，缓存key："aas_navigation_cache:"+企业id，内容类型：List **/
    public static final String CACHE_NAME_AAS_NAVIGATION_CACHE = "aas_navigation_cache";

    /** 用户一级菜单导航缓存，缓存key："aas_navigation_role_cache:"+roleId，内容类型：List **/
    public static final String CACHE_NAME_AAS_NAVIGATION_ROLE_CACHE = "aas_navigation_role_cache";

    /** 企业菜单导航缓存，缓存key："aas_menu_cache:"+企业id，内容类型：List **/
    public static final String CACHE_NAME_AAS_MENU_CACHE = "aas_menu_cache";

    /** 企业菜单导航缓存，缓存key："aas_menu_role_cache:"+roleId，内容类型：List **/
    public static final String CACHE_NAME_AAS_MENU_ROLE_CACHE = "aas_menu_role_cache";

    /** 终端设备视频监控连接的客户端，缓存key："device_video_list_cache"，内容类型：设备simCode组成的Set集合 **/
    public static final String CACHE_NAME_DEVICE_VIDEO_LIST_CACHE = "device_video_list_cache";

    /** 终端设备视频监控连接的客户端，缓存key："device_video_client_cache:"+simCode，内容类型：客户端id组成的Set集合 **/
    public static final String CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE = "device_video_client_cache:";

    /** 终端设备视频监控连接的客户端的心跳信息，缓存key："device_video_client_heartbeat_cache:"+clientId，内容类型：long **/
    public static final String CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE = "device_video_client_heartbeat_cache";

    /** 终端设备视频监控状态信息，缓存key："device_video_info_cache:"+simCode，内容类型：json **/
    public static final String CACHE_NAME_DEVICE_VIDEO_INFO_CACHE = "device_video_info_cache";

    /** 终端设备视频序号(按天存储)，缓存key："device_video_seq_cache:"+simCode_通道号，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_DEVICE_VIDEO_SEQ_CACHE = "device_video_seq_cache";

    /** 终端设备音频序号(按天存储)，缓存key："device_audio_seq_cache:"+simCode_通道号，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_DEVICE_AUDIO_SEQ_CACHE = "device_audio_seq_cache";

    /** 企业权限配置缓存，缓存key：enterpriseId，内容类型：Map<String,Object> **/
    public static final String CACHE_NAME_ENTERPRISE_CONFIG_CACHE = "enterprise_config_cache";

    /** 企业权限配置缓存，缓存key：enterpriseId+"_"+type，内容类型：List<String> **/
    public static final String CACHE_NAME_SYSTEM_CONFIG_CACHE = "system_config_cache";

    /** 视频网关当前连接客户端数量，缓存key："video_gateway_client_count:"+网关ip，内容类型：long **/
    public static final String CACHE_NAME_VIDEO_GATEWAY_CLIENT_COUNT_CACHE_PREFIX = "video_gateway_client_count:";

    /** 流量统计缓存，按天存储，有效期为一天，缓存key："device_data_count_cache:"+日期+_+simCode，内容类型：Map **/
    public static final String CACHE_NAME_DEVICE_DATA_COUNT_CACHE = "device_data_count_cache";

    /** 车辆当前司机缓存，缓存key："device_current_driver_cache:"+终端设备id，内容类型：string (司机从业资格证：qualification) **/
    public static final String CACHE_NAME_DEVICE_CURRENT_DRIVER_CACHE = "device_current_driver_cache";

    /** 终端离线指令缓存，set,缓存key："device_command_cache"，内容类型：simCode **/
    public static final String CACHE_NAME_DEVICE_COMMAND_CACHE = "device_command_cache";
    
    /** 字典缓存，set,缓存key："device_dictionary_cache"+code **/
    public static final String CACHE_NAME_DICTIONARY_CACHE = "device_dictionary_cache";


}
