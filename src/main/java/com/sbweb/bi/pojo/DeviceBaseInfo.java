package com.sbweb.bi.pojo;

/*
 * 设备基本信息
 * 地图设备点渲染信息
 * */
public class DeviceBaseInfo {
 
	private int id;
	
	//设备数量
	private int count;
	
	private String lat;
	
	private String lon;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	
}
