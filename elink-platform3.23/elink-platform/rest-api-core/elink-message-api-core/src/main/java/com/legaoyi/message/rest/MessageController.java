package com.legaoyi.message.rest;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Result;
import com.legaoyi.message.service.MessageService;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.rest.BaseController;
import com.legaoyi.platform.service.DeviceService;

@RestController("messageController")
@RequestMapping(produces = {"application/json"})
public class MessageController extends BaseController {

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @RequestMapping(value = "/heartbeat/{clinetId}/{messageId}", method = RequestMethod.GET)
    public Result heartbeat(@PathVariable String clinetId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        this.messageService.setHeartbeat(clinetId, messageId);
        return new Result();
    }

    @RequestMapping(value = "/video/808/{deviceId}", method = RequestMethod.POST)
    public Result startJtt808Video(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam String clinetId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.start808Video(enterpriseId, deviceId, clinetId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/video/808/{deviceId}", method = RequestMethod.DELETE)
    public Result stopJtt808Video(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam String clinetId, @RequestParam String channelId) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            this.messageService.stop808Video(enterpriseId, deviceId, channelId, clinetId, gatewayId);
            return new Result();
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/talk/1078/{deviceId}", method = RequestMethod.POST)
    public Result startLiveTalk(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam String clinetId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.start1078Talk(enterpriseId, deviceId, clinetId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/video/1078/{deviceId}", method = RequestMethod.POST)
    public Result startLiveVideo(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam String clinetId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.start1078Video(enterpriseId, deviceId, clinetId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/video/1078/{deviceId}", method = RequestMethod.PUT)
    public Result stopLiveVideo(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam String clinetId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            this.messageService.stop1078Video(enterpriseId, deviceId, clinetId, messageBody, gatewayId);
            return new Result();
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/deviceDownMessage/{deviceId}/{messageId}", method = RequestMethod.POST)
    public Result sendMessage(@PathVariable String deviceId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.send(enterpriseId, deviceId, messageId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/deviceDownMessage/fence/{deviceId}/{messageId}", method = RequestMethod.POST)
    public Result sendFenceMessage(@PathVariable String deviceId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.setFence(enterpriseId, deviceId, messageId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/deviceDownMessage/fence/{deviceId}/{messageId}/{ids}", method = RequestMethod.DELETE)
    public Result sendDelFenceMessage(@PathVariable String deviceId, @PathVariable String messageId, @PathVariable String ids, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        if (StringUtils.isBlank(ids)) {
            throw new BizProcessException("非法请求");
        }
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }

            return new Result(this.messageService.delFence(enterpriseId, deviceId, messageId, ids.split(","), gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    @RequestMapping(value = "/batch/deviceDownMessage/{messageId}", method = RequestMethod.POST)
    public Result batchSendMessage(@PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        List<?> list = messageService.batchSend(enterpriseId, messageId, messageBody);
        return new Result(list);
    }

    @RequestMapping(value = "/deviceDownMessage/{id}/state", method = RequestMethod.GET)
    public Result getDeviceDownMessageState(@PathVariable String id, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        String[] ids = id.split(",");
        if (ids.length > 10) {
            throw new BizProcessException("每次最多允许查询10条消息的状态");
        }
        return new Result(this.messageService.getDeviceDownMessageState(enterpriseId, ids));
    }

    /**
     * 强制终端下线
     * 
     * @param deviceId
     * @param enterpriseId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/device/offline/{deviceId}", method = RequestMethod.DELETE)
    public Result forceOffline(@PathVariable String deviceId, @RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (!StringUtils.isBlank(gatewayId)) {
                int status = this.deviceService.getStatus(deviceId, gatewayId);
                if (status == 3) {
                    this.messageService.forceOffline(device.getSimCode(), gatewayId);
                }
            }
            return new Result();
        }
        throw new BizProcessException("非法请求");
    }

    /**
     * 解除网关黑名单
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gateway/blacklist/{ids}", method = RequestMethod.DELETE)
    public Result removeBlacklist(@PathVariable String ids) throws Exception {
        String[] arr = ids.split(",");
        for (String id : arr) {
            messageService.removeBlacklist(id);
        }
        return new Result();
    }

    /**
     * 上传文件指令
     * 
     * @param deviceId
     * @param messageId
     * @param enterpriseId
     * @param messageBody
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/message/uploadFile/{deviceId}/{messageId}", method = RequestMethod.POST)
    public Result uploadFile(@PathVariable String deviceId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.uploadFile(enterpriseId, deviceId, messageId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    /**
     * 上传文件指令
     * 
     * @param deviceId
     * @param messageId
     * @param enterpriseId
     * @param messageBody
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/message/uploadControl/{deviceId}/{messageId}", method = RequestMethod.POST)
    public Result uploadControl(@PathVariable String deviceId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.uploadControl(enterpriseId, deviceId, messageId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }

    /**
     * 历史音视频资源查询指令
     * 
     * @param deviceId
     * @param messageId
     * @param enterpriseId
     * @param messageBody
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/message/fileResource/{deviceId}/{messageId}", method = RequestMethod.POST)
    public Result queryFileResource(@PathVariable String deviceId, @PathVariable String messageId, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> messageBody) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
            String gatewayId = this.deviceService.getGateway(deviceId);
            if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                throw new BizProcessException("车辆不在线");
            }
            return new Result(this.messageService.queryFileResource(enterpriseId, deviceId, messageId, messageBody, gatewayId));
        }
        throw new BizProcessException("非法请求");
    }
}
