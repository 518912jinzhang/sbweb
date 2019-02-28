package com.sbweb.bi.pojo;

/*
 * 设备详情
 * 点击地图设别图标获取设备详情
 * 
 * */
public class DeviceDetailInfo {

	private int id;
	
	private String deviceType;
	
	private String status;
	
	private String temperature;
	
	//湿度
	private String humidity;
	
	//开关箱状态
	private String switchCase;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getSwitchCase() {
		return switchCase;
	}

	public void setSwitchCase(String switchCase) {
		this.switchCase = switchCase;
	}
	
}
