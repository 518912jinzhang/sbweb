package com.sbweb.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sbweb.bi.pojo.ChartsDataResp;
import com.sbweb.init.service.BIService;
import com.sbweb.ui.pojo.DevicePojo;


@Controller  
public class StatisticsController {

	@Autowired 
	private BIService biservice;
	
    /**
     * 终端在线分析统计
     */
    @RequestMapping(value="/showMoniterOnline", method=RequestMethod.GET)
    public String showMoniterOnline(Model model) {
    	ChartsDataResp resp = biservice.getDeviceStatistics();
    	List<DevicePojo> deviceList = resp.getDeviceList();
    	int on1=0;
    	int on2=0;
    	int on3=0;
    	int on4=0;
    	int on5=0;    	
    	int off1=0;
    	int off2=0;
    	int off3=0;
    	int off4=0;
    	int off5=0;
    	for(DevicePojo de : deviceList) {
    		if(de.getDeviceType() == 0) {
    			on1 += de.getDeviceOnlineNum();
    			off1 += de.getDeviceTotalNum()-de.getDeviceOnlineNum();
    		} else if(de.getDeviceType() == 1) {
    			on2 += de.getDeviceOnlineNum();
    			off2 += de.getDeviceTotalNum()-de.getDeviceOnlineNum();
    		} else if(de.getDeviceType() == 2) {
    			on3 += de.getDeviceOnlineNum();
    			off3 += de.getDeviceTotalNum()-de.getDeviceOnlineNum();
    		} else if(de.getDeviceType() == 3) {
    			on4 += de.getDeviceOnlineNum();
    			off4 += de.getDeviceTotalNum()-de.getDeviceOnlineNum();
    		} else if(de.getDeviceType() == 4) {
    			on5 += de.getDeviceOnlineNum();
    			off5 += de.getDeviceTotalNum()-de.getDeviceOnlineNum();
    		}
    		
    	}
    	model.addAttribute("on", on1+","+on2+","+on3+","+on4+","+on5);
    	model.addAttribute("off", off1+","+off2+","+off3+","+off4+","+off5);
    	return "business/showMoniterOnline";
    }

    /**
     * 故障分析统计
     */
    @RequestMapping(value="/showBreakDown", method=RequestMethod.GET)
    public String showBreakDown() {
    	return "business/showBreakDown";
    }
    
    /**
     * 统计分析信息
     */
    @RequestMapping(value="/getStatistics", method=RequestMethod.POST)
    @ResponseBody
    public String getStatistics(Model model) {
    	ChartsDataResp resp = biservice.getDeviceStatistics();
    	return JSON.toJSONString(resp, true);
    }
    
    /**
     * 系统模板1管理
     */
    @RequestMapping(value="/showTemplate", method=RequestMethod.GET)
    public String showTemplate1(String templateId) {
    	if("1".equals(templateId)) {
        	return "business/template1";
    	} else if("2".equals(templateId)) {
        	return "business/template2";
    	} else if("3".equals(templateId)) {
        	return "business/template3";
    	}
        return "business/template1";
    }
    /**
     * 自定义模板管理
     */
    @RequestMapping(value="/showSelfTemplate", method=RequestMethod.GET)
    public String showSelfTemplate() {
    	return "business/selfTemplate";
    }
} 