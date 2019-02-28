package single.thread.pojo;

/*
 * 获取设备故障故障列表
 */
public class BreakDownPojo {
	//数据结构体标示
	private String dataHead;
	//告警设备数量
	private int warnNumber;
	//终端编号
	private int moniterId;
	//设备类型
	private int deviceType;
	//设备IP地址
	private String deviceIp;
	//设备IP地址最后刷新时间
	private String deviceIpUpdatedTime;
	//设备描述字符串
	private String deviceDesc;
	//设备描述字符串
	private int devicePort;
	
	public String getDataHead() {
		return dataHead;
	}
	public void setDataHead(String dataHead) {
		this.dataHead = dataHead;
	}
	public int getWarnNumber() {
		return warnNumber;
	}
	public void setWarnNumber(int warnNumber) {
		this.warnNumber = warnNumber;
	}
	public int getMoniterId() {
		return moniterId;
	}
	public void setMoniterId(int moniterId) {
		this.moniterId = moniterId;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getDeviceIpUpdatedTime() {
		return deviceIpUpdatedTime;
	}
	public void setDeviceIpUpdatedTime(String deviceIpUpdatedTime) {
		this.deviceIpUpdatedTime = deviceIpUpdatedTime;
	}
	public String getDeviceDesc() {
		return deviceDesc;
	}
	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}
	public int getDevicePort() {
		return devicePort;
	}
	public void setDevicePort(int devicePort) {
		this.devicePort = devicePort;
	}
	
	public String toString() {
		return "warnNumber:["+warnNumber+"]"
				+"moniterId["+moniterId+"]"
				+"deviceType["+deviceType+"]"
				+"deviceIp["+deviceIp+"]"
				+"deviceIpUpdatedTime["+deviceIpUpdatedTime+"]"
				+"deviceDesc["+deviceDesc+"]"
				+"devicePort["+devicePort+"]"
				;
	}
}