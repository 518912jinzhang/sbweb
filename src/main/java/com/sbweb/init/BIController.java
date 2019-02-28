package com.sbweb.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sbweb.bi.pojo.BreakDownPojo;
import com.sbweb.bi.pojo.ChartsDataResp;
import com.sbweb.init.service.BIService;
import com.sbweb.init.service.BIServiceTest;
import com.sbweb.ui.pojo.MoniterPojo;
import com.sbweb.ui.pojo.SktResponse;


@Controller  
public class BIController {

	@Autowired 
	private BIService biservice;
	
    /**
     * BI页面
     */
    @RequestMapping(value="/map", method=RequestMethod.GET)
    public String home(Model model) {
    	//故障列表
    	SktResponse resp = biservice.getBreakDownMoniterPojoList();
    	List<BreakDownPojo> breakDownList = resp.getDatas();
    	model.addAttribute("bdList", breakDownList);
        return "map/initMap";
    }
    
    /**
     * UI系统管理打开BI界面
     */
    @RequestMapping(value="/showSystemTemplate", method=RequestMethod.GET)
    public String showSystemTemplate(Model model,String templateId) {
    	//故障列表
    	SktResponse resp = biservice.getBreakDownMoniterPojoList();
    	List<BreakDownPojo> breakDownList = resp.getDatas();
    	model.addAttribute("bdList", breakDownList);
    	if("1".equals(templateId)) {
    		return "map/template1/initMap";
    	} else if("2".equals(templateId)) {
        	return "map/template2/initMap";
    	} else if("3".equals(templateId)) {
        	return "map/template3/initMap";
    	}
    	return "map/initMap";
    }
    
    /**
     *ajax同步获取终端地图渲染坐标信息
     **/
    @RequestMapping(value="/getMapMoniterList", method=RequestMethod.POST)
    @ResponseBody
    public String getMapMoniterList() {
    	SktResponse resp = biservice.getMoniterPojoList();
    	List<String> msgList = resp.getDatas();
    	String listJson = JSON.toJSONString(msgList, true);
    	return listJson;
    }
    
    /**
     *ajax同步获取终端具体信息
     **/
    @RequestMapping(value="/getMoniterInfo", method=RequestMethod.POST)
    @ResponseBody
    public String getMoniterInfo(String moniterId) {
    	MoniterPojo mon = biservice.getMoniterInfo(moniterId);
    	String json = JSON.toJSONString(mon, true);
    	return json;
    }

    /**
     * 定位地图经纬度页面
     */
    @RequestMapping(value="/mapLocation", method=RequestMethod.GET)
    public String mapLocation() {  
        return "map/mapLocation";
    }
    
    /**
     * UI关注终端地图在线预览
     */
    @RequestMapping(value="/mapLocationMoniters", method=RequestMethod.GET)
    public String mapLocationMoniters() {
        return "map/mapLocationMoniters";
    }

    /**
     * BI获取在线统计信息
     */
    @RequestMapping(value="/getDeviceStatistics", method=RequestMethod.POST)
    @ResponseBody
    public String getDeviceStatistics(Model model) {
    	ChartsDataResp resp = biservice.getDeviceStatistics();
    	return JSON.toJSONString(resp, true);
    }

    /**
     * 测试在线统计
     */
    @RequestMapping(value="/dataBaseShow", method=RequestMethod.GET)
    public String dataBaseShow(Model model) {
    	ChartsDataResp resp = biservice.getDeviceStatistics();
    	model.addAttribute("resp", resp);
        return "business/data-base";
    }
    
    /**
     * 数据测试
     * */
    @RequestMapping(value="/dataBase", method=RequestMethod.POST)  
    @ResponseBody
    public String dataBase() {  
        return "business/data-base";
    }
} 