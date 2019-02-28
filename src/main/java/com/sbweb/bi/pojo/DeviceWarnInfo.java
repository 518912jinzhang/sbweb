package com.sbweb.bi.pojo;

/*
 * 报警信息
 * 地图上报警列表及设备报警信息
 * */
public class DeviceWarnInfo {
	
	private int id;
	
	private String road;
	
	private String status;
	
	private String deviceType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
