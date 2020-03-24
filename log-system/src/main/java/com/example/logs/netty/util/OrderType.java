package com.example.logs.netty.util;


public class OrderType {

    /** 通用回复 */
    public static final String SEND_MSG = "10000";

    /** 产生异常指令 */
    public static final String SEND_EXCEPTION = "10001";

    /** 发送重启指令 */
    public static final String SEND_RESTART = "10002";

    /** 发送下载指令 */
    public static final String SEND_DOWNLOAD = "10003";

    /** 开启或关闭mtklog */
    public static final String SEND_OC_MTKLOG = "10004";

    /** 配置mtklogconfig */
    public static final String SEND_SET_MTKCONFIG = "10005";

    /** 开始/停止抓mtklog */
    public static final String SEND_GRAB_MTKLOG = "10006";

    /** 清除终端MTKlog */
    public static final String SEND_CLEAR_MTKLOG = "10007";

    /** 获取终端MTK log文件列表 */
    public static final String SEND_GET_LOGFILE = "10008";


}
