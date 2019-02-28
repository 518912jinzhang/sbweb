package com.sbweb.ui.pojo;

import java.util.List;

/*
 * 终端实体类
 * */
public class MoniterPojo {
	//终端编号
	private String id;
	//终端类型
	private String moniterType;
	//电源口1
	private String port1;
	//电源口2
	private String port2;
	//电源口3
	private String port3;
	//电源口4
	private String port4;
	//网络端口1
	private String wlanPort1;
	//网络端口2
	private String wlanPort2;
	//网络端口3
	private String wlanPort3;
	//网络端口4
	private String wlanPort4;
	//服务器端IP地址
	private String ip;
	//IMEI
	private String imei;
	//终端描述
	private String desc;
	//注册时间
	private String createTime;
	//最后刷新
	private String updatedTime;
	//最后心跳时间
	private String beatheartTime;
	//版本号
	private int version;
	//信号强度
	private int signal;
	//心跳
	private int beatheart;
	//电源状态
	private int powerStatus;
	//电源状态
	private String powerStr;
	//双工状态
	private int duplexStatus;
	//双工状态
	private String duplexStr;
	//省
	private String provinceCode;
	//省
	private String cityCode;
	//市
	private String townCode;
	//县
	private String countryCode;
	//乡镇名
	private String townName;
	//经度
	private String lon;
	//纬度
	private String lat;
	//设备列表
	private List<DevicePojo> deviceList;
	private String token;
	private int total;
	//retCode
	private String retCode;
	//告警类型
	private int warnType;
	//告警时间
	private String warnDate;
	//终端注册设备数量
	private int registerDeviceNum;
	//最后一次心跳时间字符串
	private String lastHbTime;
	
	
	/* 卡口设备参数 */
	//电流强度
	private String electronFlow;
	//抓拍单元电流值
	private String videoElectronFlow;
	//常亮灯电流值
	private String lampElectronFlow;
	//爆闪频次
	private String flashLampTimes;
	//脉冲信号
	private String lampSignal;

	//电流告警位
	private int warn1;
	//终端心跳告警位
	private int warn2;
	//前端设备电流位
	private int warn3;
	//网络连接告警位：双工状态
	private int warn4;
	//会话告警位
	private int warn5;
	//箱门开启
	private int warn6;
	//告警原因
	private String warnDesc;
	//所属关注类型
	private String attentionType;
	
	public String getBeatheartTime() {
		return beatheartTime;
	}

	public void setBeatheartTime(String beatheartTime) {
		this.beatheartTime = beatheartTime;
	}

	public String getDuplexStr() {
		return duplexStr;
	}

	public void setDuplexStr(String duplexStr) {
		this.duplexStr = duplexStr;
	}

	public String getAttentionType() {
		return attentionType;
	}

	public void setAttentionType(String attentionType) {
		this.attentionType = attentionType;
	}

	public String getPowerStr() {
		return powerStr;
	}

	public void setPowerStr(String powerStr) {
		this.powerStr = powerStr;
	}

	public String getWarnDesc() {
		return warnDesc;
	}

	public void setWarnDesc(String warnDesc) {
		this.warnDesc = warnDesc;
	}

	public int getWarn1() {
		return warn1;
	}

	public void setWarn1(int warn1) {
		this.warn1 = warn1;
	}

	public int getWarn2() {
		return warn2;
	}

	public void setWarn2(int warn2) {
		this.warn2 = warn2;
	}

	public int getWarn3() {
		return warn3;
	}

	public void setWarn3(int warn3) {
		this.warn3 = warn3;
	}

	public int getWarn4() {
		return warn4;
	}

	public void setWarn4(int warn4) {
		this.warn4 = warn4;
	}

	public int getWarn5() {
		return warn5;
	}

	public void setWarn5(int warn5) {
		this.warn5 = warn5;
	}

	public int getWarn6() {
		return warn6;
	}

	public void setWarn6(int warn6) {
		this.warn6 = warn6;
	}

	public String getWarnDate() {
		return warnDate;
	}

	public void setWarnDate(String warnDate) {
		this.warnDate = warnDate;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getMoniterType() {
		return moniterType;
	}

	public void setMoniterType(String moniterType) {
		this.moniterType = moniterType;
	}

	/**
	 * @return the beatheart
	 */
	public int getBeatheart() {
		return beatheart;
	}

	/**
	 * @param beatheart the beatheart to set
	 */
	public void setBeatheart(int beatheart) {
		this.beatheart = beatheart;
	}

	/**
	 * @return the powerStatus
	 */
	public int getPowerStatus() {
		return powerStatus;
	}

	/**
	 * @param powerStatus the powerStatus to set
	 */
	public void setPowerStatus(int powerStatus) {
		this.powerStatus = powerStatus;
	}

	/**
	 * @return the duplexStatus
	 */
	public int getDuplexStatus() {
		return duplexStatus;
	}

	/**
	 * @param duplexStatus the duplexStatus to set
	 */
	public void setDuplexStatus(int duplexStatus) {
		this.duplexStatus = duplexStatus;
	}

	/**
	 * @return the deviceList
	 */
	public List<DevicePojo> getDeviceList() {
		return deviceList;
	}

	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List<DevicePojo> deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the townCode
	 */
	public String getTownCode() {
		return townCode;
	}

	/**
	 * @param townCode the townCode to set
	 */
	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}

	/**
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the retCode
	 */
	public String getRetCode() {
		return retCode;
	}

	/**
	 * @param retCode the retCode to set
	 */
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the signal
	 */
	public int getSignal() {
		return signal;
	}

	/**
	 * @param signal the signal to set
	 */
	public void setSignal(int signal) {
		this.signal = signal;
	}

	/**
	 * @return the townName
	 */
	public String getTownName() {
		return townName;
	}

	/**
	 * @param townName the townName to set
	 */
	public void setTownName(String townName) {
		this.townName = townName;
	}

	public int getWarnType() {
		return warnType;
	}

	public void setWarnType(int warnType) {
		this.warnType = warnType;
	}

	public String getPort1() {
		return port1;
	}

	public void setPort1(String port1) {
		this.port1 = port1;
	}

	public String getPort2() {
		return port2;
	}

	public void setPort2(String port2) {
		this.port2 = port2;
	}

	public String getPort3() {
		return port3;
	}

	public void setPort3(String port3) {
		this.port3 = port3;
	}

	public String getPort4() {
		return port4;
	}

	public void setPort4(String port4) {
		this.port4 = port4;
	}

	public String getWlanPort1() {
		return wlanPort1;
	}

	public void setWlanPort1(String wlanPort1) {
		this.wlanPort1 = wlanPort1;
	}

	public String getWlanPort2() {
		return wlanPort2;
	}

	public void setWlanPort2(String wlanPort2) {
		this.wlanPort2 = wlanPort2;
	}

	public String getWlanPort3() {
		return wlanPort3;
	}

	public void setWlanPort3(String wlanPort3) {
		this.wlanPort3 = wlanPort3;
	}

	public String getWlanPort4() {
		return wlanPort4;
	}

	public void setWlanPort4(String wlanPort4) {
		this.wlanPort4 = wlanPort4;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getRegisterDeviceNum() {
		return registerDeviceNum;
	}

	public void setRegisterDeviceNum(int registerDeviceNum) {
		this.registerDeviceNum = registerDeviceNum;
	}

	public String getLastHbTime() {
		return lastHbTime;
	}

	public void setLastHbTime(String lastHbTime) {
		this.lastHbTime = lastHbTime;
	}

	
	
	public String getElectronFlow() {
		return electronFlow;
	}

	public void setElectronFlow(String electronFlow) {
		this.electronFlow = electronFlow;
	}

	public String getVideoElectronFlow() {
		return videoElectronFlow;
	}

	public void setVideoElectronFlow(String videoElectronFlow) {
		this.videoElectronFlow = videoElectronFlow;
	}

	public String getLampElectronFlow() {
		return lampElectronFlow;
	}

	public void setLampElectronFlow(String lampElectronFlow) {
		this.lampElectronFlow = lampElectronFlow;
	}

	public String getFlashLampTimes() {
		return flashLampTimes;
	}

	public void setFlashLampTimes(String flashLampTimes) {
		this.flashLampTimes = flashLampTimes;
	}

	public String getLampSignal() {
		return lampSignal;
	}

	public void setLampSignal(String lampSignal) {
		this.lampSignal = lampSignal;
	}

	public String toString() {
		return "id:["+id+"]"+
				"imei["+imei+"]"+
				"desc["+desc+"]"+
				"createTime["+createTime+"]"+
				"updatedTime["+updatedTime+"]"+
				"version["+version+"]"+
				"signal["+signal+"]"+
				"townCode["+townCode+"]"+
				"townName["+townName+"]"+
				"lon["+lon+"]"+
				"lat["+lat+"]"+
				"token["+token+"]"+
				"total["+total+"]"
		;
	}
	
	public String toControlString() {
		return 
				"id["+id+"]"+
				"beatheart["+beatheart+"]"+
				"powerStatus["+powerStatus+"]"+
				"duplexStatus["+duplexStatus+"]"+
				"version["+version+"]"+
				"electronFlow["+electronFlow+"]"+
				"videoElectronFlow["+videoElectronFlow+"]"+
				"lampElectronFlow["+lampElectronFlow+"]"+
				"flashLampTimes["+flashLampTimes+"]"+
				"lampSignal["+lampSignal+"]"
				;
	}
	public String toWarnString() {
		return "id:["+id+"]"+
				"imei["+imei+"]"+
				"warn1["+warn1+"]"+
				"warn2["+warn2+"]"+
				"warn3["+warn3+"]"+
				"warn4["+warn4+"]"+
				"warn5["+warn5+"]"+
				"warn6["+warn6+"]"
		;
	}
}
