package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.dao.DeviceRepository;
import com.reacheng.rc.entity.*;
import com.reacheng.rc.service.*;
import com.reacheng.rc.tools.ExcelUtils;
import com.reacheng.rc.tools.GetServerRealPathUnit;
import com.reacheng.rc.tools.Tools;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;

@Controller
@RequestMapping(value = "admin/app")
@Slf4j
public class AppController extends BaseController {


    protected final static String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/uploads/";

    @Autowired
    private AppService appService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AppLogService appLogService;

    @Autowired
    private WhitelistService whitelistService;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AppInfoService appInfoService;


    @Autowired
    private  StorageService storageService;

    @Autowired
    private IMailService mailService;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    UserService userService;



    private Integer pubAppId;

    private Integer delAppId;

    List<AppVO> dataList = null;
    List pubListExp = new ArrayList();
    List delListExp = new ArrayList();

    @GetMapping("")
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        String projectName = request.getParameter("projectName");
        String status = request.getParameter("status");
        if ("0".equals(status)){
            status = "";
        }
        Sort sort = new Sort(Sort.Direction.ASC, "id");
//        Page<Application> apps = appService.findAllByLikeName("appName", keywords, page, 10, sort);
        page = page==0?1:page;
        Page<Application> apps = appService.findAllApp(keywords,projectName,status,page-1 ,10);
        System.out.println(JSON.toJSONString(apps));
        model.addAttribute("list", apps);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        model.addAttribute("projectName", projectName);
        model.addAttribute("status", status);



        List<Application> applications = apps.getContent();
        dataList = new ArrayList<>();
        applications.stream().forEach(o->{
            AppVO appVO = new AppVO();
            appVO.setAppName(o.getAppName());
            appVO.setPackageName(o.getPackageName());
            appVO.setProjectName(o.getProject().getProjectName());
            appVO.setMd5(o.getMd5());
            appVO.setStatus(o.getStatus());
            appVO.setClassName(o.getClassName());
            appVO.setClassType(o.getClassType());
            appVO.setDownloadUrl(o.getDownloadUrl());
            appVO.setHandleStatus(o.getHandleStatus());
            appVO.setType(o.getType());
            appVO.setVersionCode(o.getVersionCode());
            appVO.setVersion(o.getVersion());
            appVO.setTestImei(o.getTestImei());
            dataList.add(appVO);
        });


        User user = (User) request.getSession().getAttribute("admin");
        String roleName = user.getGadmin().getGname();
        model.addAttribute("roleName", roleName);
       return "admin/app-list";
    }



    /**
     * 下载数据
     * @return 返回excel数据
     */
    @RequestMapping(value = "/exportData", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "下载数据", httpMethod = "GET", produces = "application/json;charset=UTF-8")
    public void exportData(HttpServletResponse response){
        long t1 = System.currentTimeMillis();
        ExcelUtils.writeExcel(response,dataList,AppVO.class);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }



    @GetMapping("getData")
    @ResponseBody
    public Page<Application> getData( HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<Application> apps = appService.findAllByLikeName("packageName", keywords, page, 10, sort);
        System.out.println(JSON.toJSONString(apps));
        return apps;
    }



    @GetMapping("/add")
    public String addUser(Model model){
        List<Project> pros = projectService.findAll();
        List<AppInfo> apps = appInfoService.findAll();
        model.addAttribute("pros", pros);
        model.addAttribute("apps", apps);
        return "admin/app-add";
    }

    @GetMapping("/edit/{application}")
    public String editDevice(Application application, Model model){
        List<Project> pros = projectService.findAll();
        List<AppInfo> apps = appInfoService.findAll();
        model.addAttribute("pros", pros);
        model.addAttribute("app",application);
        model.addAttribute("apps", apps);
        return "admin/app-edit";
    }

    @DeleteMapping("/destroy/{application}")
    @ResponseBody
    public String destroy(Application application){
        this.adminLog("删除-"+application.getAppName());
        try{
            appService.delete(application);
            String upload = GetServerRealPathUnit.getPath("upload");
            String filePath = upload  ;
            deleteServerFile(filePath,application.getFilename());
        }catch (Exception e){
            System.out.println(e);
            return this.outPutErr("请先删除关联的数据");
        }
        return this.outPutData("删除成功");
    }

    /**
     * 删除服务上的文件
     * @param filePath 路径
     * @param fileName 文件名
     * @return
     */
    public static boolean deleteServerFile(String filePath, String fileName) {
        boolean delete_flag = false;
        File file = new File(filePath + fileName);
        if (file.exists() && file.isFile() && file.delete())
            delete_flag = true;
        else
            delete_flag = false;
        return delete_flag;
    }

    @PostMapping("/save")
    @ResponseBody
    public String savePro(@Valid Application application, BindingResult result ,@RequestParam("file") MultipartFile file,HttpServletRequest request)  {

        String newFileName = null;
        try{
            String fileName = file.getOriginalFilename();//文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//文件后缀
             newFileName = UUID.randomUUID().toString() + suffixName;
            System.out.println(newFileName);
            String filePath1= uploadFile(file,newFileName);
            System.out.println(filePath1);
        }catch (Exception e){
            return this.outPutData("保存文件失败："+e);
        }
        System.out.println("file name=" + file.getName());
        System.out.println("origin file name=" + file.getOriginalFilename());
        System.out.println("file size=" + file.getSize());


        if (result.hasErrors()) {//验证
            List <ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        }else{//验证通过
            if(application.getId()!=null){
                application.setUpdateTime(new Date());
                Application application_new = appService.update(application);
                if(application_new!=null){
                    this.adminLog("修改应用信息-"+application.getAppName());
                    return this.outPutData("保存成功");
                }
            }else{



                AppInfo appInfo = appInfoService.findByName(application.getAppName());
                String[] proIds = application.getIds();
                if (proIds!=null||proIds.length!=0){
                    for (String string : proIds) {
                        System.out.println(string);
                        Application application1 = new Application();
                        //新增
                        application1.setAppName(application.getAppName());
                        application1.setFilename(newFileName);
                        application1.setCreateTime(new Date());
                        application1.setStatus("待审核");
                        application1.setType(application.getType());
                        application1.setDownloadUrl("/download/"+newFileName);
                        application1.setPackageName(appInfo.getPackageName());
                        application1.setClassName(application.getClassName());
                        application1.setVersionCode(application.getVersionCode());
                        application1.setVersion(application.getVersion());
                        Project project = projectService.get(Integer.valueOf(string));
                        application1.setProject(project);
                        application1.setClassType(application.getClassType());
                        application1.setTestImei(application.getTestImei());
                        if(application.getClassType() == 0){
                            application1.setType(-1);
                        }
                       if(application1.getType()==null){
                           return this.outPutData("参数错误,type不能为空！");
                       }

                        String upload = GetServerRealPathUnit.getPath("upload");
                        String filePath = upload  + newFileName;
                        try {
                            FileInputStream fis = new FileInputStream(new File(filePath));
                            if(StringUtils.isEmpty(application.getUpMd5())){
                                return this.outPutData("上传失败,输入的MD5不能为空");
                            }
                            if(!application.getUpMd5().equals(Tools.getFileMD5(fis))){
                                return this.outPutData("上传失败,输入的MD5不正确");
                            }
                            application1.setMd5(application.getUpMd5());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        try {
                        Application application111 = appService.findByPackageNameAndProjectNameAndVersion(application1.getPackageName(),project.getId(),application.getVersion());
                        if (application111 != null ){
                            return this.outPutData("上传失败,上传的应用已存在");
                        }
                            appService.save(application1);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                    //发送邮件
                    User user = (User) request.getSession().getAttribute("admin");
                    List<User> users = userService.findAll();
                    users.stream().forEach(o->{
                        String content = user.getUserName()+"于"+new Date()+"成功创建了一个名为"+application.getAppName()+"的应用,"+"版本号："+application.getVersion()+"。";
                        mailService.sendSimpleMail(o.getEmail(),"主题：锐承应用升级服务器通知",content);
                    });
                    this.adminLog("新增应用信息-"+application.getAppName());
                    return this.outPutData("保存成功");

            }
        }
        return this.outPutErr("保存失败,请重试");
    }

    /**
     * 添加到发布白名单
     */
    @GetMapping("/white/{imei}")
    @ResponseBody
    public String saveWhite(@PathVariable("imei") String imei){

        Application application = appService.get(this.pubAppId);
        if (imei !=null){
            //将导入得设备加入到设备列表
            Device device = deviceService.findByImeiAndProject(imei,application.getProject());

            if (device != null) {
                device.setCreateTime(new Date());
                device.setProject(application.getProject());
                device.setImei(imei);
                deviceService.update(device);//更新到数据库

            } else {
                Device device1 = new Device();
                device1.setCreateTime(new Date());
                device1.setProject(application.getProject());
                device1.setImei(imei);
                deviceService.save(device1);//保存到数据库
            }

            Whitelist whitelist = whitelistService.findByImeiAndAppId(imei,this.pubAppId);
            if(whitelist != null){
                whitelist.setImei(imei);
                whitelist.setStatus("安装");
                whitelist.setAppId(this.pubAppId);
                whitelist.setApplication(application);
                whitelist.setCreateTime(new Date());
                whitelist.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.update(whitelist);
                if (whitelist_new != null) {
                    this.adminLog("更新白名单信息-" + imei);
                    return this.outPutData("保存成功");
                }

            }else{
                //新增
                Whitelist whitelist1 = new Whitelist();
                whitelist1.setImei(imei);
                whitelist1.setStatus("安装");
                whitelist1.setAppId(this.pubAppId);
                whitelist1.setApplication(application);
                whitelist1.setCreateTime(new Date());
                whitelist1.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.save(whitelist1);
                if (whitelist_new != null) {
                    this.adminLog("新增白名单信息-" + imei);
                    return this.outPutData("保存成功");
                }

            }
        }

        return this.outPutErr("保存失败,请重试");
    }

    /**
     * 添加到卸载白名单
     */
    @GetMapping("/whit/{imei}")
    @ResponseBody
    public String saveWhit(@PathVariable("imei") String imei){

        Application application = appService.get(this.delAppId);

        if (imei !=null){

            Whitelist whitelist = whitelistService.findByImeiAndAppId(imei,this.delAppId);
            if(whitelist != null){
                whitelist.setImei(imei);
                whitelist.setStatus("卸载");
                whitelist.setAppId(this.delAppId);
                whitelist.setApplication(application);
                whitelist.setCreateTime(new Date());
                whitelist.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.update(whitelist);
                if (whitelist_new != null) {
                    this.adminLog("更新白名单信息-" + imei);
                    return this.outPutData("更新成功");
                }
            }else {
                //新增
                Whitelist whitelist1 = new Whitelist();
                whitelist1.setImei(imei);
                whitelist1.setStatus("卸载");
                whitelist1.setAppId(this.delAppId);
                whitelist1.setApplication(application);
                whitelist1.setCreateTime(new Date());
                whitelist1.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.save(whitelist1);
                if (whitelist_new != null) {
                    this.adminLog("新增白名单信息-" + imei);
                    return this.outPutData("保存成功");
                }
            }

        }
        return this.outPutErr("保存失败,请重试");
    }



    /**
     *  审核应用
     * */
    @GetMapping("/checkin/{application}")
    @ResponseBody
    public String audit(@Valid Application application, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {//验证
            List <ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        }else{//验证通过

            if(application.getId()!=null){
                application.setStatus("已审核");
                application.setUpdateTime(new Date());
                Application application_new = appService.update(application);
                if(application_new!=null){
                    //发送邮件
                    User user = (User) request.getSession().getAttribute("admin");
                    List<User> users = userService.findAll();
                    users.stream().forEach(o->{
                    String content = user.getUserName()+"于"+new Date()+"审核通过了一个名为"+application.getAppName()+"的应用,"+"版本号："+application.getVersion()+"。";
                    mailService.sendSimpleMail(o.getEmail(),"主题：锐承应用升级服务器通知",content);
                    });
                    this.adminLog("修改应用信息-"+application.getAppName());
                    return this.outPutData("审核成功");
                }
            }
        }
        return this.outPutErr("审核失败,请重试");
    }
    /**
     *  拒绝应用
     * */
    @GetMapping("/checkout/{application}")
    @ResponseBody
    public String auditOut(@Valid Application application, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {//验证
            List <ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        }else{//验证通过

            if(application.getId()!=null){
                application.setStatus("已拒绝");
                application.setUpdateTime(new Date());
                Application application_new = appService.update(application);
                if(application_new!=null){
                    //发送邮件
                    User user = (User) request.getSession().getAttribute("admin");
                    List<User> users = userService.findAll();
                    users.stream().forEach(o->{
                    String content = user.getUserName()+"于"+new Date()+"拒绝通过了一个名为"+application.getAppName()+"的应用,"+"版本号："+application.getVersion()+"。";
                    mailService.sendSimpleMail(o.getEmail(),"主题：锐承应用升级服务器通知",content);
                    });
                    this.adminLog("修改应用信息-"+application.getAppName());
                    return this.outPutData("审核成功");
                }
            }
        }
        return this.outPutErr("审核失败,请重试");
    }



    /**
     *  发布应用页面展示
     * */
    @GetMapping("/pub/{application}")
    public String publish(@Valid Application application,Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page-1,10,sort);
        Page<Device>  devices = deviceRepository.findByProId(application.getProject().getId(),pageable);
        System.out.println(JSON.toJSONString(devices));
        List<Whitelist> whitelists = whitelistService.findByProjectIdAndStatus(application.getProject().getId(),"安装",application.getId());

        this.pubAppId= application.getId();
        model.addAttribute("list", devices);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        model.addAttribute("whitelists",  JSON.toJSONString(whitelists));
        model.addAttribute("list1",   JSON.toJSONString(devices));
        System.out.println("list1:"+pubListExp);
        if(pubListExp.size()>0){
            model.addAttribute("list2",   JSON.toJSONString(pubListExp));
            pubListExp.stream().forEach(o->{
                Device device = deviceService.findByImeiAndProject(o.toString(),application.getProject());
                if (device != null){
                    deviceService.delete(device);
                }

            });
//            pubListExp.clear();
        }
        System.out.println("list2:"+pubListExp);
        return "admin/app-pub";
    }

    /**
     *  卸载应用页面展示
     * */
    @GetMapping("/del/{application}")
    public String del(@Valid Application application,Model model, HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
//        Page<Device> apps = deviceService.findAllByLikeName("imei", keywords, page, 10, sort);
        Pageable pageable = PageRequest.of(page-1,10,sort);

//        Page<Device>  devices = deviceRepository.findByProId(application.getProject().getId(),pageable);
        Page<Whitelist>  devices = whitelistService.findWhiteByProId(application.getProject().getId(),application.getId(),pageable);

        System.out.println(JSON.toJSONString(devices));
        List<Whitelist> whitelists = whitelistService.findByProjectIdAndStatus(application.getProject().getId(),"卸载",application.getId());
        this.delAppId= application.getId();
        model.addAttribute("list", devices);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        model.addAttribute("whitelists",  JSON.toJSONString(whitelists));
        model.addAttribute("list1",   JSON.toJSONString(devices));
        if(delListExp.size()>0){
            model.addAttribute("list2",   JSON.toJSONString(delListExp));
            delListExp.clear();
        }
        model.addAttribute("projectName",  application.getProject().getProjectName());



        return "admin/app-del";
    }


    @GetMapping("/all-pub")
    @ResponseBody
    public String handleStatus() {
        Application application = appService.get(this.pubAppId);
        application.setAllPush(1);
        appService.update(application);

        //把所有批量导入设备保存到数据库
        if(pubListExp.size()>0) {
            for (int i = 0; i < pubListExp.size(); i++) {
                String imei = (String) pubListExp.get(i);
                Device device = deviceService.findByImeiAndProject(imei,application.getProject());
                if (device != null) {
                    device.setCreateTime(new Date());
                    device.setProject(application.getProject());
                    device.setImei(imei);
                    deviceService.update(device);//更新到数据库
                } else {
                    Device device1 = new Device();
                    device1.setCreateTime(new Date());
                    device1.setProject(application.getProject());
                    device1.setImei(imei);
                    deviceService.save(device1);//保存到数据库
                }
            }
        }

        //查询项目下所有的设备
        List<Device> devices = deviceRepository.findAllDevice(application.getProject());
        devices.stream().forEach(o->{
            Whitelist whitelist = whitelistService.findByImeiAndAppId(o.getImei(),this.pubAppId);
            if(whitelist != null){
                whitelist.setImei(o.getImei());
                whitelist.setStatus("安装");
                whitelist.setAppId(this.pubAppId);
                whitelist.setApplication(application);
                whitelist.setCreateTime(new Date());
                whitelist.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.update(whitelist);

            }else{
                //新增
                Whitelist whitelist1 = new Whitelist();
                whitelist1.setImei(o.getImei());
                whitelist1.setStatus("安装");
                whitelist1.setAppId(this.pubAppId);
                whitelist1.setApplication(application);
                whitelist1.setCreateTime(new Date());
                whitelist1.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.save(whitelist1);

            }
        });
        this.adminLog("更新白名单信息" );
        return this.outPutData("操作成功");
    }
    @GetMapping("/all-del")
    @ResponseBody
    public String handleStatus1() {
        Application application = appService.get(this.delAppId);

        //查询项目下所有的设备
        List<Device> devices = deviceRepository.findAllDevice(application.getProject());
        devices.stream().forEach(o->{
            Whitelist whitelist = whitelistService.findByImeiAndAppId(o.getImei(),this.pubAppId);
            if(whitelist != null){
                whitelist.setImei(o.getImei());
                whitelist.setStatus("卸载");
                whitelist.setAppId(this.pubAppId);
                whitelist.setApplication(application);
                whitelist.setCreateTime(new Date());
                whitelist.setProjectId(application.getProject().getId());
                Whitelist whitelist_new = whitelistService.update(whitelist);
            }
        });
        this.adminLog("删除全部白名单信息");
        return this.outPutData("操作成功");
    }





    @GetMapping("/exp")
    public String exp(Model model){
        return "admin/app-export";
    }
    @GetMapping("/exp1")
    public String exp1(Model model){
        return "admin/app-export1";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        pubListExp.clear();
        String fileName = file.getOriginalFilename();
        Application application = appService.get(this.pubAppId);
        Project project = application.getProject();
        String data = null;
        try {
            if(fileName!=null) {
                InputStream inputStream = file.getInputStream();
                Workbook book=null;
                if(isExcel2003(fileName)) {
                    book=new HSSFWorkbook(inputStream);
                }
                if(isExcel2007(fileName)) {
                    book = new XSSFWorkbook(inputStream);
                }
                int sheetsNumber=book.getNumberOfSheets();
                Sheet sheet = book.getSheetAt(0);
                int allRowNum = sheet.getLastRowNum();
                if(allRowNum==0) {
                    return this.outPutErr("导入文件数据为空");
                }
                List failList= new ArrayList();
                for(int i=1;i<=allRowNum;i++){
                    //加载状态值，当前进度
                    Whitelist whitelist = new Whitelist();//我需要插入的数据类型
                    Row row = sheet.getRow(i); //获取第i行
                    if(row!=null) {
                        Cell cell1 = row.getCell(0); //获取第1个单元格的数据
                        if(cell1 != null && cell1.getCellType() != CellType.BLANK ) {//IMEI列验证
                            String cellValue = getCellValue(cell1);
                            if(!StringUtils.isEmpty(cellValue)){
                                int length= cellValue.length();
                                System.out.println(length);
                                if (cellValue.length()!=15){
                                    failList.add(cellValue);
                                    return  this.outPutErr("第"+i+"行的第1列的数据长度不等于15位");
                                }
                                if (!isLetterDigit(cellValue)){
                                    failList.add(cellValue);
                                    return  this.outPutErr("第"+i+"行的第1列的数据格式不正确");
                                }

                                Device device = deviceService.findByImeiAndProject(cellValue,project);
                                if(device == null){
                                    Device device1 = new Device();
                                    device1.setImei(cellValue);
                                    device1.setProject(project);
                                    device1.setCreateTime(new Date());
                                    deviceService.save(device1);
                                }
                                pubListExp.add(cellValue);
                            }

                        } else {
                            return this.outPutErr("第"+i+"行的第1列的数据不能为空");
                        }

                    }
                }
                String fail = "";
                for( int i = 0 ; i < failList.size() ; i++) {
                    fail+=failList.get(i)+",";
                }

                if(failList.size()>0){
                    data = "共计"+allRowNum+"条数据，导入成功"+pubListExp.size()+"条数据，导入失败"+(allRowNum-pubListExp.size())+"条,失败原因：数据重复或格式错误,IMEI:"+fail;
                }else{
                    data = "共计"+allRowNum+"条数据，导入成功"+pubListExp.size()+"条数据，导入失败"+(allRowNum-pubListExp.size())+"条。";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.outPutData(data);
    }


    @ResponseBody
    @RequestMapping(value = "/uploadExcel1")
    public String uploadExcel1(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        int count=0;
        String fileName = file.getOriginalFilename();
        Application application = appService.get(this.delAppId);
        Project project = application.getProject();
        String data = null;
        try {
            if(fileName!=null) {
                InputStream inputStream = file.getInputStream();
                Workbook book=null;
                if(isExcel2003(fileName)) {
                    book=new HSSFWorkbook(inputStream);
                }
                if(isExcel2007(fileName)) {
                    book = new XSSFWorkbook(inputStream);
                }
                int sheetsNumber=book.getNumberOfSheets();
                Sheet sheet = book.getSheetAt(0);
                int allRowNum = sheet.getLastRowNum();
                if(allRowNum==0) {
                    return this.outPutErr("导入文件数据为空");
                }
                List list = new ArrayList();
                for(int i=1;i<=allRowNum;i++){
                    //加载状态值，当前进度
                    Whitelist whitelist = new Whitelist();//我需要插入的数据类型
                    Device device = new Device();//我需要插入的数据类型
                    Row row = sheet.getRow(i); //获取第i行
                    if(row!=null) {
                        Cell cell1 = row.getCell(0); //获取第1个单元格的数据
                        if(cell1 != null && cell1.getCellType() != CellType.BLANK ) {//IMEI列验证
                            String cellValue = getCellValue(cell1);
                            if(!StringUtils.isEmpty(cellValue)){
                                int length= cellValue.length();
                                System.out.println(length);
                                if (cellValue.length()!=15){
                                    return  this.outPutErr("第"+i+"行的第1列的数据长度不等于15位");
                                }
                                if (!isLetterDigit(cellValue)){
                                    return  this.outPutErr("第"+i+"行的第1列的数据格式不正确");
                                }
                                device.setImei(cellValue);
                                device.setCreateTime(new Date());
                                device.setProject(project);
                                delListExp.add(cellValue);

                            }

                        } else {
                            return this.outPutErr("第"+i+"行的第1列的数据不能为空");
                        }
                        if(device.getImei()!=null) {
                            List<Whitelist> whitelists =  whitelistService.findByImei(device.getImei());
                            if(whitelists.size()>0){
                                count++;
                            }else {
                                list.add(device.getImei());
                            }
                        }

                    }
                }

                String fail = "";
                for( int i = 0 ; i < list.size() ; i++) {
                    fail+=list.get(i)+",";
                }



                if(list.size()>0){
                    data = "共计"+allRowNum+"条数据，导入成功"+delListExp.size()+"条数据，导入失败"+(allRowNum-count)+"条,失败原因：数据不在系统内。IMEI:"+fail;
                }else{
                    data = "共计"+allRowNum+"条数据，导入成功"+delListExp.size()+"条数据，导入失败"+(allRowNum-count)+"条。";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.outPutData(data);
    }



    /***
     *
     * @param: 判断文件类型是不是2003版本
     * @return
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     *
     * @param: 判断文件类型是不是2007版本
     * @return
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    public static String getCellValue(Cell cell) {
        String cellValue = null;

        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                DecimalFormat df = new DecimalFormat("0");
                cellValue = df.format(cell.getNumericCellValue());
                NumberFormat nf = NumberFormat.getInstance();
                String s = nf.format(cell.getNumericCellValue());
                if (s.indexOf(",") >= 0) {
                    s = s.replace(",", "");
                }
                cellValue = s;
                break;
            default:

        }
        return cellValue;
    }


    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }







}
