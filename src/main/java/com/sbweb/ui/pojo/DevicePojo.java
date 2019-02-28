package com.sbweb.ui.pojo;

public class DevicePojo {

	//
	private String dataHead;
	//所属终端ID
	private String moniterId;
	//IP 地址
	private String ip;
	//端口
	private int port;
	//最后刷新时间
	private String updatedTime;
	//路口信息
	private String road;
	//设备类型
	private int deviceType;
	private String deviceTypeDesc;
	//设备品牌
	private int deviceBrand;
	private String deviceBrandDesc;
	//设备型号
	private int deviceModel;
	private String deviceModelDesc;
	//设备描述
	private String desc;
	private int deviceOnlineNum;
	private int deviceTotalNum;
	
	//设备电源状态
	private int port1;
	//设备双工状态
	private int port2;
	//设备会话状态
	private int port3;
	
	public int getPort1() {
		return port1;
	}
	public void setPort1(int port1) {
		this.port1 = port1;
	}
	public int getPort2() {
		return port2;
	}
	public void setPort2(int port2) {
		this.port2 = port2;
	}
	public int getPort3() {
		return port3;
	}
	public void setPort3(int port3) {
		this.port3 = port3;
	}
	/**
	 * @return the moniterId
	 */
	public String getMoniterId() {
		return moniterId;
	}
	/**
	 * @param moniterId the moniterId to set
	 */
	public void setMoniterId(String moniterId) {
		this.moniterId = moniterId;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the road
	 */
	public String getRoad() {
		return road;
	}
	/**
	 * @param road the road to set
	 */
	public void setRoad(String road) {
		this.road = road;
	}
	/**
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the deviceBrand
	 */
	public int getDeviceBrand() {
		return deviceBrand;
	}
	/**
	 * @param deviceBrand the deviceBrand to set
	 */
	public void setDeviceBrand(int deviceBrand) {
		this.deviceBrand = deviceBrand;
	}
	/**
	 * @return the deviceModel
	 */
	public int getDeviceModel() {
		return deviceModel;
	}
	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(int deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the updatedTime
	 */
	public String getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getDataHead() {
		return dataHead;
	}
	public void setDataHead(String dataHead) {
		this.dataHead = dataHead;
	}
	public int getDeviceOnlineNum() {
		return deviceOnlineNum;
	}
	public void setDeviceOnlineNum(int deviceOnlineNum) {
		this.deviceOnlineNum = deviceOnlineNum;
	}
	public int getDeviceTotalNum() {
		return deviceTotalNum;
	}
	public void setDeviceTotalNum(int deviceTotalNum) {
		this.deviceTotalNum = deviceTotalNum;
	}
	
	public String getDeviceTypeDesc() {
		return deviceTypeDesc;
	}
	public void setDeviceTypeDesc(String deviceTypeDesc) {
		this.deviceTypeDesc = deviceTypeDesc;
	}
	public String getDeviceBrandDesc() {
		return deviceBrandDesc;
	}
	public void setDeviceBrandDesc(String deviceBrandDesc) {
		this.deviceBrandDesc = deviceBrandDesc;
	}
	public String getDeviceModelDesc() {
		return deviceModelDesc;
	}
	public void setDeviceModelDesc(String deviceModelDesc) {
		this.deviceModelDesc = deviceModelDesc;
	}
	public String toString() {
		return "ip:["+ip+"]"+
				"port["+port+"]"+
				"updatedTime["+updatedTime+"]"+
				"road["+road+"]"+
				"deviceType["+deviceType+"]"+
				"deviceBrand["+deviceBrand+"]"+
				"deviceModel["+deviceModel+"]"
		;
	}
	public String toWarnString() {
		return "ip:["+ip+"]"+
				"port["+port+"]"+
				"port1["+port1+"]"+
				"port2["+port2+"]"+
				"port3["+port3+"]"
		;
	}
}
