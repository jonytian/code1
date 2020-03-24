package com.legaoyi.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-01-18
 */
@Entity(name = "car")
@Table(name = "car")
@XmlRootElement
public class Car extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "car";

    public static Short CAR_STATE_DISABLED = 0;

    public static Short CAR_STATE_FREE = 1;

    public static Short CAR_STATE_WAITING = 2;

    public static Short CAR_STATE_USING = 3;

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

    /** 车辆归属省份 **/
    @Column(name = "province_code")
    private String provinceCode;

    /** 车辆归属市区 **/
    @Column(name = "city_code")
    private String cityCode;

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

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIconId() {
        return this.iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public Integer getComfortIndex() {
        return this.comfortIndex;
    }

    public void setComfortIndex(Integer comfortIndex) {
        this.comfortIndex = comfortIndex;
    }

    public Short getState() {
        return this.state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getColor() {
        return this.color;
    }

    public void setColor(Short color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return this.plateNumber;
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
        return this.plateColor;
    }

    public void setPlateColor(Short plateColor) {
        this.plateColor = plateColor;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProductionDate() {
        return this.productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getVin() {
        return this.vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNo() {
        return this.engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Short getBrandType() {
        return this.brandType;
    }

    public void setBrandType(Short brandType) {
        this.brandType = brandType;
    }

    public Short getBrandModel() {
        return this.brandModel;
    }

    public void setBrandModel(Short brandModel) {
        this.brandModel = brandModel;
    }

    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getSeats() {
        return this.seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getInitialMileage() {
        return this.initialMileage;
    }

    public void setInitialMileage(Integer initialMileage) {
        this.initialMileage = initialMileage;
    }

    public Integer getTankCapacity() {
        return this.tankCapacity;
    }

    public void setTankCapacity(Integer tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getBizType() {
        return this.bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getVerificationDate() {
        return this.verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getInsuranceDate() {
        return this.insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getAcquisitionDate() {
        return this.acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getRetirementDate() {
        return this.retirementDate;
    }

    public void setRetirementDate(String retirementDate) {
        this.retirementDate = retirementDate;
    }

    public String getFileNo() {
        return this.fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getDrivingLicense() {
        return this.drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getLicenseDate() {
        return this.licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseImgId() {
        return this.licenseImgId;
    }

    public void setLicenseImgId(String licenseImgId) {
        this.licenseImgId = licenseImgId;
    }

    public String getInsuranceCompany() {
        return this.insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

}
