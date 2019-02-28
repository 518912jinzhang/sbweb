package com.sbweb.bi.pojo;

import java.util.List;

import com.sbweb.ui.pojo.DevicePojo;

public class ChartsDataResp {

	//乡镇数量
	private int countriesNum;
	//设备类型数量
	private int deviceNum;
	//设备总在线数量
	private int deviceOnlineNum;
	//设备总数量
	private int deviceTotalNum;
	//乡镇列表
	private List<CountryPojo> coutryList;
	//设备类型列表
	private List<DevicePojo> deviceList;
	public int getCountriesNum() {
		return countriesNum;
	}
	public void setCountriesNum(int countriesNum) {
		this.countriesNum = countriesNum;
	}
	
	/**
	 * @return the deviceNum
	 */
	public int getDeviceNum() {
		return deviceNum;
	}
	/**
	 * @param deviceNum the deviceNum to set
	 */
	public void setDeviceNum(int deviceNum) {
		this.deviceNum = deviceNum;
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
	public List<CountryPojo> getCoutryList() {
		return coutryList;
	}
	public void setCoutryList(List<CountryPojo> coutryList) {
		this.coutryList = coutryList;
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
	public String toString() {
		return "countriesNum:["+countriesNum+"]"
				+"deviceOnlineNum["+deviceOnlineNum+"]"
				+"deviceTotalNum["+deviceTotalNum+"]"
				;
	}
}
