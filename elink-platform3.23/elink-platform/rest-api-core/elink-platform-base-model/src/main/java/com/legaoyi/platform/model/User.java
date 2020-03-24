package com.legaoyi.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "user")
@Table(name = "security_user")
@XmlRootElement
public class User extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "user";

    public static final short STATE_DISABLED = 0;

    public static final short STATE_ENABLE = 1;

    public static final short STATE_VISITOR = 2;

    public static final short STATE_EXPIRED = -1;

    /** 平台超级管理员 **/
    public static final short TYPE_SYSTEM_ADMIN = 1;

    /** 企业管理员 **/
    public static final short TYPE_ENTERPRISE_ADMIN = 2;

    /** 企业普通用户 **/
    public static final short TYPE_ENTERPRISE_USER = 3;

    /** 平台超级管理员账号 **/
    public static final String USER_ACCOUNT_ADMIN = "admin";

    /** 所属部门组织id **/
    @Column(name = "org_id")
    private String orgId;

    /** 所属部门id **/
    @Column(name = "dept_id")
    private String deptId;

    /** 员工姓名 **/
    @Column(name = "name")
    private String name;

    /** 员工昵称 **/
    @Column(name = "nick_name")
    private String nickName;

    /** 员工工号 **/
    @Column(name = "emp_id")
    private String empId;

    /** 性别 **/
    @Column(name = "gender")
    private Short gender;

    /** 员工职位 **/
    @Column(name = "title")
    private String title;

    /** 员工生日 **/
    @Column(name = "birthday")
    private String birthday;

    /** 员工电话 **/
    @Column(name = "phone")
    private String phone;

    /** 员工qq **/
    @Column(name = "qq")
    private String qq;

    /** 登陆账号 **/
    @Column(name = "account")
    private String account;

    /** 驾驶证号码 **/
    @Column(name = "drv_license")
    private String drvLicense;

    /** 驾驶证档案编号 **/
    @Column(name = "drv_file_no")
    private String drvFileNo;

    /** 员工头像 **/
    @Column(name = "avatar_url")
    private String avatarUrl;

    /** 国家 **/
    @Column(name = "country")
    private String country;

    /** 省份 **/
    @Column(name = "province")
    private String province;

    /** 城市 **/
    @Column(name = "city")
    private String city;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "union_id")
    private String unionId;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "salt")
    private String salt;

    /** 状态：0:禁用；1:启用；2:离职 **/
    @Column(name = "state")
    private Short state = STATE_ENABLE;

    /** 账号类型：1:超级管理员；2：企业管理员；3：企业员工；4：司机 **/
    @JsonIgnore
    @Column(name = "type")
    private Short type = TYPE_ENTERPRISE_USER;

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDrvLicense() {
        return drvLicense;
    }

    public void setDrvLicense(String drvLicense) {
        this.drvLicense = drvLicense;
    }

    public String getDrvFileNo() {
        return drvFileNo;
    }

    public void setDrvFileNo(String drvFileNo) {
        this.drvFileNo = drvFileNo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
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

}
