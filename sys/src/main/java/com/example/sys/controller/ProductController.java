package com.example.sys.controller;

import com.alibaba.fastjson.JSON;
import com.example.sys.entity.BusClick;
import com.example.sys.entity.Product;
import com.example.sys.entity.ProductSearch;
import com.example.sys.entity.User;
import com.example.sys.service.AsyncTaskService;
import com.example.sys.service.ProductService;
import com.example.sys.util.DownloadFileUtil;
import com.example.sys.util.ExcelUtils;
import com.example.sys.util.PageDataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by tyj on 2019/07/03.
 */
@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{

	// 文件上级目录
	String PATH = "excel";
	// 文件名
	String FILENAME ="imei.xlsx";

	@Autowired
	private ProductService productService;
	@Autowired
	private AsyncTaskService asyncTaskService;

	List<BusClick> failList  = new ArrayList<>();
	List<BusClick> busClicks = new ArrayList<>();

	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("-------------index------------");
		User user = (User) request.getSession().getAttribute("admin");
		if (user == null) {
			response.sendRedirect(request.getContextPath()+"/admin/login");
			return null;
		}

		model.addAttribute("username", user.getUsername());
		log.debug("用户名："+user.getUsername());
		return "product/index";
	}


	/**
	 * 分页查询产品列表
	 * @return ok/fail
	 */
	@RequestMapping(value = "/getProducts", method = RequestMethod.POST)
	@ResponseBody
	public PageDataResult getProducts(@RequestParam("page") Integer page,
									  @RequestParam("limit") Integer limit, ProductSearch productSearch,HttpServletRequest request) {
		log.debug("分页查询产品列表！搜索条件：productSearch：" + productSearch + ",page:" + page
				+ ",每页记录数量limit:" + limit);
		System.out.println(request.toString());

		PageDataResult pdr = new PageDataResult();
		try {
			if (null == page) {
				page = 1;
			}
			if (null == limit) {
				limit = 10;
			}
			// 获取产品列表
			pdr = productService.getProducts(productSearch, page, limit);
      			log.info("产品列表查询=pdr:" + pdr);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("产品列表查询异常！", e);
		}
		return pdr;
	}




	/**
	 * 下载模板
	 * @return 返回excel模板
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
	@ResponseBody
	public Object export(){
		ResponseEntity<InputStreamResource> response = null;
		try {
			response = DownloadFileUtil.download(PATH, FILENAME, "导入模板");
		} catch (Exception e) {
			log.error("下载模板失败");
		}
		return response;
	}

	public static boolean isLetterDigit(String str) {
		if(!StringUtils.isNotEmpty(str))
		{return false;}
		String regex = "^[a-z0-9A-Z]+$";
		return str.matches(regex);
	}

	@ResponseBody
	@RequestMapping(value = "/upload")
	public String readExcel(@RequestParam("file") MultipartFile file){

		long t1 = System.currentTimeMillis();
		List<BusClick> list = ExcelUtils.readExcel("", BusClick.class, file);
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("read over! cost:%sms", (t2 - t1)));

		failList.clear();
		log.info("开始处理数据************************");
		long t3 = System.currentTimeMillis();
		list.stream().forEach(o -> {
			int index = list.indexOf(o);
			System.out.println(JSON.toJSONString(o));

			// 数据格式过滤
			String imei1 = o.getImei_1();
			if (!isLetterDigit(imei1) || imei1.length()!=15 ){
				failList.add(o);
				return ;
			}
			String iccid1 = o.getICCID1();
			if (!isLetterDigit(iccid1) || iccid1.length()!=20){
				failList.add(o);
				return ;
			}
			String machine_sn =o.getMachine_sn();
            if (!isLetterDigit(machine_sn)|| machine_sn.length()!=16){
            failList.add(o);
            return ;
            }
            // 异步任务开启
			asyncTaskService.executeAsyncTask(o,index);

		});

		long t4 = System.currentTimeMillis();
		log.info("成功处理数据消耗时间："+(t4-t3)+"  ms");
		log.info("结束处理数据************************");
		String msg ;
		if(failList.size() > 0 ) {
			 msg = "共计" + list.size() + "条数据，导入成功" +(list.size() - failList.size()) + "条数据，导入失败" +  failList.size() + "条,失败原因：数据无效,请检查数据重新上传！";
		}else {
			 msg = "共计" + list.size() + "条数据，导入成功" + list.size() + "条数据，导入失败0条。";
		}

		return this.outPutData(msg);
	}

	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response)  throws IOException {
		List<BusClick> resultList =failList;
		long t1 = System.currentTimeMillis();
		ExcelUtils.writeExcel(response, resultList, BusClick.class);
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
	}

	/**
	 * 批量查询下载
	 * @return ok/fail
	 */
	@ResponseBody
	@RequestMapping(value = "/upload1")
	public String batch(@RequestParam("file") MultipartFile file ){

		long t1 = System.currentTimeMillis();
		List<BusClick> list = ExcelUtils.readExcel("", BusClick.class, file);
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("read over! cost:%sms", (t2 - t1)));


		long t3 = System.currentTimeMillis();
		List<Product> listAll = new ArrayList<Product>();
		for (int i=0 ;i<list.size();i++){
			ProductSearch productSearch = new ProductSearch();
			productSearch.setMachine_sn(list.get(i).getMachine_sn());
			List<Product> productList = productService.getProductList(productSearch);
			listAll.addAll(productList);
		}
		listAll = new ArrayList<Product>(new LinkedHashSet<>(listAll));

		listAll.stream().forEach(o->{
			BusClick busClick = new BusClick();
			BeanUtils.copyProperties(o, busClick);
			busClicks.add(busClick);
		});
		String msg = "共计匹配到" + listAll.size() + "条数据，请下载！";

		return this.outPutData(msg);

	}


	@RequestMapping(value = "/exportExcel1", method = RequestMethod.GET)
	public void exportExcel1(HttpServletResponse response)  throws IOException {
		List<BusClick> resultList =busClicks;
		long t1 = System.currentTimeMillis();
		ExcelUtils.writeExcel1(response, resultList, BusClick.class);
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
	}








	/**
	 * 批量删除
	 * @return ok/fail
	 */
	@RequestMapping(value = "/delProduct", method = RequestMethod.POST)
	@ResponseBody
	public String delProduct(@RequestParam("data") String proData) {
		log.debug("需要删除的产品信息："+proData);
		List<Map> list= JSON.parseArray(proData, Map.class);
		try {
			if (list.size()==0) {
				log.debug("请求参数有误，请您稍后再试");
				return this.outPutErr("请求参数有误，请您稍后再试！");
			}
			/**删除*/
			list.stream().forEach(o->{
				int pid = (int) o.get("id");
				productService.deleteProduct(pid);
			});

		} catch (Exception e) {
			e.printStackTrace();
			log.error("操作异常！", e);
			return	this.outPutErr("操作异常，请您稍后再试！");
		}
		return this.outPutData("成功删除"+list.size()+"数据！");
	}

}
