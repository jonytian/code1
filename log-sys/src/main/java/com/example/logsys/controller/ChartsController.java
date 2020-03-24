package com.example.logsys.controller;

import com.alibaba.fastjson.JSON;
import com.example.logsys.mapper.ReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/chart")
public class ChartsController extends BaseController{

    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping("/line")
    public String line( HttpServletResponse response , Model model) throws IOException {
        log.debug("-------------画折线图------------");
        return "pages/echarts/line";
    }


    @RequestMapping("/bar")
    public String bar(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------画柱状图------------");
        return "pages/echarts/bar";
    }



    @RequestMapping("/pie")
    public String pie(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("-------------画饼图------------");
        return "pages/echarts/pie";
    }




    @GetMapping("/getLine")
    @ResponseBody
    public String getLine() {

        List<Map<String, Object>> result = reportMapper.getChartLine();
        Map<String, List<Map<String, Object>>>  groupBy = result.stream().collect(Collectors.groupingBy(e -> e.get("time").toString()));
        List<Map<String, Object>> project = reportMapper.getProjectGroup();
        List title = new ArrayList<>();
        for (int i=0 ;i<project.size();i++){
            if(i>4){
                break;
            }
            title.add(project.get(i).get("name"));
        }
        System.err.println("groupBy:"+groupBy);
        List dateList = new ArrayList();
        groupBy.forEach((k,v)-> {
            dateList.add(k);
        });
        Collections.sort(dateList);

        List dataList = new ArrayList();
        for (int j= 0 ;j<title.size(); j++){
            List<Integer> res = new ArrayList<>();
            for (int k=0;k<dateList.size();k++){
                List<Map<String, Object>> d = groupBy.get(dateList.get(k));
                if (d==null||d.size()==0){
                    break;
                }
                boolean f = false;
                for (int l=0 ;l<d.size();l++){
                    if (d.get(l).get("projectName").equals(title.get(j).toString())){
                        Long t = (Long) d.get(l).get("total");
                        res.add(t.intValue());
                        f=true;
                        break;
                    }
                }
                if (!f){
                    res.add(0);
                }

            }
            Map<String, Object> map = new HashMap<>();
            map.put("name",title.get(j).toString());
            map.put("type","line");
            map.put("stack","总量");
            map.put("data",res);
            dataList.add(map);
        }

        Map map = new HashMap();
        map.put("title",title);
        map.put("date",dateList);
        map.put("data",dataList);
        return JSON.toJSONString(map);
    }

    @GetMapping("/getBar")
    @ResponseBody
    public String getBar() {

        List<Map<String, Object>> result = reportMapper.getChartBar();
        Map<String, List<Map<String, Object>>>  groupBy = result.stream().collect(Collectors.groupingBy(e -> e.get("name").toString()));
        List dimensions = new ArrayList();
        List source = new ArrayList();
        List<Map<String, Object>> fisrt = groupBy.get(getFirstOrNull(groupBy));
        dimensions.add("product");
        fisrt.stream().forEach(o->{
            if(fisrt.indexOf(o)>2){
                return;
            }
            dimensions.add(o.get("version"));
        });
        groupBy.forEach((k,v)->{
            LinkedHashMap data = new LinkedHashMap();
            data.put("product",k);
            for (int i=1;i<dimensions.size(); i++){
                String ver = dimensions.get(i).toString();
                boolean f = false;
                for (int j = 0 ;j<v.size() ; j++){
                    String s = v.get(j).get("version").toString();
                    if (ver.equals(s)){
                        data.put(ver,v.get(j).get("total"));
                        f = true;
                        break;
                    }
                }
                if(!f){
                    data.put(ver,0);
                }


            }
            source.add(data);
            System.out.println(data);
        });

        LinkedHashMap map = new LinkedHashMap(16);
        map.put("dimensions",dimensions);
        map.put("source",source);
        return JSON.toJSONString(map);
    }


    @GetMapping("/getPie")
    @ResponseBody
    public String getPie() {

        List<Map<String, Object>> result = reportMapper.getChartPie();
        Map map = new HashMap();
        List name = new ArrayList();
        result.stream().forEach(o->{
           name.add(o.get("name"));
        });
        map.put("name",name);
        map.put("data",result);
        return JSON.toJSONString(map);
    }


    /**
     * 获取map中第一个数据值
     *
     * @param <K> Key的类型
     * @param <V> Value的类型
     * @param map 数据源
     * @return 返回的值
     */
    public static <K, V> K getFirstOrNull(Map<K, V> map) {
        K obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }




}
