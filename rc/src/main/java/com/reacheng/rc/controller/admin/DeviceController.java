package com.reacheng.rc.controller.admin;

import com.alibaba.fastjson.JSON;
import com.reacheng.rc.controller.BaseController;
import com.reacheng.rc.entity.*;
import com.reacheng.rc.service.DeviceService;
import com.reacheng.rc.service.ProjectService;
import com.reacheng.rc.tools.DownloadFileUtil;
import com.reacheng.rc.tools.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "admin/dev")
@Slf4j
public class DeviceController extends BaseController {

    //文件上级目录
    String PATH = "excel";
    //文件名
    String FILENAME ="imei.xlsx";
    //下载展示的文件名
    String NEWNAME="";
    List<DeviceVO> dataList = null;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ProjectService projectService;


    @GetMapping("")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "page", defaultValue = "1") Integer page) {
        String keywords = request.getParameter("keywords");
        String projectName = request.getParameter("projectName");
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        //Page<Device> devs = deviceService.findAllByLikeName("imei", keywords, page, 10, sort);
        page = page==0?1:page;
        Page<Device> devs = deviceService.findAllDev(keywords,projectName,page-1,10);

        System.out.println(JSON.toJSONString(devs));
        model.addAttribute("list", devs);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        model.addAttribute("projectName", projectName);

        List<Device> devices = devs.getContent();
        dataList = new ArrayList<>();
        devices.stream().forEach(o->{
            DeviceVO deviceVO = new DeviceVO();
            deviceVO.setImei(o.getImei());
            deviceVO.setProjectName(o.getProject().getProjectName());
            dataList.add(deviceVO);
        });

        User user = (User) request.getSession().getAttribute("admin");
        String roleName = user.getGadmin().getGname();
        model.addAttribute("roleName", roleName);

        return "admin/dev-list";
    }
    @GetMapping("/add")
    public String addDevice(Model model){
        List<Project> pros = projectService.findAll();
        model.addAttribute("pros", pros);
        return "admin/dev-add";
    }

    @GetMapping("/edit/{device}")
    public String editDevice(Device device, Model model){
        List<Device> devices = deviceService.findAll();
        model.addAttribute("devices", devices);
        model.addAttribute("device",device);
        return "admin/dev-edit";
    }

    @DeleteMapping("/destroy/{device}")
    @ResponseBody
    public String destroy(Device device){
        this.adminLog("删除-"+device.getImei());
        try{
            deviceService.delete(device);
        }catch (Exception e){
            System.out.println(e);
            return this.outPutErr("请先删除关联的数据");
        }
        return this.outPutData("删除成功");
    }


    @PostMapping("/save")
    @ResponseBody
    public String saveDevice(@Valid Device device, BindingResult result){
        if (result.hasErrors()) {//验证
            List <ObjectError> error = result.getAllErrors();
            for (ObjectError e : error) {
                return this.outPutErr(e.getDefaultMessage());
            }
            return null;
        }else{//验证通过

            if(device.getId()!=null){
                Device device_new = deviceService.update(device);
                if(device_new!=null){
                    this.adminLog("修改设备信息-"+device.getImei());
                    return this.outPutData("保存成功");
                }
            }else{
                //新增
                Project project = projectService.findByName(device.getProjectName());
                if (project !=null){
                    device.setImei(device.getImei());
                    device.setCreateTime(new Date());
                    device.setProject(project);

                    Device device1 = deviceService.findByImei(device.getImei());
                    if (device1==null){
                        Device device_new = deviceService.save(device);
                        if(device_new!=null){
                            this.adminLog("新增设备信息-"+device.getImei());
                            return this.outPutData("保存成功");
                        }
                    }else {
                        return this.outPutErr("保存失败,不能重复添加！");
                    }

                }else {
                    return this.outPutErr("保存失败,请选择项目");
                }



            }
        }
        return this.outPutErr("保存失败,请重试");
    }

    /**
     * 下载模板
     * @return 返回excel模板
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "模板下载", httpMethod = "GET", produces = "application/json;charset=UTF-8")
    public Object export(){
        ResponseEntity<InputStreamResource> response = null;
        try {
            response = DownloadFileUtil.download(PATH, FILENAME, "导入模板");
        } catch (Exception e) {
            log.error("下载模板失败");
        }
        return response;
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
        ExcelUtils.writeExcel(response,dataList,DeviceVO.class);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
    }



    @GetMapping("/exp")
    public String exp(Model model){
        List<Project> pros = projectService.findAll();
        model.addAttribute("pros", pros);
        return "admin/dev-export";
    }


    @ResponseBody
    @RequestMapping(value = "/uploadExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile file, String projectName, HttpServletRequest request, HttpServletResponse response) {
        int count=0;
        String fileName = file.getOriginalFilename();
        if(StringUtils.isEmpty(projectName)){
            return this.outPutErr("请选择项目");
        }
        Project project = projectService.findByName(projectName);
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
                    Device device = new Device();//我需要插入的数据类型
                    Row row = sheet.getRow(i); //获取第i行
                    if(row!=null) {
                        Cell cell1 = row.getCell(0); //获取第1个单元格的数据
                        if(cell1 != null && cell1.getCellType() != CellType.BLANK) {//IMEI列验证
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
                            }

                        } else {
                            return this.outPutErr("第"+i+"行的第1列的数据不能为空");
                        }
                        if(device.getImei()!=null) {

                            Device device1 = deviceService.findByImeiAndProject(device.getImei(),project);
                            if (device1 == null) {
                                count++;
                                deviceService.save(device);//保存到数据库
                            }else {
                               list.add(device1.getImei());
                            }
                        }
                    }
                }
                String fail = "";
                for( int i = 0 ; i < list.size() ; i++) {
                    fail+=list.get(i)+",";
                }
                if(list.size()>0){
                    data = "共计"+allRowNum+"条数据，导入成功"+count+"条数据，导入失败"+(allRowNum-count)+"条,失败原因：数据已存在。IMEI:"+fail;
                }else{
                    data = "共计"+allRowNum+"条数据，导入成功"+count+"条数据，导入失败"+(allRowNum-count)+"条。";
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
