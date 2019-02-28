package com.sbweb.bi.pojo;

public class CountryPojo {
	//数据类型标示
	private String dataHead;
	//乡镇编码
	private int countryCode;
	//乡镇编码
	private String countryName;
	//乡镇在线数量
	private int countryOnlineNum;
	//乡镇设备总量
	private int countryTotalNum;
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public int getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}
	public int getCountryOnlineNum() {
		return countryOnlineNum;
	}
	public void setCountryOnlineNum(int countryOnlineNum) {
		this.countryOnlineNum = countryOnlineNum;
	}
	public int getCountryTotalNum() {
		return countryTotalNum;
	}
	public void setCountryTotalNum(int countryTotalNum) {
		this.countryTotalNum = countryTotalNum;
	}
	/**
	 * @return the dataHead
	 */
	public String getDataHead() {
		return dataHead;
	}
	/**
	 * @param dataHead the dataHead to set
	 */
	public void setDataHead(String dataHead) {
		this.dataHead = dataHead;
	}
	public String toString() {
		return "dataHead:["+dataHead+"]"
				+"countryCode["+countryCode+"]"
				+"countryOnlineNum["+countryOnlineNum+"]"
				+"countryTotalNum["+countryTotalNum+"]"
				;
	}
}
