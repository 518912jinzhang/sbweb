package com.sbweb.init.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sbweb.bi.pojo.ChartsDataResp;
import com.sbweb.bi.pojo.CountryPojo;
import com.sbweb.common.AbstractService;
import com.sbweb.common.Constants;
import com.sbweb.ui.pojo.DevicePojo;
import com.sbweb.ui.pojo.MoniterPojo;
import com.sbweb.ui.pojo.SktResponse;


/**
 * BI侧获取设备故障故障列表
 */
@Service
@PropertySource("classpath:city.properties") 
public class BIService extends AbstractService {
	private static PrintStream ps = null;
	private static Socket s = null;
	private static InputStream is = null;
	private static String CMD_BIDO = "BIDO";
	private static String CMD_DOFI = "DOFI";
	private static String CMD_DTFI = "DTFI";
	private static String CMD_BIDL = "BIDL";
	private static String CMD_DLFI = "DLFI";

	//BI侧获取终端描述
	private static String CMD_BIDE = "BIDE";
	private static String CMD_DEFI = "DEFI";
	private static String CMD_BIDI = "BIDI";

	private static Map<String, String> deviceBrandMap = new HashMap<String, String>();
	private static Map<String, String> deviceTypeMap= new HashMap<String, String>();
	private static Map<String, String> deviceModelMap= new HashMap<String, String>();
	
	@Autowired
    private Environment env;
	
	static {
		//设备品牌
		deviceBrandMap.put("10", "乔安");
		deviceBrandMap.put("11", "海康威视");
		deviceBrandMap.put("12", "海尔");
		deviceBrandMap.put("13", "亚安");
		deviceBrandMap.put("14", "小米");

		//设备类型
		deviceTypeMap.put("0", "电子警察");
		deviceTypeMap.put("1", "治安卡口");
		deviceTypeMap.put("2", "监控");
		deviceTypeMap.put("3", "人脸识别");

		//设备型号
		deviceModelMap.put("110", "DS-2CD1301-I");
		deviceModelMap.put("111", "DS-2CD1311-I");
		deviceModelMap.put("112", "DS-2CD1321-I");

	}
	Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		BIService bService = new BIService();
		//地图终端列表
		//bService.getMoniterPojoList();
		
		//设备统计信息
		//bService.getDeviceStatistics();
		
		//获取故障列表
		//bService.getBreakDownMoniterPojoList();
		
		//获取单台设备信息
		bService.getMoniterInfo("1000000029");
	}

	
	/*
	 * 1.1BI侧获取终端列表
	 * @param token:用户token
	 **/
	public SktResponse getMoniterPojoList() {
		PrintWriter pw = null;
		BufferedReader br = null;
		Socket s = null;
		SktResponse response = new SktResponse();
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			InputStream is = s.getInputStream();
			int total = 0; 
			int numReq = 90;
			int dataReqNum = 90;
			
			System.out.println("BI 查询终端数据开始---");
			//第一次请求
			pojoList = getMoniterDatas(1, dataReqNum, s, pw, br, is, response);
			total = pojoList.get(0).getTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getMoniterDatas(cntReq, dataReqNum, s, pw, br, is, response));
			}
			response.setDatas(pojoList);
			System.out.println("BI 查询终端数据结束---");
			
			//关闭
			s.close();
			pw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			try {
				if(null != s) {s.close();}
				if(null != pw) {pw.close();}
				if(null != br) {br.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}

	public List<MoniterPojo> getMoniterDatas(int idxReq,int dataReqNum, Socket s
			,PrintWriter pw, BufferedReader br
			,InputStream is, SktResponse response) 
			throws UnknownHostException, IOException {
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		if(null != response.getDatas() && response.getDatas().size() > 0) {
			pojoList = response.getDatas();
		}
		String retCode;
		int total = 0;
		//第一次请求发送指令
		if(idxReq == 1) {
			pw.print(CMD_BIDE);
			pw.flush();
		}
		byte[] dataBytes = new byte[1024*40];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		//解析数据头
		int len = is.read(dataBytes);
		System.out.println("server len idxReq【"+idxReq+"】::" + len);
		retCode = subBytes2String(2, dataSubStart, dataBytes);
		response.setRetCode(retCode);
		//可查询终端数量
		total = subBytes2Int(4, dataSubStart, dataBytes);
		response.setRetTotal(total);
		//第一次默认根据阈值查询，如果库中数据个数小于阈值，则更新阈值
		if(dataReqNum > total) {
			dataReqNum = total;
		}
	
		for(int req = 0; req<dataReqNum; req++) {
			MoniterPojo moniter = new MoniterPojo();
			// 终端编号
			String moniterId = ""+subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setId(moniterId);
			// 经度
			
			int lon = subBytes2Int(4, dataSubStart, dataBytes);
			if(lon>0) {
				moniter.setLon(subBytes2String(lon, dataSubStart, dataBytes));
			}
			// 纬度
			int lat = subBytes2Int(4, dataSubStart, dataBytes);
			if(lat>0) {
				moniter.setLat(subBytes2String(lat, dataSubStart, dataBytes));
			}

			System.out.println(moniter.toString());
			if(lon>0 && lat>0) {
				pojoList.add(moniter);
			} else {
				System.out.println("终端信息经纬度信息为空："+moniter.getId());
			}
			
		}

		//最后一次不发送结束指令
		pw.print((CMD_DEFI));
		pw.flush();
		return pojoList;
	}
	

	/*
	 * 1.3 BI侧获取终端故障列表
	 * @param token:用户token
	 * @param id:终端ID
	 * @param desc:终端描述
	 * @param townCode:乡镇编码
	 * 
	 * @return 终端故障列表
	 **/
	public SktResponse getBreakDownMoniterPojoList() {
		PrintStream ps = null;
		Socket s = null;
		SktResponse response = new SktResponse();
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			int total = 0; 
			int numReq = 10;
			int dataReqNum = 10;

			//第一次请求
			pojoList = getBreakDownDatas(1, dataReqNum, s, ps, response);
			total = response.getRetTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getBreakDownDatas(cntReq, dataReqNum, s, ps, response));
			}
			
			//告警原因转换
			
			response.setDatas(pojoList);
			System.out.println("告警终端数量："+response.getDatas().size());
			
			//关闭
			s.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			try {
				if(null != s) {s.close();}
				if(null != ps) {ps.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}
	

	/* *
	 * 获取终端故障列表
	 * @param moniterId 终端ID 
	 * @param warnType 告警类型
	 * */
	public List<MoniterPojo> getBreakDownDatas(int idxReq,int dataReqNum, Socket s,
			PrintStream ps, SktResponse response) 
			throws UnknownHostException, IOException {
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		if(null != response.getDatas() && response.getDatas().size() > 0) {
			pojoList = response.getDatas();
		}
		InputStream is = null;
		String retCode;
		int total = 0;
		//第一次请求发送指令
		if(idxReq == 1) {
			byte[] breakDownParams = new byte[1024];
			System.arraycopy(strToByteArray(CMD_BIDL),
					0, breakDownParams, 0, 4);

			ps.write(breakDownParams);
			ps.flush();
		}

		is = s.getInputStream();
		byte[] dataBytes = new byte[1024*40];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		//解析数据头
		int len = is.read(dataBytes);
		System.out.println("server len idxReq【"+idxReq+"】::" + len);
		retCode = subBytes2String(2, dataSubStart, dataBytes);
		response.setRetCode(retCode);
		//告警终端数量
		total = subBytes2Int(4, dataSubStart, dataBytes);
		response.setRetTotal(total);
		//第一次默认根据阈值查询，如果库中数据个数小于阈值，则更新阈值
		if(dataReqNum > total) {
			dataReqNum = total;
		}
	
		for(int req = 0; req<dataReqNum; req++) {
			MoniterPojo moniter = new MoniterPojo();
			
			// 终端编号
			String id = ""+subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setId(id);
			// 告警时间
			int warnDate = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setWarnDate(subBytes2String(warnDate, dataSubStart, dataBytes));
			// IMEI
			moniter.setImei(subBytes2String(15, dataSubStart, dataBytes));
			// 终端描述
			int desc = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setDesc(subBytes2String(desc, dataSubStart, dataBytes));
			// 注册时间
			int createTime = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCreateTime(subBytes2String(createTime, dataSubStart, dataBytes));
			// 最后刷新时间
			int updatedTime = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setUpdatedTime(subBytes2String(updatedTime, dataSubStart, dataBytes));
			// 版本号
			moniter.setVersion(subBytes2Int(4, dataSubStart, dataBytes));
			// 信号强度
			moniter.setSignal(subBytes2Int(4, dataSubStart, dataBytes));
			// 省市县乡镇编号
			int province = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setProvinceCode(subBytes2String(province, dataSubStart, dataBytes));
			int city = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCityCode(subBytes2String(city, dataSubStart, dataBytes));
			int country = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCountryCode(subBytes2String(country, dataSubStart, dataBytes));
			int town = subBytes2Int(4, dataSubStart, dataBytes);
			if(town>0) {
				moniter.setTownName(subBytes2String(town, dataSubStart, dataBytes));
			}
			// 经度
			int lon = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setLon(subBytes2String(lon, dataSubStart, dataBytes));
			// 纬度
			int lat = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setLat(subBytes2String(lat, dataSubStart, dataBytes));

			//电流告警位
			moniter.setWarn1(subBytes2Int(4, dataSubStart, dataBytes));
			moniter.setWarn2(subBytes2Int(4, dataSubStart, dataBytes));
			moniter.setWarn3(subBytes2Int(4, dataSubStart, dataBytes));
			moniter.setWarn4(subBytes2Int(4, dataSubStart, dataBytes));
			moniter.setWarn5(subBytes2Int(4, dataSubStart, dataBytes));
			moniter.setWarn6(subBytes2Int(4, dataSubStart, dataBytes));

			List<DevicePojo> deviceList = new ArrayList<DevicePojo>();
			//前台设备数量
			int deviceNum = subBytes2Int(4, dataSubStart, dataBytes);
			for(int num=0; num < deviceNum; num++) {
				DevicePojo device = new DevicePojo();
				//设备IP地址
				int ip = subBytes2Int(4, dataSubStart, dataBytes);
				device.setIp(subBytes2String(ip, dataSubStart, dataBytes));

				//描述朝向
				int roadDesc = subBytes2Int(4, dataSubStart, dataBytes);
				device.setDesc(subBytes2String(roadDesc, dataSubStart, dataBytes));

				//端口号
				device.setPort(subBytes2Int(4, dataSubStart, dataBytes));
				
				//设备电源、双工、会话状态
				//前端设备电流位值为0，不封装结构字段2的字段5
				if(moniter.getWarn3() == 1) {
					device.setPort1(subBytes2Int(4, dataSubStart, dataBytes));
				} else {
					device.setPort1(1);
				}
				if(moniter.getWarn4() == 1) {
					device.setPort2(subBytes2Int(4, dataSubStart, dataBytes));
				} else {
					device.setPort2(1);
				}
				if(moniter.getWarn5() == 1) {
					device.setPort3(subBytes2Int(4, dataSubStart, dataBytes));
				} else {
					device.setPort3(1);
				}

				System.out.println(device.toWarnString());
				deviceList.add(device);
			}
			moniter.setDeviceList(deviceList);
			System.out.println(moniter.toWarnString());
			pojoList.add(moniter);
		}

		convertWarnDesc(pojoList);
		//最后一次不发送结束指令
		ps.print((CMD_DLFI));
		ps.flush();
		return pojoList;
	}
	
	/*
	 * 获取关注列表所有终端编号
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 * @param attentionType: 关注类型
	 **/
	private void convertWarnDesc(List<MoniterPojo> list) {
		if(null != list) {
			for(MoniterPojo pojo : list) {
				String warnDesc="";
				//判断断电
				if(pojo.getWarn1() == 1) {
					warnDesc = "电源口1电流输入中断（断电）";
					pojo.setWarnDesc(warnDesc);
					System.out.println("终端告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
					continue;
				}
				//终端心跳
				if(pojo.getWarn2() == 1) {
					warnDesc = "终端心跳异常";
					pojo.setWarnDesc(warnDesc);
					System.out.println("终端告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
					continue;
				}
				//前端设备电流异常
				warnDesc = pojo.getDesc();
				List<DevicePojo> dList = pojo.getDeviceList();
				if(null != dList) {
					//过滤终端下面报警的设备
					for(DevicePojo device : dList) {
						if(device.getPort1() == 0) {
							warnDesc += device.getDesc();
							warnDesc += "("+device.getIp()+")";
							warnDesc += "--电流异常";
							break;
						}
						if(device.getPort2() == 0) {
							warnDesc += device.getDesc();
							warnDesc += "("+device.getIp()+")";
							warnDesc += "--网络异常";
							break;
						}
						if(device.getPort3() == 0) {
							warnDesc += device.getDesc();
							warnDesc += "("+device.getIp()+")";
							warnDesc += "--会话异常";
							break;
						}
					}
				}
				//开箱状态
				if(pojo.getWarn6() == 1) {
					warnDesc += "--箱门开启";
				}
				pojo.setWarnDesc(warnDesc);
				System.out.println("设备告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
				continue;
			}
		}
		
	}
	

	/*
	 * 1.4 BI侧获取单台终端信息
	 * @param token: 终端token
	 * @param moniterId: 终端编号
	 **/
	public MoniterPojo getMoniterInfo(String moniterId) {
		//moniterId = scanner.next();
		String retCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		MoniterPojo moniter = new MoniterPojo();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			byte[] queryParamsValidate = new byte[32];
			System.arraycopy(strToByteArray(CMD_BIDI),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, queryParamsValidate, 4, 4);
			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024];
			int len = is.read(dataBytes);
			System.out.println("BI query single moniter receive data len:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);
				
			moniter.setRetCode(retCode);
			//可以查询
			if(Constants.COMMON_DI.equals(retCode)) {
				// 终端编号
				moniter.setId(""+subBytes2Int(4, dataSubStart, dataBytes));
				// 终端类型(版本号)
				moniter.setMoniterType(""+subBytes2Int(4, dataSubStart, dataBytes));
				//电源口1-4
				moniter.setPort1(""+subBytes2Int(1, dataSubStart, dataBytes));
				moniter.setPort2(""+subBytes2Int(1, dataSubStart, dataBytes));
				if("0".equals(moniter.getMoniterType())) {
					moniter.setPort3(""+subBytes2Int(1, dataSubStart, dataBytes));
					moniter.setPort4(""+subBytes2Int(1, dataSubStart, dataBytes));
				}
				//网络端口1-4
				moniter.setWlanPort1(""+subBytes2Int(1, dataSubStart, dataBytes));
				moniter.setWlanPort2(""+subBytes2Int(1, dataSubStart, dataBytes));
				if("0".equals(moniter.getMoniterType())) {
					moniter.setWlanPort3(""+subBytes2Int(1, dataSubStart, dataBytes));
					moniter.setWlanPort4(""+subBytes2Int(1, dataSubStart, dataBytes));
				}
				//服务器端IP地址
				int ip = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setIp(subBytes2String(ip, dataSubStart, dataBytes));
				//服务器端IP地址最后刷新时间
				int updatedTime = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setUpdatedTime(subBytes2String(updatedTime, dataSubStart, dataBytes));
				// 终端注册设备数量
				int deviceNum = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setRegisterDeviceNum(deviceNum);
				// 省市县乡镇编号
				int province = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setProvinceCode(subBytes2String(province, dataSubStart, dataBytes));
				int city = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setCityCode(subBytes2String(city, dataSubStart, dataBytes));
				int country = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setCountryCode(subBytes2String(country, dataSubStart, dataBytes));
				int town = subBytes2Int(4, dataSubStart, dataBytes);
				if(town>0) {
					moniter.setTownName(subBytes2String(town, dataSubStart, dataBytes));
				}
				
				// IMEI
				//moniter.setImei(subBytes2String(15, dataSubStart, dataBytes));
				// 终端描述
				int desc = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setDesc(subBytes2String(desc, dataSubStart, dataBytes));
				//最后一次心跳时间
				int lhbTime = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setLastHbTime(subBytes2String(lhbTime, dataSubStart, dataBytes));
				
				//设备相关信息
				if(deviceNum>0) {
					List<DevicePojo> deviceList = new ArrayList<DevicePojo>();
					for(int num=0; num < deviceNum; num++) {
						DevicePojo device = new DevicePojo();
						//设备IP地址
						int deviceIp = subBytes2Int(4, dataSubStart, dataBytes);
						device.setIp(subBytes2String(deviceIp, dataSubStart, dataBytes));
						//设备IP地址最后刷新时间
						int deviceUpTime = subBytes2Int(4, dataSubStart, dataBytes);
						device.setUpdatedTime(subBytes2String(deviceUpTime, dataSubStart, dataBytes));
						//设备所在端口
						device.setPort(subBytes2Int(4, dataSubStart, dataBytes));
						//设备类型，品牌，型号
						device.setDeviceType(subBytes2Int(4, dataSubStart, dataBytes));
						device.setDeviceBrand(subBytes2Int(4, dataSubStart, dataBytes));
						device.setDeviceModel(subBytes2Int(4, dataSubStart, dataBytes));
						//设备描述
						int deviceDesc = subBytes2Int(4, dataSubStart, dataBytes);
						device.setDesc(subBytes2String(deviceDesc, dataSubStart, dataBytes));

						System.out.println(device.toString());
						deviceList.add(device);
					}
				}
				
				System.out.println(moniter.toString());
			} else {
				System.out.println("查询单台终端信息失败");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		return moniter;
	}

	private String intToByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++) {
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		}

		return (byteArray[0]+""+byteArray[1]+byteArray[2]+byteArray[3]);
	}

	/*
	 * 获取设备总在线率信息
	 **/
	public ChartsDataResp getDeviceStatistics() {
		ChartsDataResp chartsDataResp = new ChartsDataResp();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			is = s.getInputStream();
			int total = 0; 
			int numReq = 20;//协议每次最多请求个数
			int dataReqNum = 20;//单次读取socket获取个数

			//乡镇数据
			getCountryDatas(1, dataReqNum, s, ps, is, chartsDataResp);
			total = chartsDataResp.getCountriesNum();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求乡镇数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				getCountryDatas(cntReq, dataReqNum, s, ps, is, chartsDataResp);
			}
			System.out.println("乡镇总数量："+chartsDataResp.getCountriesNum());
			

			total = 0; 
			numReq = 20;
			dataReqNum = 20;

			//设备数据
			getDeviceTypeDatas(1, dataReqNum, s, is, chartsDataResp);
			total = chartsDataResp.getDeviceNum();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求设备数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				getDeviceTypeDatas(cntReq, dataReqNum, s, is,chartsDataResp);
			}
			System.out.println("设备总数量："+chartsDataResp.getDeviceNum());
			//关闭
			s.close();
			ps.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			try {
				if(null != s) {s.close();}
				if(null != ps) {ps.close();}
				if(null != is) {is.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return chartsDataResp;
	}
	
	/*
	 * 获取设备总在线率信息
	 * 
	 * 创建socket连接，解析数据结构体
	 * */
	private void getCountryDatas(int idxReq,int dataReqNum, Socket s
			,PrintStream ps, InputStream is, ChartsDataResp chartsDataResp) 
			throws UnknownHostException, IOException {
		List<CountryPojo> pojoList = new ArrayList<CountryPojo>();
		int total = 0;
		//第一次请求发送指令
		//信令1：BI->MriUIBI
		if(idxReq == 1) {
			System.out.println("Client input cmd:");
			String str = CMD_BIDO;
			ps.print((str));
			ps.flush();
		}
		byte[] dataBytes = new byte[2048];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		String dataHead = "";
		//信令2：MriUIBI->BI
		int len = is.read(dataBytes);
		System.out.println("server len idxReq【"+idxReq+"】::" + len);
		//数据类型标示
		dataHead = subBytes2String(2, dataSubStart, dataBytes);
		//乡镇数量
		byte[] warnNumber = subBytes(4, dataSubStart, dataBytes);
		total = byteArrayToInt(warnNumber);
		chartsDataResp.setCountriesNum(total);
		//设备总在线数量
		byte[] countryOnlineNum = subBytes(4, dataSubStart, dataBytes);
		chartsDataResp.setDeviceOnlineNum(chartsDataResp.getDeviceOnlineNum()+byteArrayToInt(countryOnlineNum));
		//设备总数量
		byte[] countryTotalNum = subBytes(4, dataSubStart, dataBytes);
		chartsDataResp.setDeviceTotalNum(byteArrayToInt(countryTotalNum));
		if(chartsDataResp.getCountriesNum()<dataReqNum) {
			dataReqNum = chartsDataResp.getCountriesNum();
		}
		for(int req = 0; req<dataReqNum; req++) {
			CountryPojo countryPojo = new CountryPojo();
			//解析乡镇数据
			// 乡镇编码
			countryPojo.setDataHead(dataHead);
			int townCode = subBytes2Int(4, dataSubStart, dataBytes);
			countryPojo.setCountryName(subBytes2String(townCode, dataSubStart, dataBytes));

			//乡镇在线数量
			countryPojo.setCountryOnlineNum(subBytes2Int(4, dataSubStart, dataBytes));
			
			//乡镇设备总量
			countryPojo.setCountryTotalNum(subBytes2Int(4, dataSubStart, dataBytes));

			System.out.println(countryPojo.toString());
			pojoList.add(countryPojo);
		}
		List<CountryPojo> list = chartsDataResp.getCoutryList();
		if(null == list) {
			list = new ArrayList<CountryPojo>();
		}
		list.addAll(pojoList);
		chartsDataResp.setCoutryList(list);

		//信令3：BI->MriUIBI
		ps.print((CMD_DOFI));
		ps.flush();
	}

	/*
	 * 获取设备类型统计信息
	 * */
	private void getDeviceTypeDatas(int idxReq,int dataReqNum, Socket s
			, InputStream is, ChartsDataResp charstDataResp) 
			throws UnknownHostException, IOException {
		List<DevicePojo> pojoList = new ArrayList<DevicePojo>();
		int total = 0;
		byte[] dataBytes = new byte[2048];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		//信令4：MriUIBI->BI
		//解析设备类型
		for(int req = 0; req<dataReqNum; req++) {
			DevicePojo devicePojo = new DevicePojo();
			if(req == 0) {
				int len = is.read(dataBytes);
				System.out.println("BI设备类型在线统计【"+idxReq+"】::" + len);

				//数据类型标示
				devicePojo.setDataHead(subBytes2String(2, dataSubStart, dataBytes));

				//设备类型数量
				byte[] deviceNum = subBytes(4, dataSubStart, dataBytes);
				total = byteArrayToInt(deviceNum);
				charstDataResp.setDeviceNum(total);
			}
			//解析设备数据
			//设备类型
			byte[] deviceType = subBytes(4, dataSubStart, dataBytes);
			devicePojo.setDeviceType(byteArrayToInt(deviceType));
			
			//设备类型在线数量
			byte[] deviceOnlineNum = subBytes(4, dataSubStart, dataBytes);
			devicePojo.setDeviceOnlineNum(byteArrayToInt(deviceOnlineNum));
			
			//设备类型总量
			byte[] deviceTotalNum = subBytes(4, dataSubStart, dataBytes);
			devicePojo.setDeviceTotalNum(byteArrayToInt(deviceTotalNum));

			System.out.println("BI设备类型在线统计："+devicePojo.toString());
			pojoList.add(devicePojo);
		}
		List<DevicePojo> list = charstDataResp.getDeviceList();
		if(null == list) {
			list = new ArrayList<DevicePojo>();
		}
		list.addAll(pojoList);
		charstDataResp.setDeviceList(list);

		//信令5：BI->MriUIBI
		ps.print((CMD_DTFI));
		ps.flush();
	}
	
	/*
	 * 关闭资源连接
	 * */
	private void close(Socket s, PrintStream ps, InputStream is) {
		//关闭
		try {
			if(null != s) {s.close();}
			if(null != ps) {ps.close();}
			if(null != is) {is.close();}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
