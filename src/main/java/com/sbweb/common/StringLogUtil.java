package com.sbweb.common;

public class StringLogUtil {

	public static void log(String retCode) {
		if("DF".equals(retCode)) {
			System.out.println("删除失败");
		} else if("DS".equals(retCode)) {
			System.out.println("删除成功");
		} else if("IE".equals(retCode)) {
			System.out.println("信息错误");
		} else if("TT".equals(retCode)) {
			System.out.println("Token 过期");
		} else if("NP".equals(retCode)) {
			System.out.println("没有权限");
		} else if("NU".equals(retCode)) {
			System.out.println("没有删除用户");
		} else if("CD".equals(retCode)) {
			System.out.println("可以删除");
		} else if("IFER".equals(retCode)) {
			System.out.println("信息错误");
		} else if("TOTI".equals(retCode)) {
			System.out.println("Token 过期");
		} else if("NOPM".equals(retCode)) {
			System.out.println("没有权限");
		} else if("USNN".equals(retCode)) {
			System.out.println("用户名不为空");
		} else if("USEX".equals(retCode)) {
			System.out.println("用户名已存在");
		} else if("USEX".equals(retCode)) {
			System.out.println("用户名已存在");
		} else if("PANN".equals(retCode)) {
			System.out.println("密码不能为空");
		} else if("PHNN".equals(retCode)) {
			System.out.println("手机号不能为空");
		} else if("RONN".equals(retCode)) {
			System.out.println("角色不能为空");
		} else if("NANN".equals(retCode)) {
			System.out.println("姓名不能为空");
		} else if("SENN".equals(retCode)) {
			System.out.println("性别不能为空");
		} else if("SENN".equals(retCode)) {
			System.out.println("性别不能为空");
		} else if("JONN".equals(retCode)) {
			System.out.println("职务不能为空");
		} else if("BINN".equals(retCode)) {
			System.out.println("出生年月不能为空");
		} else if("OFNN".equals(retCode)) {
			System.out.println("办公地址不能为空");
		} else if("CONN".equals(retCode)) {
			System.out.println("所在单位不能为空");
		} else if("USAF".equals(retCode)) {
			System.out.println("用户录入失败");
		} else if("USAS".equals(retCode)) {
			System.out.println("用户录入成功");
		} else if("NR".equals(retCode)) {//密码修改
			System.out.println("用户未注册");
		} else if("CE".equals(retCode)) {
			System.out.println("验证码错误");
		} else if("CT".equals(retCode)) {
			System.out.println("验证码超时");
		} else if("MF".equals(retCode)) {
			System.out.println("密码不能一样，修改失败");
		} else if("MS".equals(retCode)) {
			System.out.println("密码修改成功");
		}
	}
	
	public static String covertCode(String retCode) {
		if("DF".equals(retCode)) { return "删除失败";}
		else if("DS".equals(retCode)) { return "删除成功";}
		else if("IE".equals(retCode)) { return "信息错误";}
		else if("TT".equals(retCode)) {
			return "Token 过期";
		} else if("NP".equals(retCode)) {
			return "没有权限";
		} else if("NU".equals(retCode)) {
			return "没有删除用户";
		} else if("CD".equals(retCode)) {
			return "可以删除";
		} else if("IFER".equals(retCode)) {
			return "信息错误";
		} else if("TOTI".equals(retCode)) {
			return "Token 过期";
		} else if("NOPM".equals(retCode)) {
			return "没有权限";
		} else if("USNN".equals(retCode)) {
			return "用户名不为空";
		} else if("USEX".equals(retCode)) {
			return "用户名已存在";
		} else if("USEX".equals(retCode)) {
			return "用户名已存在";
		} else if("PANN".equals(retCode)) {
			return "密码不能为空";
		} else if("PHNN".equals(retCode)) {
			return "手机号不能为空";
		} else if("RONN".equals(retCode)) {
			return "角色不能为空";
		} else if("NANN".equals(retCode)) {
			return "姓名不能为空";
		} else if("SENN".equals(retCode)) {
			return "性别不能为空";
		} else if("SENN".equals(retCode)) {
			return "性别不能为空";
		} else if("JONN".equals(retCode)) {
			return "职务不能为空";
		} else if("BINN".equals(retCode)) {
			return "出生年月不能为空";
		} else if("OFNN".equals(retCode)) {
			return "办公地址不能为空";
		} else if("CONN".equals(retCode)) {
			return "所在单位不能为空";
		} else if("USAF".equals(retCode)) {
			return "用户录入失败";
		} else if("USAS".equals(retCode)) {
			return "用户录入成功";
		}
		
		return "";
	}
}
