package com.sbweb.init.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sbweb.bi.pojo.BreakDownPojo;


/*
 * BI侧获取设备故障故障列表
 * */
@Service
public class BIServiceTest {
	public static List<BreakDownPojo> getBreakDownPojoList(){
		List<BreakDownPojo> list = new ArrayList<BreakDownPojo>();
		BreakDownPojo bd1 = new BreakDownPojo();
		bd1.setDeviceDesc("湖东路定点1");
		bd1.setWarnNumberDesc("断电");
		bd1.setDeviceTypeDesc("电子警察");

		BreakDownPojo bd2 = new BreakDownPojo();
		bd2.setDeviceDesc("睢州大道西3");
		bd2.setWarnNumberDesc("断电");
		bd2.setDeviceTypeDesc("治安卡口");

		BreakDownPojo bd3 = new BreakDownPojo();
		bd3.setDeviceDesc("水口路定点1");
		bd3.setWarnNumberDesc("断电");
		bd3.setDeviceTypeDesc("监控");

		BreakDownPojo bd4 = new BreakDownPojo();
		bd4.setDeviceDesc("睢州大道东1");
		bd4.setWarnNumberDesc("断电");
		bd4.setDeviceTypeDesc("人脸识别");
		list.add(bd1);
		list.add(bd2);
		list.add(bd3);
		list.add(bd4);
		return list;
	}
}
