package com.sbweb.init;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbweb.bi.pojo.LogInfo;
import com.sbweb.common.HexUtil;
import com.sbweb.init.service.MoniterService;
import com.sbweb.ui.pojo.AttentionMoniter;
import com.sbweb.ui.pojo.DevicePojo;
import com.sbweb.ui.pojo.MoniterPojo;
import com.sbweb.ui.pojo.SktResponse;
import com.sbweb.ui.query.AttentionMoniterQuery;
import com.sbweb.ui.query.MoniterLogQuery;

@Controller  
public class MoniterController2 {
	@Autowired 
	private MoniterService moniterService;
	private String token;
    
    @RequestMapping(value="/showError", method=RequestMethod.GET)  
    public String showError() {
    	return "business/error";
    }

    /**
     * 终端列表
     * */
    @RequestMapping(value="/moniterList", method=RequestMethod.GET)
    public String moniterList(Model model, HttpServletRequest req) {
    	token = (String) req.getSession().getAttribute("token");
    	System.out.println("----token:-------"+token);
    	SktResponse response = moniterService.getMoniterPojoList(token);
    	List<MoniterPojo> list = response.getDatas();
    	model.addAttribute("list", list);
    	model.addAttribute("size", list.size());
        return "business/moniterlist2";
    }

    /**
     * 关注列表
     **/
    @RequestMapping(value="/attentionMoniterList", method=RequestMethod.GET)
    public String attentionMoniterList(Model model, HttpServletRequest req
    		, AttentionMoniterQuery query) {
    	token = (String) req.getSession().getAttribute("token");
    	System.out.println("----token:-------"+token);
    	//关注终端列表
    	SktResponse resp = moniterService.getMoniterAttentionList(
    			token, query.getQueryMoniterId(), query.getQueryAttentionType());
    	List<MoniterPojo> moniterList = resp.getDatas();
    	model.addAttribute("list", moniterList);
    	model.addAttribute("size", moniterList.size());
    	model.addAttribute("query", query);
        return "business/moniterlist-attention";
    }

    @RequestMapping(value="/moniterAddShow", method=RequestMethod.GET)  
    public String moniterAddShow(@ModelAttribute MoniterPojo moniterPojo) {
        return "business/moniter-add";
    }

    /**
     * 终端注册提交
     **/
    @RequestMapping(value="/moniterAdd", method=RequestMethod.POST)
    @ResponseBody
    public String moniterAdd(@ModelAttribute MoniterPojo moniterPojo) {
    	System.out.println("----add moniter token:-------"+token);
    	String retCode = moniterService.add(token, moniterPojo);
    	return "{\"retCode\":\""+retCode+"\"}";
    }

    /**
     * 添加关注页面
     */
    @RequestMapping(value="/moniterAttentionShow", method=RequestMethod.GET)  
    public String moniterAttentionShow(Model model, @ModelAttribute AttentionMoniter am, 
    		HttpServletRequest req) {
    	am.setUserId((String) req.getSession().getAttribute("userId"));
    	model.addAttribute("am", am);
        return "business/moniter-attention-add";
    }

    /**
     * 添加关注提交
     */
    @RequestMapping(value="/moniterAttentionAdd", method=RequestMethod.POST)
    @ResponseBody
    public String moniterAttentionAdd(@ModelAttribute AttentionMoniter am) {
    	String retCode = moniterService.addAttentionMoniter(token, am);
    	return  "{\"retCode\":\""+retCode+"\"}";
    }

    /**
     * 终端日志显示
     */
    @RequestMapping(value="/viewMoniterLogs", method=RequestMethod.GET)  
    public String viewMoniterLogs(Model model, String id, String interval) {
    	//id = "1000000063";
    	if(StringUtils.isEmpty(interval)) {
    		interval = "7";
    	}
    	List<LogInfo> logList = moniterService.getMoniterLogList(token, id, interval);
    	model.addAttribute("list", logList);
    	model.addAttribute("size", logList.size());
    	model.addAttribute("id", id);
        return "business/moniter-logs";
    }
    /**
     * 终端日志ajax
     **/
    @RequestMapping(value="/viewMoniterLogsLoad", method=RequestMethod.GET)  
    public String viewMoniterLogsLoad(Model model, String id, String interval) {
    	//id = "1000000063";
    	if(StringUtils.isEmpty(interval)) {
    		interval = "7";
    	}
    	List<LogInfo> logList = moniterService.getMoniterLogList(token, id, interval);
    	model.addAttribute("list", logList);
    	return "business/moniter-logs::gridData";
    }
    
    /**
     * 终端信息及控制台
     */
    @RequestMapping(value="/viewMoniter", method=RequestMethod.GET)  
    public String viewMoniter(Model model, String id) {
    	MoniterPojo moniterPojo = new MoniterPojo();
    	moniterPojo = moniterService.getMoniterInfo(token, id);
    	
    	SktResponse resp = moniterService.getBreakDownMoniterPojoList(token, id, null);
    	List<MoniterPojo> moniterList = resp.getDatas();
    	String warnDesc = "设备运行正常";
    	if(null != moniterList && moniterList.size()>0) {
        	MoniterPojo breakInfo = moniterList.get(0);
        	warnDesc = breakInfo.getWarnDesc();
    	}
    	moniterPojo.setWarnDesc(warnDesc);
    	
    	//控制台电源信息
    	MoniterPojo moniterPower = moniterService.getBeatHeartInfo(token, id);
    	moniterPojo.setBeatheart(moniterPower.getBeatheart());
    	moniterPojo.setPowerStatus(moniterPower.getPowerStatus());
    	moniterPojo.setElectronFlow(moniterPower.getElectronFlow());
    	moniterPojo.setVideoElectronFlow(moniterPower.getVideoElectronFlow());
    	moniterPojo.setLampElectronFlow(moniterPower.getLampElectronFlow());
    	moniterPojo.setFlashLampTimes(moniterPower.getFlashLampTimes());
    	moniterPojo.setLampSignal(moniterPower.getLampSignal());
    	String ps = HexUtil.decimalToBinary(moniterPower.getPowerStatus());
    	//补足3位
    	while(ps.length() < 8) {
    		ps = "0"+ps;
    	}
    	moniterPojo.setPowerStr(ps);
    	String ds = HexUtil.decimalToBinary(moniterPower.getDuplexStatus());
    	//补足3位
    	while(ds.length() < 8) {
    		ds = "0"+ds;
    	}
    	moniterPojo.setDuplexStr(ds);
    	model.addAttribute("moniterPojo", moniterPojo);
		//根据终端编号查询设备列表信息
    	if(null != moniterPojo) {
			List<DevicePojo> deviceList = moniterService.getDeviceList(token, moniterPojo.getId());
			moniterPojo.setDeviceList(deviceList);
    	}
        if(moniterPojo.getVersion()==2||moniterPojo.getVersion()==3) {//版本2版本3
    		return "business/moniter-detail3-new";
    	}else {
    		return "business/moniter-detail2";
    	}
    }

    //TODO:废弃
    @RequestMapping(value="/viewDevice", method=RequestMethod.GET)  
    public String viewDevice(Model model, String id, String ip) {
    	DevicePojo devicePojo = new DevicePojo();
    	devicePojo = moniterService.getDeviceInfo(token, id, ip);
    	devicePojo.setMoniterId(id);
    	model.addAttribute("devicePojo", devicePojo);
        return "business/device-detail";
    }

    /**
     * 设备信息修改
     **/
    @RequestMapping(value="/editDevice", method=RequestMethod.POST)
    @ResponseBody
    public String editDevice(@ModelAttribute DevicePojo device, Model model, String moniterId) {
    	System.out.println("----editDevice token:-------"+token);
    	String retCode = moniterService.editDeviceByToken(token, moniterId, device);
    	model.addAttribute("device", device);
    	return "{\"retCode\":\""+retCode+"\"}";
    }

    @RequestMapping(value="/editDeviceShow", method=RequestMethod.GET)
    public String editDeviceShow(Model model, String id, String ip) {
    	DevicePojo devicePojo = new DevicePojo();
    	devicePojo = moniterService.getDeviceInfo(token, id, ip);
    	devicePojo.setMoniterId(id);
    	model.addAttribute("device", devicePojo);
        return "business/device-edit";
    }
    /**
     * 设备重启
     **/
    @RequestMapping(value="/deviceRestart", method=RequestMethod.POST)
    @ResponseBody
    public String deviceRestart(String id, String ip) {
    	System.out.println("----deviceRestart token:-------"+token);
    	String retCode = moniterService.deviceRestart(token, id, ip);
    	return  "{\"retCode\":\""+retCode+"\"}";
    }
    
    @RequestMapping(value="/moniterDelete", method=RequestMethod.POST)
    @ResponseBody
    public String moniterDelete(@ModelAttribute MoniterPojo moniterPojo) {
    	System.out.println("----moniterDelete token:-------"+token);
    	String retCode = moniterService.deleteByToken(token, moniterPojo.getId());
    	return  "{\"retCode\":\""+retCode+"\"}";
    }

    /**
     * 终端信息显示
     */
    @RequestMapping(value="/moniterEditShow", method=RequestMethod.GET)  
    public String moniterEditShow(Model model, String id) {
    	MoniterPojo moniter = new MoniterPojo();
    	moniter = moniterService.getMoniterInfo(token, id);
    	model.addAttribute("moniter", moniter);
    	if(StringUtils.isEmpty(moniter.getId())) {
    		model.addAttribute("errMsg", "终端数据不存在，请刷新页面后重新查询");
    	}
    	return "business/moniter-edit";
    }
    
    /**
     * 终端信息修改
     */
    @RequestMapping(value="/moniterEdit", method=RequestMethod.POST)
    @ResponseBody
    public String moniterEdit(@ModelAttribute MoniterPojo moniter) {
    	System.out.println("----moniterEdit token:-------"+token);
    	String retCode = moniterService.editMoniterByToken(token, moniter);
    	return "{\"retCode\":\""+retCode+"\"}";
    }

    //TODO:废弃
    @RequestMapping(value="/heartbeatEditShow", method=RequestMethod.GET)  
    public String heartbeatEditShow(Model model, String id) {
    	MoniterPojo moniter = new MoniterPojo();
    	moniter = moniterService.getBeatHeartInfo(token, id);
    	model.addAttribute("moniter", moniter);
        return "business/moniter-beatHeart";
    }

    @RequestMapping(value="/editHeartbeat", method=RequestMethod.POST)
    @ResponseBody
    public String editHeartbeat(@ModelAttribute MoniterPojo moniterPojo, String id, String heart, String power) {
    	System.out.println("----heartbeatEdit token:-------"+token);
    	String retCode = moniterService.editBeatHeartInfoByToken(token, id, heart, power);
    	return  "{\"retCode\":\""+retCode+"\"}";
    }
    
    /**
     * 故障列表 
     **/
    @RequestMapping(value="/moniterDownList", method=RequestMethod.GET)  
    public String moniterDownList(Model model, HttpServletRequest req
    		, AttentionMoniterQuery query) {
    	token = (String) req.getSession().getAttribute("token");
    	System.out.println("----moniterDownList token:-------"+token);
    	SktResponse resp = moniterService.getBreakDownMoniterPojoList(token, query.getQueryMoniterId(), query.getWarnType());
    	model.addAttribute("list", resp.getDatas());
    	model.addAttribute("size", resp.getDatas().size());
    	model.addAttribute("query", query);
    	return "business/moniterDownlist";
    }
    
    /**
     * 故障列表 ajax
     **/
    @RequestMapping(value="/moniterDownListLoad", method=RequestMethod.GET)  
    public String moniterDownListLoad(Model model, HttpServletRequest req
    		, AttentionMoniterQuery query) {
    	token = (String) req.getSession().getAttribute("token");
    	System.out.println("----moniterDownList token:-------"+token);
    	SktResponse resp = moniterService.getBreakDownMoniterPojoList(token, query.getQueryMoniterId(), query.getWarnType());
    	model.addAttribute("list", resp.getDatas());
    	model.addAttribute("size", resp.getDatas().size());
    	model.addAttribute("query", query);
    	return "business/moniterDownlist::gridData";
    }
} 