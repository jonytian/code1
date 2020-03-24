package com.legaoyi.management.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legaoyi.common.excel.util.ExcelLogs;
import com.legaoyi.common.excel.util.ExcelUtil;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.MappedBiggerFileReader;
import com.legaoyi.common.util.Result;
import com.legaoyi.management.model.Group;
import com.legaoyi.management.util.JeasyuiTree;
import com.legaoyi.management.util.Ztree;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.rest.BaseController;
import com.legaoyi.platform.service.DeviceService;

@RestController("deviceController")
@RequestMapping(produces = {"application/json"})
public class DeviceController extends BaseController {

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Autowired
    @Qualifier("deviceExtendService")
    private ExtendService extendService;

    @RequestMapping(value = "/device/today/online", method = RequestMethod.GET)
    public Result staticTodayOnline(@RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(deviceService.staticTodayOnline(enterpriseId));
    }

    @RequestMapping(value = "/device/{id}/state", method = RequestMethod.GET)
    public Result getStatus(@PathVariable String id) throws Exception {
        String ids[] = id.split(",");
        List<Map<String, Object>> retList = Lists.newArrayList();
        for (String id1 : ids) {
            Map<String, Object> data = new HashMap<String, Object>();
            String gatewayId = this.deviceService.getGateway(id1);
            data.put("id", id1);
            data.put("state", this.deviceService.getStatus(id1, gatewayId));
            retList.add(data);
        }
        return new Result(retList);
    }

    @RequestMapping(value = "/device/{id}/bizState", method = RequestMethod.GET)
    public Result getBizState(@PathVariable String id) throws Exception {
        String ids[] = id.split(",");
        List<Map<String, Object>> retList = Lists.newArrayList();
        for (String id1 : ids) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", id1);
            data.put("bizState", this.deviceService.getBizState(id1));
            retList.add(data);
        }
        return new Result(retList);
    }

    @RequestMapping(value = "/device/group", method = RequestMethod.GET)
    public Result getDeviceGroup(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(defaultValue = "1", required = false) int type, @RequestParam(defaultValue = "false", required = false) boolean isParent) throws Exception {
        Map<String, Object> form = new HashMap<String, Object>();
        form.put("enterpriseId.eq", enterpriseId);
        form.put("type.eq", type);
        List<?> list = this.service.find(Group.ENTITY_NAME, "parentId", false, form);
        return new Result(setJeasyuiTree(list, isParent));
    }

    @RequestMapping(value = "/device/group/ztree", method = RequestMethod.GET)
    public Result getDeviceGroupTree(@RequestHeader(name = "_enterpriseId") String userEnterpriseId, @RequestParam(required = false) String enterpriseId, @RequestParam(defaultValue = "1", required = false) int type,
            @RequestParam(defaultValue = "false", required = false) boolean isParent) throws Exception {
        Map<String, Object> form = new HashMap<String, Object>();
        enterpriseId = StringUtils.isBlank(enterpriseId) ? userEnterpriseId : enterpriseId;
        form.put("enterpriseId.eq", enterpriseId);
        form.put("type.eq", type);
        List<?> list = this.service.find(Group.ENTITY_NAME, "parentId", false, form);
        return new Result(setZtree(list, isParent));
    }

    @GetMapping("/device/{deviceId}/car")
    public Result getDeviceCar(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.deviceService.getDeviceCar(deviceId));
    }

    @GetMapping("/device/unbind")
    public Result queyUnbindDevice(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo, @RequestParam String groupId,
            @RequestParam(name = "enterpriseId", required = false) String postEnterpriseId) throws Exception {
        enterpriseId = StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId) ? postEnterpriseId : enterpriseId;
        return new Result(this.deviceService.queyUnbindDevice(enterpriseId, groupId, pageSize, pageNo));
    }

    @PatchMapping(value = "/car/{carId}/device/{deviceId}")
    public Result bindDevice(@RequestHeader(name = "_enterpriseId") String enterpriseId, @PathVariable String carId, @PathVariable String deviceId) throws Exception {
        this.deviceService.bindDevice(carId, deviceId);
        return new Result();
    }

    @PatchMapping(value = "/car/{carId}/device")
    public Result unbindDevice(@RequestHeader(name = "_enterpriseId") String enterpriseId, @PathVariable String carId) throws Exception {
        this.deviceService.unbindDevice(carId);
        return new Result();
    }

    @RequestMapping(value = "/device/import/result", method = RequestMethod.GET)
    public String downloanImportResult(@RequestHeader(name = "_enterpriseId") String enterpriseId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getServletContext().getRealPath("/");
        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }
        MappedBiggerFileReader reader = null;
        try {
            reader = new MappedBiggerFileReader(path.concat(File.separator).concat(enterpriseId.concat("_import_device_result.xls")));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-xls");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode("设备批量导入失败列表.xls", "UTF-8"));
            ServletOutputStream responseOutputStream = response.getOutputStream();
            while (reader.read() != -1) {
                responseOutputStream.write(reader.getArray());
            }
            responseOutputStream.flush();
        } catch (Exception e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = {"/device/import"}, method = {RequestMethod.POST})
    public Result importDevice(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        ExcelLogs logs = new ExcelLogs();
        Collection<Map> list = ExcelUtil.importExcel(Map.class, file.getInputStream(), "yyyy-MM-dd HH:mm:ss", logs, 0);
        if (logs.getHasError()) {
            return new Result(Result.RESP_CODE_ERROR, "文件格式不正确", null);
        }

        List<Map> errorList = Lists.newArrayList();
        // {鉴权码=123456, 设备名称=测A00001, 设备状态=未注册, 协议版本=2011, 终端ID=1234567,
        // 设备ID=013000000000, 分组名称=福田区}
        if (list != null && !list.isEmpty()) {
            if (list.size() > 1000) {
                return new Result(Result.RESP_CODE_ERROR, "导入终端设备数量，一次最大支持1000条", null);
            }
            List<?> groupList = this.service.findAll(Group.ENTITY_NAME, new String[] {"id", "name"});
            Map<String, String> groupMap = Maps.newHashMap();
            if (groupList != null) {
                for (Object item : groupList) {
                    Object arr[] = (Object[]) item;
                    groupMap.put((String) arr[1], (String) arr[0]);
                }
            }
            Map<String, String> protocolVersionMap = Maps.newHashMap();
            protocolVersionMap.put("2011", "");
            protocolVersionMap.put("2013", "");
            protocolVersionMap.put("2015", "");
            protocolVersionMap.put("2016", "");
            protocolVersionMap.put("201602", "");
            protocolVersionMap.put("2019", "");
            protocolVersionMap.put("tjsatl", "");

            for (Map map : list) {
                String name = String.valueOf(map.get("设备名称"));
                if (StringUtils.isBlank(name) || name.length() > 10) {
                    map.put("错误描述", "设备名称不合法，不能为空并且不能超过10字符");
                    errorList.add(map);
                    continue;
                }

                String protocolVersion = null;
                Object version = map.get("协议版本");
                if (version instanceof String) {
                    protocolVersion = (String) version;
                } else {
                    protocolVersion = String.valueOf(version);
                    int index = protocolVersion.indexOf(".");
                    if (index != -1) {
                        protocolVersion = protocolVersion.substring(0, index);
                    }
                }

                if (StringUtils.isBlank(protocolVersion) || !protocolVersionMap.containsKey(protocolVersion)) {
                    protocolVersion = "2011";
                }

                String simCode = String.valueOf(map.get("设备ID"));
                if (protocolVersion.equals("2019")) {
                    if (StringUtils.isBlank(simCode) || simCode.length() != 20) {
                        map.put("错误描述", "设备ID不合法，不能为空并且值20位，若不足前面补0");
                        errorList.add(map);
                        continue;
                    }
                } else {
                    if (StringUtils.isBlank(simCode) || simCode.length() != 12) {
                        map.put("错误描述", "设备ID不合法，不能为空并且值12位，若不足前面补0");
                        errorList.add(map);
                        continue;
                    }
                }

                Object authCode = map.get("鉴权码");
                if (authCode == null || StringUtils.isBlank(String.valueOf(authCode))) {
                    authCode = getRandomString(7);
                } else if (String.valueOf(authCode).length() > 16) {
                    map.put("错误描述", "鉴权码不合法，不能超过16位字母或者数字");
                    errorList.add(map);
                    continue;
                }
                String stateStr = String.valueOf(map.get("设备状态"));
                int state = (StringUtils.isBlank(stateStr) || !stateStr.equals("已注册")) ? 0 : 1;

                String groupId = String.valueOf(map.get("分组名称"));
                if (!StringUtils.isBlank(groupId)) {
                    groupId = groupMap.get(groupId);
                }

                Device device = new Device();
                device.setEnterpriseId(enterpriseId);
                device.setName(name);
                device.setAuthCode(String.valueOf(authCode));
                device.setGroupId(groupId);
                device.setProtocolVersion(protocolVersion);
                device.setSimCode(simCode);
                device.setState((short) state);
                try {
                    this.deviceService.save(device);
                } catch (Exception e) {
                    logger.error("import device error,info={}", map, e);
                    if (e instanceof BizProcessException) {
                        map.put("错误描述", ((BizProcessException) e).getMessage());
                    } else {
                        map.put("错误描述", "未知错误");
                    }
                    errorList.add(map);
                }
            }
        }

        if (!errorList.isEmpty()) {
            Map<String, String> header = new LinkedHashMap<>();
            header.put("设备名称", "设备名称");
            header.put("设备ID", "设备ID");
            header.put("协议版本", "协议版本");
            header.put("分组名称", "分组名称");
            header.put("鉴权码", "鉴权码");
            header.put("设备状态", "设备状态");
            header.put("错误描述", "错误描述");
            String path = request.getServletContext().getRealPath("/");
            if (!path.endsWith(File.separator)) {
                path = path.concat(File.separator);
            }
            logger.info("errorList file path:{}", path);
            OutputStream out = null;
            try {
                File f = new File(path.concat(File.separator).concat(enterpriseId.concat("_import_device_result.xls")));
                out = new FileOutputStream(f);
                ExcelUtil.exportExcel(header, errorList, out);
                Map<String, Object> result = Maps.newHashMap();
                result.put("file", f.getName());
                result.put("success", list.size() - errorList.size());
                result.put("failure", errorList.size());
                return new Result(Result.RESP_CODE_ERROR, null, result);
            } catch (Exception e) {
                logger.error("create device import result error", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        return new Result("导入成功");
    }

    private static String string = "abcdefghijklmnopqrstuvwxyz";

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private JeasyuiTree setJeasyuiTree(List<?> list, boolean isParent) {
        String parentId = "0";
        // 根节点
        JeasyuiTree root = new JeasyuiTree();
        root.setId(parentId);
        root.setText("根节点");
        root.setState(JeasyuiTree.TREE_STATE_OPEN);
        root.addAttributes("isParent", true);
        if (list == null || list.isEmpty()) {
            return root;
        }

        Group group = null;
        List<JeasyuiTree> children = new ArrayList<JeasyuiTree>();
        List<JeasyuiTree> treeList = new ArrayList<JeasyuiTree>();
        Map<String, List<JeasyuiTree>> map = new HashMap<String, List<JeasyuiTree>>();
        for (Object o : list) {
            group = (Group) o;
            String id = group.getId();
            JeasyuiTree tree = new JeasyuiTree();
            tree.setId(id);
            tree.setText(group.getName());
            if (!parentId.equals(group.getParentId())) {
                map.put(parentId, children);
                parentId = group.getParentId();
                children = new ArrayList<JeasyuiTree>();
            }
            children.add(tree);
            treeList.add(tree);
        }
        if (!children.isEmpty()) {
            map.put(parentId, children);
        }
        for (JeasyuiTree tree : treeList) {
            List<JeasyuiTree> children1 = map.get(tree.getId());
            if (isParent || children1 != null && !children1.isEmpty()) {
                tree.setChildren(children1);
                tree.setState(JeasyuiTree.TREE_STATE_CLOSED);
                tree.addAttributes("isParent", true);
            }
        }

        root.setChildren(map.get("0"));
        return root;
    }

    private List<Ztree> setZtree(List<?> list, boolean isParent) {
        String parentId = "0";
        Group group = null;
        List<Ztree> children = new ArrayList<Ztree>();
        List<Ztree> treeList = new ArrayList<Ztree>();
        Map<String, List<Ztree>> map = new HashMap<String, List<Ztree>>();
        for (Object o : list) {
            group = (Group) o;
            String id = group.getId();
            Ztree tree = new Ztree();
            tree.setId(id);
            tree.setName(group.getName());
            tree.setType("group");
            if (!parentId.equals(group.getParentId())) {
                map.put(parentId, children);
                parentId = group.getParentId();
                children = new ArrayList<Ztree>();
            }
            children.add(tree);
            treeList.add(tree);
        }
        if (!children.isEmpty()) {
            map.put(parentId, children);
        }
        for (Ztree tree : treeList) {
            List<Ztree> children1 = map.get(tree.getId());
            if (isParent || children1 != null && !children1.isEmpty()) {
                tree.setChildren(children1);
            }
        }
        return map.get("0");
    }
}
