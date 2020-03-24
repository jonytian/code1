package com.legaoyi.platform.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "carDevice")
@Table(name = "v_car_device")
@XmlRootElement
public class CarDevice implements Serializable {

    private static final long serialVersionUID = -8115720901642476217L;

    public static final String ENTITY_NAME = "carDevice";

    @Id
    @Column(name = "id", length = 32)
    private String carId;

    @Column(name = "enterprise_id")
    private String enterpriseId;

    /** 部门id **/
    @Column(name = "dept_id")
    private String deptId;

    /** 分组id **/
    @Column(name = "group_id")
    private String groupId;

    /** 车载设备id **/
    @Column(name = "device_id")
    private String deviceId;

    /** 车辆图片id **/
    @Column(name = "icon_id")
    private String iconId;

    /** 车辆舒适度指数,0~100 **/
    @Column(name = "comfort_index")
    private Integer comfortIndex;

    /** 状态：0:停用；1：空闲；2：使用中；3：维修中 **/
    @Column(name = "state")
    private Short state = 1;

    /** 车辆颜色 **/
    @Column(name = "color")
    private Short color;

    /** 车牌号 **/
    @Column(name = "plate_number")
    private String plateNumber;

    /** 车牌号类型 **/
    @Column(name = "plate_type")
    private String plateType;

    /** 车牌颜色 **/
    @Column(name = "plate_color")
    private Short plateColor = 1;

    /** 生产日期 **/
    @Column(name = "production_date")
    private String productionDate;

    /** 车架号 **/
    @Column(name = "vin")
    private String vin;

    /** 发动机号 **/
    @Column(name = "engine_no")
    private String engineNo;

    /*** 车辆品牌 */
    @Column(name = "brand_type")
    private Short brandType;

    /*** 品牌型号 */
    @Column(name = "brand_model")
    private Short brandModel;

    /** 车辆类型 **/
    @Column(name = "type")
    private Short type;

    /** 核载人数 **/
    @Column(name = "seats")
    private Integer seats = 5;

    /** 初始里程 **/
    @Column(name = "initial_mileage")
    private Integer initialMileage;

    /** 油箱容积 **/
    @Column(name = "tank_capacity")
    private Integer tankCapacity;

    /** 整车质量 **/
    @Column(name = "weight")
    private Integer weight;

    /** 业务类型 **/
    @Column(name = "biz_type")
    private Integer bizType;

    /** 年审日期 **/
    @Column(name = "verification_date")
    private String verificationDate;

    /** 保险到期日期 **/
    @Column(name = "insurance_date")
    private String insuranceDate;

    /** 购置日期 **/
    @Column(name = "acquisition_date")
    private String acquisitionDate;

    /** 报废日期 **/
    @Column(name = "retirement_date")
    private String retirementDate;

    /** 档案编号 **/
    @Column(name = "file_no")
    private String fileNo;

    /** 行驶证 **/
    @Column(name = "driving_license")
    private String drivingLicense;

    /** 行驶证到期日期 **/
    @Column(name = "driving_lic_date")
    private String licenseDate;

    /** 行驶证照片 **/
    @Column(name = "driving_lic_img_id")
    private String licenseImgId;

    /** 保险公司 **/
    @Column(name = "insurance_company")
    private String insuranceCompany;

    /** 备注 **/
    @Column(name = "remark")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    protected Date createTime = new Date();

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    /** sim卡号 **/
    @Column(name = "sim_code")
    private String simCode;

    /** 终端协议版本 **/
    @Column(name = "protocol_version")
    private String protocolVersion;

    /** 视频协议版本 **/
    @Column(name = "vedio_protocol")
    private String vedioProtocol;

    /** 视频通道 **/
    @Column(name = "vedio_channel")
    private Integer vedioChannel;

    /** 设备状态：0:未注册；1:已注册；2:离线；3:在线；4:已注销；5：已停用 **/
    @Column(name = "device_state")
    private Short deviceState;

    /** 业务状态：0：离线；1：行驶；2：停车；3：熄火；4：无信号 **/
    @Column(name = "biz_state")
    private Short bizState;

    /** 终端最后上线时间 **/
    @Column(name = "last_online_time")
    private Long lastOnlineTime;

    /** 终端最后下线时间 **/
    @Column(name = "last_offline_time")
    private Long lastOfflineTime;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public Integer getComfortIndex() {
        return comfortIndex;
    }

    public void setComfortIndex(Integer comfortIndex) {
        this.comfortIndex = comfortIndex;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getColor() {
        return color;
    }

    public void setColor(Short color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public Short getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(Short plateColor) {
        this.plateColor = plateColor;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Short getBrandType() {
        return brandType;
    }

    public void setBrandType(Short brandType) {
        this.brandType = brandType;
    }

    public Short getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(Short brandModel) {
        this.brandModel = brandModel;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getInitialMileage() {
        return initialMileage;
    }

    public void setInitialMileage(Integer initialMileage) {
        this.initialMileage = initialMileage;
    }

    public Integer getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(Integer tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(String retirementDate) {
        this.retirementDate = retirementDate;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseImgId() {
        return licenseImgId;
    }

    public void setLicenseImgId(String licenseImgId) {
        this.licenseImgId = licenseImgId;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getVedioProtocol() {
        return vedioProtocol;
    }

    public void setVedioProtocol(String vedioProtocol) {
        this.vedioProtocol = vedioProtocol;
    }

    public Integer getVedioChannel() {
        return vedioChannel;
    }

    public void setVedioChannel(Integer vedioChannel) {
        this.vedioChannel = vedioChannel;
    }

    public Short getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(Short deviceState) {
        this.deviceState = deviceState;
    }

    public Short getBizState() {
        return bizState;
    }

    public void setBizState(Short bizState) {
        this.bizState = bizState;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

}
