package com.sbweb.common;

public interface Constants {
	//token 有效期
	int ACCESS_TOKEN = 30;
	String SOCKET_URL = "www.suixianyunwei.top";
	
	int SOCKET_PORT = 23859;
	//TOKEN长度
	int TOKEN_LEN = 24;
	//socket 编码
	String SOCKET_ENCODE = "UTF-8";

	String COMMON_ERROR_IE = "IE";//信息错误
	String COMMON_ERROR_TT = "TT";//token 过期
	String COMMON_ERROR_CR= "CR";//可以注册
	String COMMON_STATUS_DF= "DF";//删除失败
	String COMMON_STATUS_DS= "DS";//删除成功
	//可以修改
	String COMMON_CM = "CM";
	//获取成功
	String COMMON_GS = "GS";
	//可以修改
	String COMMON_MF = "MF";
	//可以修改
	String COMMON_MS = "MS";
	//可以查询
	String COMMON_CC = "CC";
	//可以查询
	String COMMON_DI = "DI";
	//可以查询
	String COMMON_DE = "DE";
	//可以查询
	String COMMON_CG = "CG";
	//短信超时
	String COMMON_CT = "CT";
	//短信短信错误
	String COMMON_CE = "CE";
	//关注列表添加失败
	String COMMON_AF = "AF";
	//关注列表添加成功
	String COMMON_AS = "AS";
	//查询失败
	String COMMON_CF = "CF";
	//查询成功
	String COMMON_CS = "CS";

	//************************终端新增
	//终端编号已存在
	String ADD_MONITER_ME = "ME";
	//IMEI号已存在
	String ADD_MONITER_ET = "ET";
	//注册失败
	String ADD_MONITER_RF = "RF	";
	//注册成功
	String ADD_MONITER_RS = "RS";
	//终端未注册
	String DELETE_MONITER_NR = "NR";
	//可以删除
	String DELETE_MONITER_SS = "SS";
	//可以修改
	String EDIT_MONITER_TM = "TM";
	//可以修改
	String EDIT_DEVICE_MM = "MM";
	
	
	//************************用户新增
	//信息错误
	String ADD_PERSON_IE = "IE";
	//Token 过期
	String ADD_PERSON_TT = "TT";
	//没有权限
	String ADD_PERSON_NP = "NP";
	//可以创建
	String ADD_PERSON_CC = "CC";
	//可以查询
	String QUERY_PERSON_CK = "CK";
	//可以删除
	String DELETE_PERSON_CD = "CD";
	//可以修改
	String EDIT_PERSON_CM = "CM";
	//短信发送状态:发送成功
	String EDIT_PERSON_SS = "SS";
	//信息错误
	String ADD_PERSON_IFER = "IFER";
	//Token 过期
	String ADD_PERSON_TOTI = "TOTI";
	//没有权限
	String ADD_PERSON_NOPM = "NOPM";
	//用户不能为空
	String ADD_PERSON_USNN = "USNN";
	//用户已存在
	String ADD_PERSON_USEX = "USEX";
}
