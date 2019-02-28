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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbweb.bi.pojo.LogInfo;
import com.sbweb.bi.pojo.TestPerson;
import com.sbweb.common.AbstractService;
import com.sbweb.common.Constants;
import com.sbweb.common.DateUtil;
import com.sbweb.common.HexUtil;
import com.sbweb.common.UUIDUtils;
import com.sbweb.ui.pojo.AttentionMoniter;
import com.sbweb.ui.pojo.DevicePojo;
import com.sbweb.ui.pojo.MoniterPojo;
import com.sbweb.ui.pojo.SktResponse;

/*
 * 终端相关处理service
 * */
@Service
@PropertySource("classpath:system.properties")
public class MoniterService extends AbstractService {
	private static String CMD_UITR = "UITR";//终端新增验证
	private static String CMD_UIAC = "UIAC";//关注列表新增验证
	private static String CMD_UITD = "UITD";//终端删除验证
	private static String CMD_UITM = "UITM";//终端修改
	private static String CMD_UISE = "UISE";//心跳，电源，双工修改
	private static String CMD_UIMM = "UIMM";//设备修改
	private static String CMD_UIGL = "UIGL";//获取终端运行日志
	private static String CMD_UIMR = "UIMR";//设备重启
	private static String CMD_TR = "TR";//终端注册
	private static String CMD_TD = "TD";//终端删除
	private static String QUERY_START_FLAG = "UITL";
	private static String QUERY_END_FLAG = "TLFI";
	private static String QUERY_END_GLFI = "GLFI";
	private static String CMD_UITA = "UITA";
	private static String CMD_TAFI = "TAFI";//告警列表结束
	private static String CMD_UITI = "UITI";//终端信息查询
	private static String CMD_UICC = "UICC";//终端控制台信息查询
	private static String CMD_UIML = "UIML";//设备列表查询
	private static String CMD_UIMI = "UIMI";//设备信息查询
	private static String CMD_UICO = "UICO";//关注列表查询
	//终端告警类型
	private static Map<String, String> warnTypes = new HashMap<String, String>();
	
	@Autowired
    private Environment env;

	static {
		warnTypes.put("1", "电流输入中断（断电）");
		warnTypes.put("2", "前端设备电流异常");
		warnTypes.put("3", "网络连接异常");
		warnTypes.put("4", "会话异常");
		warnTypes.put("5", "终端心跳超时");
		warnTypes.put("6", "箱门开启");
	}
	Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		MoniterPojo pojo = new MoniterPojo();
		pojo.setId("200002");
		pojo.setImei(UUIDUtils.get12UUID()+"000");
		pojo.setDesc("王峰测试修改");
		pojo.setLon("115.103553");
		pojo.setLat("34.413983");
		pojo.setTownCode("2");
		
		//终端添加
		MoniterService service = new MoniterService();
		//service.add("",pojo);

		//终端列表
		//service.getMoniterPojoList("");
		
		//单台终端信息
		//service.getMoniterInfo("", "");
		
		//终端删除
		//service.deleteByToken("", "");
		
		//service.editMoniterByToken("", pojo);
		
		//设备列表
		//service.getDeviceList("","");
		//设备详情
		//service.getDeviceInfo("","","192.168.1.1");
		
		AttentionMoniter att =  new AttentionMoniter();
		att.setNum(4);
		att.setUserId("root");
		att.setAttentionType("关注0701");
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(111111);
		ids.add(123456);
		ids.add(200001);
		ids.add(200002);
		att.setMoniterIdList(ids);
		//service.addAttentionMoniter("134433155164578749137728",att);
		
		//测试关注列表
		/*List<Integer> list = service.getAttentionMoniterIds("134433155164578749137728", "", "");
		for(Integer i : list) {
			System.out.println(i);
		}*/
		
		//故障列表
		//service.getBreakDownMoniterPojoList("065957296256427382396139","999999","999999");
	}

	/*
	 * 终端信息查询
	 * @param token: 终端token
	 * @param moniterId: 终端编号
	 **/
	public MoniterPojo getMoniterInfo(String token, String moniterId) {
		//token = scanner.next();
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
			System.arraycopy(strToByteArray(CMD_UITI),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, queryParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, queryParamsValidate, 4+token.length(), 4);

			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024*2];
			int len = is.read(dataBytes);
			System.out.println("query moniter receive data len:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);

			moniter.setRetCode(retCode);
			//可以查询
			if(Constants.COMMON_CC.equals(retCode)) {
				// 终端编号
				moniter.setId(""+subBytes2Int(4, dataSubStart, dataBytes));
				// IMEI
				moniter.setImei(subBytes2String(15, dataSubStart, dataBytes));
				// 终端描述
				int desc = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setDesc(subBytes2String(desc, dataSubStart, dataBytes));
				// 注册时间
				int createTime = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setCreateTime(subBytes2String(createTime, dataSubStart, dataBytes));
				// 最后协商时间
				int updatedTime = subBytes2Int(4, dataSubStart, dataBytes);
				if(updatedTime > 0) {
					moniter.setUpdatedTime(subBytes2String(updatedTime, dataSubStart, dataBytes));
				}
				// 版本号
				moniter.setVersion(subBytes2Int(4, dataSubStart, dataBytes));
				// 最后心跳时间
				int beatHeart = subBytes2Int(4, dataSubStart, dataBytes);
				if (beatHeart>0) {
					moniter.setBeatheartTime(subBytes2String(beatHeart, dataSubStart, dataBytes));
				}
				// 信号强度
				moniter.setSignal(subBytes2Int(4, dataSubStart, dataBytes));
				// 省
				int province = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setProvinceCode(subBytes2String(province, dataSubStart, dataBytes));
				// 市
				int city = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setCityCode(subBytes2String(city, dataSubStart, dataBytes));
				// 县
				int country = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setCountryCode(subBytes2String(country, dataSubStart, dataBytes));
				// 乡
				int townCode = subBytes2Int(4, dataSubStart, dataBytes);
				if(townCode>0) {
					moniter.setTownName(subBytes2String(townCode, dataSubStart, dataBytes));
					moniter.setTownCode(moniter.getTownName());
				}
				// 经度
				int lon = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setLon(subBytes2String(lon, dataSubStart, dataBytes));
				// 纬度
				int lat = subBytes2Int(4, dataSubStart, dataBytes);
				moniter.setLat(subBytes2String(lat, dataSubStart, dataBytes));

				System.out.println(moniter.toString());
			} else {
				System.out.println("查询终端信息失败:"+retCode);
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
	
	/*
	 * 终端心跳，电源状态，双工状态信息查询
	 * @param token: 终端token
	 * @param moniterId: 终端编号
	 **/
	public MoniterPojo getBeatHeartInfo(String token, String moniterId) {
		String retCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		MoniterPojo moniter = new MoniterPojo();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("token:"+token);
			byte[] queryParamsValidate = new byte[32];
			System.arraycopy(strToByteArray(CMD_UICC),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, queryParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, queryParamsValidate, 4+token.length(), 4);

			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024];
			int len = is.read(dataBytes);
			System.out.println("query beatHeart receive data len:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);
				
			moniter.setRetCode(retCode);
			//可以查询
			if(Constants.COMMON_CC.equals(retCode)) {
				// 心跳
				moniter.setId(moniterId);
				moniter.setBeatheart(subBytes2Int(4, dataSubStart, dataBytes));
				moniter.setPowerStatus(subBytes2Int(4, dataSubStart, dataBytes));
				moniter.setDuplexStatus(subBytes2Int(4, dataSubStart, dataBytes));
				moniter.setVersion(subBytes2Int(4, dataSubStart, dataBytes));
				//电流强度
			    int electronFlow = subBytes2Int(4, dataSubStart, dataBytes);
			    moniter.setElectronFlow(subBytes2String(electronFlow, dataSubStart, dataBytes));
				//抓拍单元电流值
			    int videoElectronFlow = subBytes2Int(4, dataSubStart, dataBytes);
			    moniter.setVideoElectronFlow(subBytes2String(videoElectronFlow, dataSubStart, dataBytes));
				//常亮灯电流值
			    int lampElectronFlow = subBytes2Int(4, dataSubStart, dataBytes);
			    moniter.setLampElectronFlow(subBytes2String(lampElectronFlow, dataSubStart, dataBytes));
				//爆闪频次
			    int flashLampTimes = subBytes2Int(4, dataSubStart, dataBytes);
			    moniter.setFlashLampTimes(subBytes2String(flashLampTimes, dataSubStart, dataBytes));
				//脉冲信号
			    int lampSignal = subBytes2Int(4, dataSubStart, dataBytes);
			    moniter.setLampSignal(subBytes2String(lampSignal, dataSubStart, dataBytes));			
				
				

				System.out.println(moniter.toControlString());
			} else {
				System.out.println("查询终端信息失败");
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

	/*
	 * 设备信息查询
	 * @param token: 终端token
	 * @param moniterId: 终端编号
	 * 
	 * @return List<DevicePojo> 设备列表
	 **/
	public List<DevicePojo> getDeviceList(String token, String moniterId) {
		//token = scanner.next();
		//moniterId = scanner.next();
		if(StringUtils.isEmpty(moniterId)) {
			moniterId = "999999";
		}
		String retCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		List<DevicePojo> deviceList = null;
		try {
			deviceList = new ArrayList<DevicePojo>();
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("token:"+token);
			byte[] queryParamsValidate = new byte[32];
			System.arraycopy(strToByteArray(CMD_UIML),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, queryParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, queryParamsValidate, 4+token.length(), 4);

			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024];
			int len = is.read(dataBytes);
			System.out.println("query device list receive data len:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);

			//可以查询
			if(Constants.COMMON_CC.equals(retCode)) {
				//设备数量
				int deviceNum = subBytes2Int(4, dataSubStart, dataBytes);
				for(int num=0; num < deviceNum; num++) {
					DevicePojo device = new DevicePojo();
					int ip = subBytes2Int(4, dataSubStart, dataBytes);
					device.setIp(subBytes2String(ip, dataSubStart, dataBytes));

					//端口
					int port = subBytes2Int(4, dataSubStart, dataBytes);
					device.setPort(port);
					
					//最后刷新时间
					int updateTime = subBytes2Int(4, dataSubStart, dataBytes);
					device.setUpdatedTime(subBytes2String(updateTime, dataSubStart, dataBytes));
					
					//路口信息
					int road = subBytes2Int(4, dataSubStart, dataBytes);
					device.setRoad(subBytes2String(road, dataSubStart, dataBytes));
					
					//设备类型，品牌，型号
					int type = subBytes2Int(4, dataSubStart, dataBytes);
					int brand = subBytes2Int(4, dataSubStart, dataBytes);
					int model = subBytes2Int(4, dataSubStart, dataBytes);
					device.setDeviceType(type);
					device.setDeviceBrand(brand);
					device.setDeviceModel(model);
					device.setDeviceBrandDesc(env.getProperty("device.type."+type));
					device.setDeviceTypeDesc(env.getProperty("device.brand."+brand));
					device.setDeviceModelDesc(env.getProperty("device.model."+model));

					System.out.println(device.toString());
					deviceList.add(device);
				}

			} else {
				System.out.println("查询设备信息失败 retCode:"+retCode);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		return deviceList;
	}

	/*
	 * 设备信息查询
	 * @param token: 终端token
	 * @param moniterId: 终端编号
	 * 
	 * @return DevicePojo 设备详情
	 **/
	public DevicePojo getDeviceInfo(String token, String moniterId, String queryIp) {
		//token = scanner.next();
		//moniterId = scanner.next();
		
		String retCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		DevicePojo device = null;
		try {
			device = new DevicePojo();
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("token:"+token);
			byte[] queryParamsValidate = new byte[64];
			System.arraycopy(strToByteArray(CMD_UIMI),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, queryParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, queryParamsValidate, 4+token.length(), 4);
			System.arraycopy(int2byte(queryIp.length()),
					0, queryParamsValidate, 8+token.length(), 4);
			System.arraycopy(strToByteArray(queryIp),
					0, queryParamsValidate, 12+token.length(), queryIp.length());

			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024];
			int len = is.read(dataBytes);
			System.out.println("query deviceInfo receive data len:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);

			//可以查询
			if(Constants.COMMON_CC.equals(retCode)) {
				int ip = subBytes2Int(4, dataSubStart, dataBytes);
				device.setIp(subBytes2String(ip, dataSubStart, dataBytes));
				
				device.setPort(subBytes2Int(4, dataSubStart, dataBytes));

				int updateTime = subBytes2Int(4, dataSubStart, dataBytes);
				device.setUpdatedTime(subBytes2String(updateTime, dataSubStart, dataBytes));

				int road = subBytes2Int(4, dataSubStart, dataBytes);
				device.setRoad(subBytes2String(road, dataSubStart, dataBytes));
				
				//设备类型，品牌，型号
				device.setDeviceType(subBytes2Int(4, dataSubStart, dataBytes));
				device.setDeviceBrand(subBytes2Int(4, dataSubStart, dataBytes));
				device.setDeviceModel(subBytes2Int(4, dataSubStart, dataBytes));

				System.out.println(device.toString());
			} else {
				System.out.println("查询设备信息失败 retCode:"+retCode);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		return device;
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

	/*
	 * 终端新增
	 * @param token:用户token
	 * @param moniter: 参数对象
	 **/
	public String add(String token, MoniterPojo moniter) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("add moniter token:"+token);
			byte[] addParamsVal = new byte[4+token.length()];
			System.arraycopy(strToByteArray(CMD_UITR),
					0, addParamsVal, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, addParamsVal, 4, token.length());

			ps.write(addParamsVal);
			ps.flush();

			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			int len = is.read(dataBytes);
			System.out.println("add moniter validate receive byste length:"+len);
			resCode = new String(dataBytes);
			//可以创建
			if(Constants.COMMON_ERROR_CR.equals(resCode)) {
				int byteLen = 0;
				byte[] addParams = new byte[512];
				System.arraycopy(strToByteArray(CMD_TR),
						0, addParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, addParams, 2, token.length());
				String id = moniter.getId();
				String imei = moniter.getImei();
				String desc = moniter.getDesc();
				String provinceCode = moniter.getProvinceCode();
				String countryCode = moniter.getCountryCode();
				String cityCode = moniter.getCityCode();
				String townCode = moniter.getTownCode();
				if(StringUtils.isEmpty(townCode)) {
					townCode = countryCode;//乡镇不存在，保存区县信息
				}
				String lon = moniter.getLon();
				String lat = moniter.getLat();
				
				byteLen += 2 + token.length();
				//终端编号
				System.arraycopy(int2byte(Integer.parseInt(id)), 0, addParams, byteLen, 4);
				byteLen += 4;
				//IMEI
				System.arraycopy(strToByteArray(imei), 0, addParams, byteLen, 15);
				byteLen += 15;
				//终端描述
				byteLen = copyString2Array(addParams, byteLen, desc);
				//乡镇编号
				byteLen = copyString2Array(addParams, byteLen, provinceCode);
				byteLen = copyString2Array(addParams, byteLen, cityCode);
				byteLen = copyString2Array(addParams, byteLen, countryCode);
				byteLen = copyString2Array(addParams, byteLen, townCode);
				
				//经度，纬度
				byteLen = copyString2Array(addParams, byteLen, lon);
				byteLen = copyString2Array(addParams, byteLen, lat);
				
				ps.write(addParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[2];
				len = is.read(addBytes);
				System.out.println("add moniter receive byte length:"+len);
				resCode = new String(addBytes);
				if(Constants.ADD_MONITER_ME.equals(resCode)) {
					System.out.println("moniter id existed!");
				} else if(Constants.ADD_MONITER_ET.equals(resCode)) {
					System.out.println("IMEI existed!");
				} else if(Constants.ADD_MONITER_RF.equals(resCode)) {
					System.out.println("moniter add fail!");
				} else if(Constants.ADD_MONITER_RS.equals(resCode)) {
					System.out.println("moniter add success!");
				}
			} else if(Constants.COMMON_ERROR_IE.equals(resCode)) {
				System.out.println("moniter add has a error");
			} else if(Constants.ADD_PERSON_TT.equals(resCode)) {
				System.out.println("moniter add token time out");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		return resCode;
	}

	/*
	 * 关注列表新增
	 * @param token:用户token
	 * @param moniter: 参数对象
	 **/
	public String addAttentionMoniter(String token, AttentionMoniter attMoniter) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("add moniter token:"+token);
			byte[] addParamsVal = new byte[1024];
			System.arraycopy(strToByteArray(CMD_UIAC),
					0, addParamsVal, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, addParamsVal, 4, token.length());
			int byteLen = 4+token.length();
			//终端数量
			attMoniter.setNum(attMoniter.getIds().split(",").length);
			System.arraycopy(int2byte(attMoniter.getNum()), 0, addParamsVal, byteLen, 4);
			byteLen += 4;
			//关注用户名
			byteLen = copyString2Array(addParamsVal, byteLen, attMoniter.getUserId());
			//关注类型
			byteLen = copyString2Array(addParamsVal, byteLen, attMoniter.getAttentionType());
			//终端编号
			for(String id : attMoniter.getIds().split(",")) {
				//终端数量
				System.arraycopy(int2byte(Integer.parseInt(id)), 0, addParamsVal, byteLen, 4);
				byteLen += 4;
			}

			ps.write(addParamsVal);
			ps.flush();

			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			int len = is.read(dataBytes);
			System.out.println("addAttentionMoniter receive byste length:"+len);
			resCode = new String(dataBytes);
			//可以创建
			if(Constants.COMMON_AF.equals(resCode)) {
				System.out.println("关注列表添加失败");
			} else if(Constants.COMMON_AS.equals(resCode)) {
				System.out.println("关注列表添加成功");
			} else if(Constants.COMMON_ERROR_IE.equals(resCode)) {
				System.out.println("moniter add has a error");
			} else if(Constants.ADD_PERSON_TT.equals(resCode)) {
				System.out.println("moniter add token time out");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		return resCode;
	}
	
	/*
	 * @param des:参数数组
	 * @param byteCount:参数数组开始位置计数器
	 * @param paramStr:参数
	 * 
	 * @return int 参数数组开始位置计数器
	 * 
	 * */
	private int copyString2Array(byte[] des, int byteCount, String paramStr) {
		if(null == paramStr) {
			paramStr = "";
		}
		byte[] paramBytes = strToByteArray(paramStr);
		System.arraycopy(int2byte(paramBytes.length), 0, des, byteCount, 4);
		byteCount += 4;
		System.arraycopy(paramBytes, 0, des, byteCount, paramBytes.length);
		byteCount += paramBytes.length;
		
		return byteCount;
	}

	/*
	 * 创建socket连接，解析数据结构体
	 * @param token:用户token
	 **/
	public SktResponse getMoniterPojoList(String token) {
		//token = scanner.next();
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
			int numReq = 80;
			int dataReqNum = 80;
			//第一次请求
			pojoList = getMoniterDatas(token, 1, dataReqNum, s, pw, br, is, response);
			total = response.getRetTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getMoniterDatas(token, cntReq, dataReqNum, s, pw, br, is, response));
			}
			response.setDatas(pojoList);
			System.out.println("运维设备总数量："+response.getDatas().size());
			
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

	/*
	 * 2.3.6获取终端故障列表
	 * @param token:用户token
	 * @param id:终端ID
	 * @param desc:终端描述
	 * @param townCode:乡镇编码
	 * 
	 * @return 终端故障列表
	 **/
	public SktResponse getBreakDownMoniterPojoList(String token, String id, String warnType) {
		if(null == id || "".equals(id)) {
			id = "999999";
		}
		if(null == warnType || "".equals(warnType)) {
			warnType = "999999";
		}
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		SktResponse response = new SktResponse();
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			is = s.getInputStream();
			int total = 0; 
			int numReq = 10;
			int dataReqNum = 10;

			//第一次请求
			pojoList = getBreakDownDatas(token, id, warnType, 1, dataReqNum, s, ps, is, response);
			total = response.getRetTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getBreakDownDatas(token, id, warnType, cntReq, dataReqNum, s, ps, is, response));
			}
			
			//告警原因转换
			response.setDatas(pojoList);
			System.out.println("告警终端数量："+response.getDatas().size());
			
			//关闭
			s.close();
			ps.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		
		return response;
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
					warnDesc = warnTypes.get("1");
					pojo.setWarnDesc(warnDesc);
					System.out.println("终端告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
					continue;
				}
				//终端心跳
				if(pojo.getWarn2() == 1) {
					warnDesc = "终端离线";
					pojo.setWarnDesc(warnDesc);
					System.out.println("终端告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
					continue;
				}
				//前端设备电流异常
				warnDesc = pojo.getDesc();
				List<DevicePojo> dList = pojo.getDeviceList();
				if(null != dList) {
					//过滤终端下面报警的设备
					for(int i=0; i<dList.size(); i++) {
						DevicePojo device = dList.get(i);
						if(i != 0) {
							warnDesc += "";
						}
						if(device.getPort1() == 0) {
							warnDesc += device.getDesc();
							warnDesc += ", 电源口"+device.getPort()+"异常("+device.getIp()+")";
							continue;
						}
						if(device.getPort2() == 0) {
							warnDesc += device.getDesc();
							warnDesc += ", 网口"+device.getPort()+"异常("+device.getIp()+")";
							continue;
						}
						if(device.getPort3() == 0) {
							warnDesc += device.getDesc();
							warnDesc += ", 端口"+device.getPort()+"会话异常("+device.getIp()+")";
							continue;
						}
					}
				}
				//开箱状态
				if(pojo.getWarn6() == 1) {
					warnDesc += ", 箱门开启";
				}
				if(",".equals(warnDesc.substring(0, 1))) {
					warnDesc = warnDesc.substring(1);
				}
				pojo.setWarnDesc(warnDesc);
				System.out.println("设备告警信息"+pojo.getId()+":"+pojo.getWarnDesc());
				continue;
			}
		}
		
	}

	/*
	 * 获取关注列表所有终端编号
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 * @param attentionType: 关注类型
	 **/
	public SktResponse getMoniterAttentionList(String token, String moniterId, String attentionType) {
		//token = scanner.next();
		PrintStream ps = null;
		BufferedReader br = null;
		Socket s = null;
		SktResponse response = new SktResponse();
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			InputStream is = s.getInputStream();

			System.out.println("Client input cmd:");
			byte[] attentionIdsQuery = new byte[1024];
			System.arraycopy(strToByteArray(CMD_UICO),
					0, attentionIdsQuery, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, attentionIdsQuery, 4, token.length());
			if(StringUtils.isEmpty(moniterId)) {
				moniterId = "999999";
			}
			if(StringUtils.isEmpty(attentionType)) {
				attentionType = "";
			}
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, attentionIdsQuery, 4+token.length(), 4);
			System.arraycopy(int2byte(attentionType.length()),
					0, attentionIdsQuery, 8+token.length(), 4);
			System.arraycopy(strToByteArray(attentionType),
					0, attentionIdsQuery, 12+token.length(), attentionType.length());

			ps.write(attentionIdsQuery);
			ps.flush();

			int total = 0; 
			int numReq = 80;
			int dataReqNum = 80;
			//第一次请求
			pojoList = getMoniterAttentionDatas(token, 1, dataReqNum, s, ps, br, is, response);
			total = response.getRetTotal();

			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getMoniterAttentionDatas(token, cntReq, dataReqNum, s, ps, br, is, response));
			}
			response.setDatas(pojoList);
			System.out.println("关注终端总数量："+response.getDatas().size());
			
			//关闭
			s.close();
			ps.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, null);
		}
		
		return response;
	}

	/**
	 * 解析运维终端列表数据 
	 */
	public List<MoniterPojo> getMoniterDatas(String token, int idxReq,int dataReqNum, Socket s
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
			System.out.println("Client input cmd:");
			String str = QUERY_START_FLAG;
			str += token;
			pw.print((str));
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
			// IMEI
			moniter.setImei(subBytes2String(15, dataSubStart, dataBytes));
			// 终端描述
			int desc = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setDesc(subBytes2String(desc, dataSubStart, dataBytes));
			// 信号强度
			moniter.setSignal(subBytes2Int(4, dataSubStart, dataBytes));
			// 省
			int province = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setProvinceCode(subBytes2String(province, dataSubStart, dataBytes));
			// 市
			int city = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCityCode(subBytes2String(city, dataSubStart, dataBytes));
			// 县
			int country = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCountryCode(subBytes2String(country, dataSubStart, dataBytes));
			// 乡镇编号
			int townCode = subBytes2Int(4, dataSubStart, dataBytes);
			if(townCode>0) {
				moniter.setTownName(subBytes2String(townCode, dataSubStart, dataBytes));
			}
			// 经度
			int lon = subBytes2Int(4, dataSubStart, dataBytes);
			if(lon>0) {
				moniter.setLon(subBytes2Substring(lon, dataSubStart, dataBytes));
			}
			// 纬度
			int lat = subBytes2Int(4, dataSubStart, dataBytes);
			if(lat>0) {
				moniter.setLat(subBytes2String(lat, dataSubStart, dataBytes));
			}

			System.out.println(moniter.toString());
			pojoList.add(moniter);
		}

		//最后一次不发送结束指令
		pw.print((QUERY_END_FLAG));
		pw.flush();
		return pojoList;
	}


	/**
	 * 解析关注终端列表数据 
	 */
	public List<MoniterPojo> getMoniterAttentionDatas(String token, int idxReq,int dataReqNum, Socket s
			,PrintStream ps, BufferedReader br
			,InputStream is, SktResponse response) 
			throws UnknownHostException, IOException {
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		if(null != response.getDatas() && response.getDatas().size() > 0) {
			pojoList = response.getDatas();
		}
		String retCode;
		int total = 0;
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
			// IMEI
			moniter.setImei(subBytes2String(15, dataSubStart, dataBytes));
			// 终端描述
			int desc = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setDesc(subBytes2String(desc, dataSubStart, dataBytes));
			// 信号强度
			moniter.setSignal(subBytes2Int(4, dataSubStart, dataBytes));
			// 省
			int province = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setProvinceCode(subBytes2String(province, dataSubStart, dataBytes));
			// 市
			int city = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCityCode(subBytes2String(city, dataSubStart, dataBytes));
			// 县
			int country = subBytes2Int(4, dataSubStart, dataBytes);
			moniter.setCountryCode(subBytes2String(country, dataSubStart, dataBytes));
			// 乡镇编号
			int townCode = subBytes2Int(4, dataSubStart, dataBytes);
			if(townCode>0) {
				moniter.setTownName(subBytes2String(townCode, dataSubStart, dataBytes));
			}
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
			// 关注类型
			int attentionType = subBytes2Int(4, dataSubStart, dataBytes);
			if(attentionType > 0) {
				moniter.setAttentionType(subBytes2String(attentionType, dataSubStart, dataBytes));
			}

			System.out.println(moniter.toString());
			pojoList.add(moniter);
		}

		//最后一次不发送结束指令
		ps.write(strToByteArray(QUERY_END_FLAG));
		ps.flush();
		return pojoList;
	}
	
	/* *
	 * 获取终端故障列表
	 * @param moniterId 终端ID 
	 * @param warnType 告警类型
	 * */
	public List<MoniterPojo> getBreakDownDatas(String token, 
			String moniterId, String warnType, int idxReq,int dataReqNum, Socket s,
			PrintStream ps, InputStream is, SktResponse response) 
			throws UnknownHostException, IOException {
		List<MoniterPojo> pojoList = new ArrayList<MoniterPojo>();
		if(null != response.getDatas() && response.getDatas().size() > 0) {
			pojoList = response.getDatas();
		}
		
		String retCode;
		int total = 0;
		//第一次请求发送指令
		if(idxReq == 1) {
			byte[] breakDownParams = new byte[1024];
			System.arraycopy(strToByteArray(CMD_UITA),
					0, breakDownParams, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, breakDownParams, 4, token.length());
			if(null == moniterId || "".equals(moniterId)) {
				moniterId = "999999";
			}
			if(null == warnType || "".equals(warnType)) {
				warnType = "999999";
			}
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, breakDownParams, 4+token.length(), 4);
			System.arraycopy(int2byte(Integer.parseInt(warnType)),
					0, breakDownParams, 8+token.length(), 4);

			ps.write(breakDownParams);
			ps.flush();
		}

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

				//设备描述
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

		//告警描述信息处理
		convertWarnDesc(pojoList);
		
		//最后一次不发送结束指令
		ps.write(strToByteArray(CMD_TAFI));
		ps.flush();
		return pojoList;
	}
	
	/*
	 * 终端删除
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 **/
	public String deleteByToken(String token, String moniterId) {
		//token = scanner.next();
		//moniterId = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("delete moniter token:"+token);
			byte[] deleteParamsValidate = new byte[8+token.length()];
			System.arraycopy(strToByteArray(CMD_UITD),
					0, deleteParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, deleteParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, deleteParamsValidate, 4+token.length(), 4);
			ps.write(deleteParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[8];
			int len = is.read(dataBytes);
			System.out.println("delete moniter validate receive byste length:"+len);
			byte[] retBytes = new byte[2];
			System.arraycopy(dataBytes, 0, retBytes, 0, 2);
			resCode = new String(retBytes);
			//可以删除
			if(Constants.DELETE_MONITER_SS.equals(resCode)) {
				//短信验证码处理后期删除
				byte[] msgCode = new byte[6];
				System.arraycopy(dataBytes, 2, msgCode, 0, 6);
				String msg = resCode = new String(msgCode);
				
				byte[] deleteParams = new byte[32];
				System.arraycopy(strToByteArray(CMD_TD),
						0, deleteParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, deleteParams, 2, Constants.TOKEN_LEN);
				System.arraycopy(strToByteArray(msg),
						0, deleteParams, 2+Constants.TOKEN_LEN, msg.length());
				
				ps.write(deleteParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[2];
				len = is.read(addBytes);
				resCode = new String(addBytes);
			}
			//关闭
			s.close();
			ps.close();
			is.close();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		System.out.println("delete return code:"+resCode);
		return resCode;
	}

	/*
	 * 终端修改
	 * @param token:用户token
	 * @param person:参数对象
	 **/
	public String editMoniterByToken(String token, MoniterPojo moniter) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("edit moniter token:"+token);
			byte[] editParamsValidate = new byte[32];
			System.arraycopy(strToByteArray(CMD_UITM),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniter.getId())),
					0, editParamsValidate, 4+token.length(), 4);
			ps.write(editParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);
			System.out.println("edit person validate retCode:"+resCode);
			//可以修改
			if(Constants.COMMON_CM.equals(resCode)) {
				int byteLen = 0;
				byte[] editMoniterParams = new byte[512];
				System.arraycopy(strToByteArray(Constants.EDIT_MONITER_TM),
						0, editMoniterParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, editMoniterParams, 2, token.length());
				
				byteLen += 2 + token.length();
				//终端描述
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getDesc());
				//省市县乡
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getProvinceCode());
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getCityCode());
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getCountryCode());
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getTownCode());
				//经度
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getLon());
				//纬度
				byteLen = copyString2Array(editMoniterParams, byteLen, moniter.getLat());

				ps.write(editMoniterParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[2];
				is.read(addBytes);
				resCode = new String(addBytes);
			}
			//关闭
			s.close();
			ps.close();
			is.close();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		System.out.println("edit moniter retCode:"+resCode);
		return resCode;
	}
	
	/*
	 * 控制台信息修改：心跳，电源，双工
	 * @param token:用户token
	 * @param moniter:参数对象
	 **/
	public String editBeatHeartInfoByToken(String token, String id, String heart, String power) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("editBeatHeartInfoByToken token:"+token);
			byte[] editParamsValidate = new byte[40];
			System.arraycopy(strToByteArray(CMD_UISE),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(id)),
					0, editParamsValidate, 4+token.length(), 4);
			System.arraycopy(int2byte(Integer.parseInt(heart)),
					0, editParamsValidate, 8+token.length(), 4);
			System.arraycopy(int2byte(HexUtil.binaryToDecimal(power)),
					0, editParamsValidate, 12+token.length(), 4);
			ps.write(editParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);

			//关闭
			s.close();
			ps.close();
			is.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		System.out.println("edit beatHeart retCode:"+resCode);
		return resCode;
	}

	/*
	 * 设备修改
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 * @param person:参数对象
	 **/
	public String editDeviceByToken(String token, String moniterId, DevicePojo device) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("edit moniter token:"+token);
			byte[] editParamsValidate = new byte[64];
			String ip = device.getIp();
			System.arraycopy(strToByteArray(CMD_UIMM),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, editParamsValidate, 4+token.length(), 4);
			System.arraycopy(int2byte(ip.length()),
					0, editParamsValidate, 8+token.length(), 4);
			System.arraycopy(strToByteArray(ip),
					0, editParamsValidate, 12+token.length(), ip.length());
			ps.write(editParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);
			System.out.println("edit person validate retCode:"+resCode);
			//可以修改
			if(Constants.COMMON_CM.equals(resCode)) {
				int byteLen = 0;
				byte[] editMoniterParams = new byte[512];
				System.arraycopy(strToByteArray(Constants.EDIT_DEVICE_MM),
						0, editMoniterParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, editMoniterParams, 2, token.length());
				
				byteLen += 2 + token.length();
				//终端描述
				byteLen = copyString2Array(editMoniterParams, byteLen, device.getRoad());
				//设备类型
				System.arraycopy(int2byte(device.getDeviceType()), 0, editMoniterParams, byteLen, 4);
				byteLen += 4;
				//设备类型
				System.arraycopy(int2byte(device.getDeviceBrand()), 0, editMoniterParams, byteLen, 4);
				byteLen += 4;
				//设备类型
				System.arraycopy(int2byte(device.getDeviceModel()), 0, editMoniterParams, byteLen, 4);
				byteLen += 4;

				ps.write(editMoniterParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[2];
				is.read(addBytes);
				resCode = new String(addBytes);
			}
			//关闭
			s.close();
			ps.close();
			is.close();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		System.out.println("edit moniter retCode:"+resCode);
		return resCode;
	}

	/*
	 * 设备重启
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 * @param person:参数对象
	 **/
	public String deviceRestart(String token, String moniterId, String ip) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("device restart token:"+token);
			byte[] editParamsValidate = new byte[64];
			System.arraycopy(strToByteArray(CMD_UIMR),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, editParamsValidate, 4+token.length(), 4);
			System.arraycopy(int2byte(ip.length()),
					0, editParamsValidate, 8+token.length(), 4);
			System.arraycopy(strToByteArray(ip),
					0, editParamsValidate, 12+token.length(), ip.length());
			ps.write(editParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);
			System.out.println("device restart:"+resCode);
			//关闭
			s.close();
			ps.close();
			is.close();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		return resCode;
	}

	/*
	 * 创建socket连接，解析数据结构体
	 * @param token:用户token
	 **/
	public List<LogInfo> getMoniterLogList(String token, String moniterId, String interval) {
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		SktResponse response = new SktResponse();
		List<LogInfo> pojoList = new ArrayList<LogInfo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			is = s.getInputStream();

			int total = 0; 
			int numReq = 4090;
			int dataReqNum = 4090;
			//第一次请求
			String jsonStr = getMoniterLogDatas(token, 1, dataReqNum, s, ps, is, response, moniterId, interval);
			total = response.getRetTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				if(null == jsonStr) {
					jsonStr = "";
				}
				jsonStr += getMoniterLogDatas(token, cntReq, dataReqNum, s, ps, is, response, moniterId, interval);
			}
			if(!StringUtils.isEmpty(jsonStr)) {
				pojoList = JSON.parseArray(jsonStr,LogInfo.class);
			}
			System.out.println("终端运行日志行数："+pojoList.size());
			
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
		
		return pojoList;
	}

	/**
	 * 解析运维终端日志列表数据 
	 */
	public String getMoniterLogDatas(String token, int idxReq,int dataReqNum, Socket s
			,PrintStream ps,InputStream is, SktResponse response, String moniterId, String interval) 
			throws UnknownHostException, IOException {
		List<LogInfo> pojoList = new ArrayList<LogInfo>();
		if(null != response.getDatas() && response.getDatas().size() > 0) {
			pojoList = response.getDatas();
		}
		String retCode;
		//第一次请求发送指令
		if(idxReq == 1) {
			//第一次请求参数设置
			if(StringUtils.isEmpty(interval)) {
				interval = "7";
			}
			interval = "-"+interval;
			String endTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");//String startTime = "20180918 15:12:40";
			int days = Integer.parseInt(interval);
			Date end = DateUtil.getDateAfter(DateUtil.parseDateTime(endTime), days);
			String startTime = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");//String endTime = "20180919 15:13:50";
			endTime = endTime.replaceAll("-", "");
			startTime = startTime.replaceAll("-", "");
			byte[] editParamsValidate = new byte[66];
			System.arraycopy(strToByteArray(CMD_UIGL),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, editParamsValidate, 4+token.length(), 4);
			System.arraycopy(strToByteArray(startTime),
					0, editParamsValidate, 8+token.length(), 17);
			System.arraycopy(strToByteArray(endTime),
					0, editParamsValidate, 25+token.length(), 17);
			ps.write(editParamsValidate);
			ps.flush();
		}
		byte[] dataBytes = new byte[4096];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		//解析数据头
		int len = is.read(dataBytes);
		System.out.println("server len idxReq【"+idxReq+"】::" + len);
		retCode = subBytes2String(2, dataSubStart, dataBytes);
		response.setRetCode(retCode);
		//json字符长度
		int jsonLen = subBytes2Int(4, dataSubStart, dataBytes);
		response.setRetTotal(jsonLen);
		System.out.println("JSON字符串长度 :"+jsonLen);
		//第一次默认根据阈值查询，如果库中数据个数小于阈值，则更新阈值
		if(dataReqNum > jsonLen) {
			dataReqNum = jsonLen;
		}

		System.out.println("截取字符串长度 :"+dataReqNum);
		// JSON字符串
		String jsonStr = subBytes2String(dataReqNum, dataSubStart, dataBytes);

		//最后一次不发送结束指令
		ps.write(strToByteArray(QUERY_END_GLFI));
		ps.flush();
		return jsonStr;
	}

	
	/*
	 * 获取日志
	 * @param token:用户token
	 * @param moniterId: 终端编号
	 * @param interval:1->最近七天 2->最近一个月 3->最近三个月 4->最近半年
	 **/
	public List<LogInfo> getMoniterlogs(String token, String moniterId, String interval) {
		//token = scanner.next();
		String resCode = "";
		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		if(StringUtils.isEmpty(interval)) {
			interval = "7";
		}
		List<LogInfo> logList = new ArrayList<LogInfo>();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("获取终端运行日志token:"+token);
			byte[] editParamsValidate = new byte[66];
			String startTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");//String startTime = "20180918 15:12:40";
			int days = Integer.parseInt(interval);
			Date end = DateUtil.getDateAfter(DateUtil.parseDateTime(startTime), days);
			String endTime = DateUtil.format(end, "yyyy-MM-dd HH:mm:ss");//String endTime = "20180919 15:13:50";
			System.arraycopy(strToByteArray(CMD_UIGL),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			System.arraycopy(int2byte(Integer.parseInt(moniterId)),
					0, editParamsValidate, 4+token.length(), 4);
			System.arraycopy(strToByteArray(startTime),
					0, editParamsValidate, 8+token.length(), 17);
			System.arraycopy(strToByteArray(endTime),
					0, editParamsValidate, 25+token.length(), 17);
			ps.write(editParamsValidate);
			ps.flush();
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024*1024];
			int len = is.read(dataBytes);
			System.out.println("getMoniterlogs:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			String retCode = subBytes2String(2, dataSubStart, dataBytes);

			//第一次请求获取成功
			if(Constants.COMMON_GS.equals(retCode)) {
				// JSON字符串长度
				int jsonLen = subBytes2Int(4, dataSubStart, dataBytes);
				System.out.println("JSON字符串长度 :"+jsonLen);
				// JSON字符串
				String jsonStr = subBytes2String(jsonLen, dataSubStart, dataBytes);
				logList = JSON.parseArray(jsonStr,LogInfo.class);
			} else {
				System.out.println("获取终端运行日志失败:"+retCode);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭
			close(s, ps, is);
		}
		System.out.println("edit moniter retCode:"+resCode);
		return logList;
	}
}
