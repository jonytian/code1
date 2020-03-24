package com.reacheng.rc.controller.api;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.entity.*;
import com.reacheng.rc.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@Api(value = "Api接口", tags = "Api接口", description = "设备连接相关的接口")
@RequestMapping("/api")
@RestController
@Slf4j
public class ApiController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private AppLogService appLogService;
    @Autowired
    private WhitelistService whitelistService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    AppService appService;

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @ApiOperation(value = "检查更新", notes = "检查apk更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "应用包名", dataType = "String"),
            @ApiImplicitParam(name = "imei", value = "imei编号", dataType = "String"),
            @ApiImplicitParam(name = "projectName", value = "项目名", dataType = "String")
    })
    @RequestMapping(value = "check_updates", method = RequestMethod.POST)
    public synchronized String checkUpdates(
            @RequestParam(value = "imei", required = false) String imei,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "packageName", required = false) String packageName) {

        log.info("检查更新接口请求参数>>> imei:" + imei + "projectName:" + projectName + "packageName:" + packageName);
        /**返回结果集*/
        Map<String, Object> result = new HashMap<>(16);
        result.put("imei", imei);
        result.put("errorCode", 0);
        result.put("elapsedTime", new Date().getTime());
        List list = new ArrayList();

        /**IMEI格式判断正确才采集*/
        if (imei.length()==15) {
            if (isLetterDigit(imei)){
            /**自动采集项目*/
            if (!StringUtils.isEmpty(projectName)) {
                synchronized (this) {
                    Project project = projectService.findByName(projectName);
                    if (project == null) {
                        Project project_new = new Project();
                        project_new.setProjectName(projectName);
                        project_new.setCreateTime(new Date());
                        projectService.save(project_new);
                        log.info("数据采集-项目新增成功");
                    }
                }
            }
            /**自动采集设备*/
            Project project1 = projectService.findByName(projectName);
            if (!StringUtils.isEmpty(imei)) {
                synchronized (this) {
                    Device device = deviceService.findByImei(imei);
                    if (device == null) {
                        Device device_new = new Device();
                        device_new.setImei(imei);
                        device_new.setCreateTime(new Date());
                        device_new.setProject(project1);
                        deviceService.save(device_new);
                        log.info("数据采集-设备信息新增成功");
                    }
                }
            }


        /**新的设备采集进系统，如果当前设备，当前项目下，有应用是全部发布状态，将此设备自动插入到白名单*/
        List<Application> applications = appService.findByProjectName(projectName);
        applications.stream().forEach(o -> {
            if(o.getAllPush()!=null){
                if(o.getAllPush()==1){

                    Whitelist whitelist = whitelistService.findByImeiAndAppId(imei,o.getId());
                    if(whitelist != null){
                        whitelist.setImei(imei);
                        whitelist.setStatus("安装");
                        whitelist.setAppId(o.getId());
                        whitelist.setApplication(o);
                        whitelist.setCreateTime(new Date());
                        whitelist.setProjectId(o.getProject().getId());
                        Whitelist whitelist_new = whitelistService.update(whitelist);
                        if (whitelist_new != null) {
                            log.info("更新白名单信息-" +imei);
                        }
                    }else{
                        //新增
                        Whitelist whitelist1 = new Whitelist();
                        whitelist1.setImei(imei);
                        whitelist1.setStatus("安装");
                        whitelist1.setAppId(o.getId());
                        whitelist1.setApplication(o);
                        whitelist1.setCreateTime(new Date());
                        whitelist1.setProjectId(o.getProject().getId());
                        Whitelist whitelist_new = whitelistService.save(whitelist1);
                        if (whitelist_new != null) {
                            log.info("新增白名单信息-" +imei);
                        }
                    }

                }
            }

        });


        /**查询白名单列表的app*/
        List<Whitelist> whitelists = null;
        if (StringUtils.isEmpty(packageName)){
            whitelists = whitelistService.findByImeiAndProjectId(imei,project1.getId());
        }else {
            whitelists = whitelistService.findByImeiAndPackageNameAndProjectId(imei, packageName,project1.getId());
        }
        /**查询测试类型的app*/
        List<Application> testApps = null;
        if (StringUtils.isEmpty(packageName)){
           testApps = appService.findByAll(projectName,imei);
        }else {
           testApps = appService.findByAll(packageName, projectName,imei);
        }


        /**查询白名单里面的应用*/
        whitelists.stream().forEach(o -> {
            ApplicationVO applicationVO = new ApplicationVO();
            applicationVO.setPackageName(o.getApplication().getPackageName());
            applicationVO.setClassName(o.getApplication().getClassName());
            applicationVO.setType(o.getApplication().getType());
            applicationVO.setDownloadUrl(o.getApplication().getDownloadUrl());
            applicationVO.setMd5(o.getApplication().getMd5());
            applicationVO.setVersionCode(o.getApplication().getVersionCode());
            if ("卸载".equals(o.getStatus())) {
                applicationVO.setVersionCode(0);
            }
            list.add(applicationVO);
        });
        /**查询测试类型的app*/
        testApps.stream().forEach(o -> {
            ApplicationVO applicationVO = new ApplicationVO();
            applicationVO.setPackageName(o.getPackageName());
            applicationVO.setClassName(o.getClassName());
            applicationVO.setType(o.getType());
            applicationVO.setDownloadUrl(o.getDownloadUrl());
            applicationVO.setMd5(o.getMd5());
            applicationVO.setVersionCode(o.getVersionCode());
            if ("卸载".equals(o.getStatus())) {
                applicationVO.setVersionCode(0);
            }
            list.add(applicationVO);
        });
            }
        }

        /**重复应用去重*/
        List list_new = removeDuplicate(list);
        result.put("updateBeans", list_new);
        log.info("检查更新接口成功返回结果:" + result.toString());
        return JSON.toJSONString(result);
    }
    // list去重
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @ApiOperation(value = "应用状态回调", notes = "应用状态回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "应用包名", dataType = "String"),
            @ApiImplicitParam(name = "imei", value = "imei编号", dataType = "String"),
            @ApiImplicitParam(name = "projectName", value = "项目名", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String")
    })
    @RequestMapping(value = "report_status", method = RequestMethod.POST)
    public String feedback(
            @RequestParam(value = "imei", required = false) String imei,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "packageName", required = false) String packageName,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "versionCode", required = false) String versionCode
    ) {

        log.info("回调接口请求参数:imei:" + imei + "projectName:" + projectName + "packageName:" + packageName + "status:" + status + "versionCode:" + versionCode);
        Map<String, Object> result = new HashMap<>(16);
        if(StringUtils.isEmpty(imei)||StringUtils.isEmpty(projectName)||StringUtils.isEmpty(versionCode)){
            result.put("code", 200);
            result.put("msg", "参数有误");
            return JSON.toJSONString(result);
        }
        AppLog appLog_old = appLogService.findAppInfo(imei,projectName,packageName,versionCode);

        if (!StringUtils.isEmpty(status)){
            if ("0".equals(status)) {
                status = "安装成功";
            }
            if ("1".equals(status)) {
                status = "下载失败";
            }
            if ("2".equals(status)) {
                status = "MD5错误";
            }
            if ("3".equals(status)) {
                status = "安装失败";
            }
            if ("4".equals(status)) {
                status = "删除失败";
            }
            if ("5".equals(status)) {
                status = "删除成功";
            }
        }
        if (appLog_old == null) {

            AppLog appLog = new AppLog();
            appLog.setImei(imei);
            appLog.setPackageName(packageName);
            appLog.setProjectName(projectName);
            appLog.setVersionCode(versionCode);
            appLog.setStatus(status);
            appLog.setCreateTime(new Date());
            appLog = appLogService.save(appLog);
            String msg = appLog == null ? "fail" : "success";

            result.put("code", 200);
            result.put("msg", msg);
            log.info("回调接口成功返回结果:" + result.toString());
        }else {

            if(!appLog_old.getStatus().equals( status)){
                appLog_old.setCreateTime(new Date());
                appLog_old.setStatus(status);
                appLog_old.setVersionCode(versionCode);
                appLogService.update(appLog_old);
                result.put("code", 200);
                result.put("msg", "更新成功");
                log.info("回调接口成功返回结果:" + result.toString());
            }

        }

        return JSON.toJSONString(result);

    }


}
