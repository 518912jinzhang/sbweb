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
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sbweb.common.AbstractService;
import com.sbweb.common.Constants;
import com.sbweb.common.StringLogUtil;
import com.sbweb.ui.pojo.PersonPojo;

/**
 * 人员管理
 * 
 */
@Service
@PropertySource("classpath:system.properties")
public class PersonService extends AbstractService{
	private static String CMD_UIAD = "UIAD";//人员新增验证
	private static String CMD_CUFI = "CUFI";//人员新增验证
	private static String CMD_UIDL = "UIDL";//人员删除验证
	private static String CMD_UIMP = "UIMP";//修改密码
	private static String CMD_UIMC = "UIMC";//修改密码
	private static String CMD_UIMF = "UIMF";//人员删除验证
	private static String CMD_AD = "AD";//人员新增
	private static String CMD_DL = "DL";//人员删除
	private static String CMD_MP = "MP";//人员密码修改
	private static String CMD_MF = "MF";//人员修改
	private static String CMD_START_FLAG = "UICK";
	private static String CMD_END_FLAG = "CKFI";
	private static String CMD_UICU = "UICU";//用户个人信息查询
	
	private static Map<String, String> personTypeMap = new HashMap<String, String>();
	static {
		personTypeMap.put("0", "超级管理员用户");
		personTypeMap.put("1", "管理员用户");
		personTypeMap.put("2", "普通用户");
		personTypeMap.put("3", "甲方");
		personTypeMap.put("4", "乙方");
		personTypeMap.put("5", "设备供应商");
		personTypeMap.put("6", "运维人员");
		personTypeMap.put("7", "集成公司");
		personTypeMap.put("8", "电力公司");
		personTypeMap.put("9", "运营商");
		personTypeMap.put("10", "用户单位");
	}
	
	@Autowired
    private Environment env;
	
	/*
	 * 用户个人信息查询
	 * @param token:用户token
	 * @param person:参数对象
	 **/
	public PersonPojo getPersonInfo(String token, String userId) {
		String retCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		PersonPojo person = new PersonPojo();
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("query person info token:"+token);
			byte[] queryParamsValidate = new byte[128];
			System.arraycopy(strToByteArray(CMD_UICU),
					0, queryParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, queryParamsValidate, 4, token.length());
			System.arraycopy(int2byte(userId.length()),
					0, queryParamsValidate, 4+token.length(), 4);
			System.arraycopy(strToByteArray(userId),
					0, queryParamsValidate, 8+token.length(), userId.length());

			ps.write(queryParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[1024];
			int len = is.read(dataBytes);
			System.out.println("getPersonInfo validate receive byste length:"+len);
			AtomicInteger dataSubStart = new AtomicInteger(0);
			retCode = subBytes2String(2, dataSubStart, dataBytes);
				
			person.setRetCode(retCode);
			//可以查询
			if(Constants.QUERY_PERSON_CK.equals(retCode)) {
				// 用户名
				int id = subBytes2Int(4, dataSubStart, dataBytes);
				person.setId(subBytes2String(id, dataSubStart, dataBytes));
				// 密码
				int pwd = subBytes2Int(4, dataSubStart, dataBytes);
				person.setPwd(subBytes2String(pwd, dataSubStart, dataBytes));
				// 手机号
				int tel = subBytes2Int(4, dataSubStart, dataBytes);
				person.setTelphone(subBytes2String(tel, dataSubStart, dataBytes));
				// 角色
				person.setRole(subBytes2Int(4, dataSubStart, dataBytes));
				person.setRoleDesc(personTypeMap.get(""+person.getRole()));
				// 姓名
				int name = subBytes2Int(4, dataSubStart, dataBytes);
				person.setName(subBytes2Substring(name, dataSubStart, dataBytes));
				// 性别
				int sex = subBytes2Int(4, dataSubStart, dataBytes);
				person.setSex(subBytes2String(sex, dataSubStart, dataBytes));
				// 职务
				int job = subBytes2Int(4, dataSubStart, dataBytes);
				person.setJob(subBytes2String(job, dataSubStart, dataBytes));
				// 出生年月
				int bir = subBytes2Int(4, dataSubStart, dataBytes);
				person.setBirthday(subBytes2String(bir, dataSubStart, dataBytes));
				// 邮箱
				int email = subBytes2Int(4, dataSubStart, dataBytes);
				person.setEmail(subBytes2String(email, dataSubStart, dataBytes));
				// 办公地址
				int wa = subBytes2Int(4, dataSubStart, dataBytes);
				person.setWorkAddress(subBytes2String(wa, dataSubStart, dataBytes));
				// 备用手机
				int bp = subBytes2Int(4, dataSubStart, dataBytes);
				person.setBakPhone(subBytes2String(bp, dataSubStart, dataBytes));
				// 办公电话
				int wp = subBytes2Int(4, dataSubStart, dataBytes);
				person.setWorkPhone(subBytes2String(wp, dataSubStart, dataBytes));
				// 所在单位
				int co = subBytes2Int(4, dataSubStart, dataBytes);
				person.setCompany(subBytes2String(co, dataSubStart, dataBytes));

				person.format();
				System.out.println(person.toString());
			} else {
				System.out.println("查询用户个人信息失败");
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
		return person;
	}

	/*
	 * 用户新增
	 * @param token:用户token
	 * @param person:参数对象
	 **/
	public String add(String token, PersonPojo person) {
		String resCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("add person token:"+token);
			byte[] addParamsValidate = new byte[4+token.length()];
			System.arraycopy(strToByteArray(CMD_UIAD),
					0, addParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, addParamsValidate, 4, token.length());

			ps.write(addParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			int len = is.read(dataBytes);
			System.out.println("add person validate receive byste length:"+len);
			resCode = new String(dataBytes);
			//可以创建
			if(Constants.ADD_PERSON_CC.equals(resCode)) {
				int byteLen = 0;
				byte[] addParams = new byte[512];
				System.arraycopy(strToByteArray(CMD_AD),
						0, addParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, addParams, 2, token.length());
				String id = "US"+person.getId();
				String pwd = "PA"+person.getPwd();
				String tel = "PH"+person.getTelphone();
				String name = "NA"+person.getName();
				String sex = "SE"+person.getSex();
				String job = "JO"+person.getJob();
				String birthday = "BD"+person.getBirthday();
				String email = "MB"+person.getEmail();
				String workAddress = "OA"+person.getWorkAddress();
				String bakPhone = "SP"+person.getBakPhone();
				String workPhone = "OP"+person.getWorkPhone();
				String company = "CO"+person.getCompany();
				
				byteLen += 2 + token.length();
				//用户ID
				byteLen = copyArray(addParams, byteLen, id);
				//密码
				byteLen = copyArray(addParams, byteLen, pwd);
				//手机号
				byteLen = copyArray(addParams, byteLen, tel);
				//角色
				System.arraycopy(int2byte(person.getRole()), 0, addParams, byteLen, 4);
				byteLen += 4;
				//姓名
				byteLen = copyArray(addParams, byteLen, name);
				//性别
				byteLen = copyArray(addParams, byteLen, sex);
				//职务
				byteLen = copyArray(addParams, byteLen, job);
				//出生年月
				byteLen = copyArray(addParams, byteLen, birthday);
				//邮箱
				byteLen = copyArray(addParams, byteLen, email);
				//办公地址
				byteLen = copyArray(addParams, byteLen, workAddress);
				//备用手机
				byteLen = copyArray(addParams, byteLen, bakPhone);
				//办公电话
				byteLen = copyArray(addParams, byteLen, workPhone);
				//所在单位
				byteLen = copyArray(addParams, byteLen, company);

				ps.write(addParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[4];
				len = is.read(addBytes);
				System.out.println("add person receive byte length:"+len);
				resCode = new String(addBytes);
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
		System.out.println("add person resCode:"+resCode);
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
	private int copyArray(byte[] des, int byteCount, String paramStr) {
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
	public List<PersonPojo> getPersonList(String token) {
		PrintWriter pw = null;
		BufferedReader br = null;
		List<PersonPojo> pojoList = new ArrayList<PersonPojo>();
		try {
			Socket s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			InputStream is = s.getInputStream();
			int total = 0; 
			int numReq = 10;
			int dataReqNum = 10;

			//第一次请求
			pojoList = getPersonDatas(token, 1, dataReqNum, s, pw, br, is);
			total = pojoList.get(0).getTotal();
			
			for(int cntReq=2; cntReq<(total/numReq)+2; cntReq++) {
				//最后一次请求数据取余数
				if(cntReq == (total/numReq)+1) {
					dataReqNum = total % numReq;
				}
				pojoList.addAll(getPersonDatas(token, cntReq, dataReqNum, s, pw, br, is));
			}
			System.out.println("总数量："+pojoList.size());
			//关闭
			pw.close();
			br.close();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(null == pojoList) {
			pojoList = new ArrayList<PersonPojo>();
		}
		return pojoList;
	}
	
	/* *
	 * 获取人员列表
	 * */
	private List<PersonPojo> getPersonDatas(String token, int idxReq,int dataReqNum, Socket s
			,PrintWriter pw, BufferedReader br
			,InputStream is) 
			throws UnknownHostException, IOException {
		List<PersonPojo> pojoList = new ArrayList<PersonPojo>();
		String retCode;
		int total = 0;
		//第一次请求发送指令
		if(idxReq == 1) {
			System.out.println("Client input cmd:");
			String str = CMD_START_FLAG;
			str += token;
			pw.print((str));
			pw.flush();
		}
		byte[] dataBytes = new byte[2048*300];
		AtomicInteger dataSubStart = new AtomicInteger(0);
		for(int req = 0; req<dataReqNum; req++) {
			PersonPojo person = new PersonPojo();
			if(req == 0) {
				int len = is.read(dataBytes);
				System.out.println("server len idxReq【"+idxReq+"】::" + len);
				retCode = subBytes2String(2, dataSubStart, dataBytes);

				//该用户可查询人员数量
				byte[] personNum = subBytes(4, dataSubStart, dataBytes);
				total = byteArrayToInt(personNum);
				person.setTotal(total);
				//第一次默认根据阈值查询，如果库中数据个数小于阈值，则更新阈值
				if(dataReqNum>total) {
					dataReqNum = total;
				}
			}
			
			// 用户名
			int id = subBytes2Int(4, dataSubStart, dataBytes);
			person.setId(subBytes2String(id, dataSubStart, dataBytes));
			// 密码
			int pwd = subBytes2Int(4, dataSubStart, dataBytes);
			person.setPwd(subBytes2String(pwd, dataSubStart, dataBytes));
			// 手机号
			int tel = subBytes2Int(4, dataSubStart, dataBytes);
			person.setTelphone(subBytes2String(tel, dataSubStart, dataBytes));
			// 角色
			person.setRole(subBytes2Int(4, dataSubStart, dataBytes));
			person.setRoleDesc(personTypeMap.get(""+person.getRole()));
			// 姓名
			int name = subBytes2Int(4, dataSubStart, dataBytes);
			person.setName(subBytes2Substring(name, dataSubStart, dataBytes));
			// 手机号
			int sex = subBytes2Int(4, dataSubStart, dataBytes);
			person.setSex(subBytes2String(sex, dataSubStart, dataBytes));
			// 手机号
			int job = subBytes2Int(4, dataSubStart, dataBytes);
			person.setJob(subBytes2String(job, dataSubStart, dataBytes));
			// 生日
			int bir = subBytes2Int(4, dataSubStart, dataBytes);
			person.setBirthday(subBytes2String(bir, dataSubStart, dataBytes));
			// 邮箱
			int email = subBytes2Int(4, dataSubStart, dataBytes);
			person.setEmail(subBytes2String(email, dataSubStart, dataBytes));
			// 公司地址
			int wa = subBytes2Int(4, dataSubStart, dataBytes);
			person.setWorkAddress(subBytes2String(wa, dataSubStart, dataBytes));
			// 备用手机号
			int bp = subBytes2Int(4, dataSubStart, dataBytes);
			person.setBakPhone(subBytes2String(bp, dataSubStart, dataBytes));
			// 工作号码
			int wp = subBytes2Int(4, dataSubStart, dataBytes);
			person.setWorkPhone(subBytes2String(wp, dataSubStart, dataBytes));
			// 公司名
			int co = subBytes2Int(4, dataSubStart, dataBytes);
			person.setCompany(subBytes2String(co, dataSubStart, dataBytes));
			// 上级用户ID
			int spid = subBytes2Int(4, dataSubStart, dataBytes);
			person.setSpid(subBytes2String(spid, dataSubStart, dataBytes));

			person.format();
			System.out.println(person.toString());
			pojoList.add(person);
		}

		//最后一次不发送结束指令
		pw.print((CMD_END_FLAG));
		pw.flush();
		return pojoList;
	}

	/*
	 * 修改密码获取验证码
	 * @param token:用户token
	 * @param pwd: 参数对象
	 * @param identifCode: 短信验证码
	 **/	
	public String changePwdValid(String token) {
		String resCode = "";
		String identifCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			byte[] changePasswordParams = new byte[4+token.length()];
			System.arraycopy(strToByteArray(CMD_UIMC),
					0, changePasswordParams, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, changePasswordParams, 4, token.length());
			ps.write(changePasswordParams);
			ps.flush();

			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[8];
			is.read(dataBytes);
			StringLogUtil.log(resCode);
			byte[] ret = new byte[2];
			byte[] identifyCode = new byte[6];
			System.arraycopy(dataBytes, 0, ret, 0, 2);
			System.arraycopy(dataBytes, 2, identifyCode, 0, 6);
			resCode = new String(ret);
			//可以修改密码
			if(Constants.EDIT_PERSON_SS.equals(resCode)) {
				identifCode = new String(identifyCode);
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
		System.out.println("change password return identifCode:"+identifCode);
		return identifCode;
	}
	
	/*
	 * 用户修改密码
	 * @param token:用户token
	 * @param pwd: 参数对象
	 * @param identifCode: 短信验证码
	 **/	
	public String changePassword(String token, String userId, String pwd, String identifCode) {
		String resCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			byte[] changePasswordParams = new byte[128];
			System.arraycopy(strToByteArray(CMD_UIMP),
					0, changePasswordParams, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, changePasswordParams, 4, token.length());
			System.arraycopy(int2byte(userId.length()),
					0, changePasswordParams, 4+Constants.TOKEN_LEN, 4);
			System.arraycopy(strToByteArray(userId),
					0, changePasswordParams, 8+Constants.TOKEN_LEN	, userId.length());
			System.arraycopy(int2byte(pwd.length()),
					0, changePasswordParams, 8+Constants.TOKEN_LEN+userId.length(), 4);
			System.arraycopy(strToByteArray(pwd),
					0, changePasswordParams, 12+Constants.TOKEN_LEN+userId.length(), pwd.length());
			System.arraycopy(strToByteArray(identifCode),
					0, changePasswordParams, 12+Constants.TOKEN_LEN+userId.length()+pwd.length(), identifCode.length());
			ps.write(changePasswordParams);
			ps.flush();

			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);
			StringLogUtil.log(resCode);
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
		System.out.println("change password return code:"+resCode);
		return resCode;
	}

	/*
	 * 用户删除
	 * @param token:用户token
	 * @param person:参数对象
	 **/
	public String deleteByToken(String token, String userId) {
		String resCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("delete person token:"+token);
			byte[] deleteParamsValidate = new byte[4+token.length()];
			System.arraycopy(strToByteArray(CMD_UIDL),
					0, deleteParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, deleteParamsValidate, 4, token.length());
			ps.write(deleteParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			is.read(dataBytes);
			resCode = new String(dataBytes);
			StringLogUtil.log(resCode);
			//可以删除
			if(Constants.DELETE_PERSON_CD.equals(resCode)) {
				int byteLen = 0;
				byte[] deleteParams = new byte[512];
				System.arraycopy(strToByteArray(CMD_DL),
						0, deleteParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, deleteParams, 2, Constants.TOKEN_LEN);
				System.arraycopy(int2byte(userId.length()),
						0, deleteParams, 2+Constants.TOKEN_LEN, 4);
				System.arraycopy(strToByteArray(userId),
						0, deleteParams, 6+Constants.TOKEN_LEN	, userId.length());
				
				ps.write(deleteParams);
				ps.flush();

				//读取server返回值
				byte[] addBytes = new byte[2];
				is.read(addBytes);
				resCode = new String(addBytes);
				StringLogUtil.log(resCode);
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
		return resCode;
	}

	/*
	 * 用户修改
	 * @param token:用户token
	 * @param person:参数对象
	 **/
	public String editByToken(String token, PersonPojo person) {
		String resCode = "";

		PrintStream ps = null;
		Socket s = null;
		InputStream is = null;
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			System.out.println("edit person token:"+token);
			byte[] editParamsValidate = new byte[4+token.length()];
			System.arraycopy(strToByteArray(CMD_UIMF),
					0, editParamsValidate, 0, 4);
			System.arraycopy(strToByteArray(token),
					0, editParamsValidate, 4, token.length());
			ps.write(editParamsValidate);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[2];
			int len = is.read(dataBytes);
			System.out.println("edit person validate receive byste length:"+len);
			resCode = new String(dataBytes);
			//可以修改
			if(Constants.EDIT_PERSON_CM.equals(resCode)) {
				//二次验证，多余信令后期删除
				String userId = person.getId();
				 editParamsValidate = new byte[2+token.length()+4+userId.length()];
				System.arraycopy(strToByteArray("MF"),
						0, editParamsValidate, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, editParamsValidate, 2, token.length());
				System.arraycopy(int2byte(userId.length()),
						0, editParamsValidate, 2+Constants.TOKEN_LEN, 4);
				System.arraycopy(strToByteArray(userId),
						0, editParamsValidate, 6+Constants.TOKEN_LEN, userId.length());
				ps.write(editParamsValidate);
				ps.flush();

				is = s.getInputStream();
				dataBytes = new byte[2];
				len = is.read(dataBytes);
				resCode = new String(dataBytes);
				if(!Constants.EDIT_PERSON_CM.equals(resCode)) {
					System.out.println("----------error---------- edit person:"+resCode);
					return resCode;
				}
				
				int byteLen = 0;
				byte[] addParams = new byte[512];
				System.arraycopy(strToByteArray(CMD_MF),
						0, addParams, 0, 2);
				System.arraycopy(strToByteArray(token),
						0, addParams, 2, token.length());
				String id = "US"+person.getId();
				String tel = "PH"+person.getTelphone();
				//String name = "NA"+person.getName();
				String sex = "SE"+person.getSex();
				String job = "JO"+person.getJob();
				String birthday = "BD"+person.getBirthday();
				String email = "MB"+person.getEmail();
				String workAddress = "OA"+person.getWorkAddress();
				String bakPhone = "SP"+person.getBakPhone();
				String workPhone = "OP"+person.getWorkPhone();
				String company = "CO"+person.getCompany();
				
				byteLen += 2 + token.length();
				//用户ID
				byteLen = copyArray(addParams, byteLen, id);
				//手机号
				byteLen = copyArray(addParams, byteLen, tel);
				//角色
				System.arraycopy(int2byte(person.getRole()), 0, addParams, byteLen, 4);
				byteLen += 4;
				//姓名
				//byteLen = copyArray(addParams, byteLen, name);
				//性别
				byteLen = copyArray(addParams, byteLen, sex);
				//职务
				byteLen = copyArray(addParams, byteLen, job);
				//出生年月
				byteLen = copyArray(addParams, byteLen, birthday);
				//邮箱
				byteLen = copyArray(addParams, byteLen, email);
				//办公地址
				byteLen = copyArray(addParams, byteLen, workAddress);
				//备用手机
				byteLen = copyArray(addParams, byteLen, bakPhone);
				//办公电话
				byteLen = copyArray(addParams, byteLen, workPhone);
				//所在单位
				byteLen = copyArray(addParams, byteLen, company);

				ps.write(addParams);
				ps.flush();
				
				//读取server返回值
				byte[] addBytes = new byte[4];
				len = is.read(addBytes);
				System.out.println("add person receive byte length:"+len);
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
		System.out.println("edit return code:"+resCode);
		return resCode;
	}
}
