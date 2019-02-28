package com.sbweb.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.sbweb.common.BytesUtil2;
import com.sbweb.common.Constants;
import com.sbweb.common.DateUtil;
import com.sbweb.common.MD5Encrypt;
import com.sbweb.ui.pojo.LoginReq;
import com.sbweb.ui.pojo.LoginToken;

@Controller  
public class LoginController {  
	//用户存在
	private static final String ERROR_LA = "LA";
	// 用户不存在(账号、密码错误)
	private static final String ERROR_LE = "LE";
	//验证码错误
	private static final String ERROR_LC = "LC";
	//验证码超时
	private static final String ERROR_LT = "LT";
	private static final String CMD_UILA = "UILA";
	private static final String CMD_UILI = "UILI";
	
    @RequestMapping(value="/home", method=RequestMethod.GET)  
    public String home(@ModelAttribute LoginReq login,RedirectAttributes attr) {
    	System.out.println("token:"+attr.getFlashAttributes().get("token"));
        return "business/index";  
    }
    
    @RequestMapping(value="/welcome", method=RequestMethod.GET)  
    public String welcome(HttpServletRequest request) {  
    	request.setAttribute("username", "admin");
        return "business/welcome";  
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(Model model, String errCode) {
    	LoginReq req = new LoginReq();
    	if(!StringUtils.isEmpty(errCode)) {
    		String errMsg = "密码错误，请重新登陆";
        	model.addAttribute("errMsg", errMsg);
    	}
    	model.addAttribute("login", req);
        return "business/login";
    }
    
    /**
     *显示系统消息列表
     **/
    @RequestMapping(value="/showSystemMsg", method=RequestMethod.GET)
    public String showSystemMsg() {
        return "business/system-msg";
    }
    
    /**
     *系统消息推送
     **/
    @RequestMapping(value="/getSystemMsg", method=RequestMethod.POST)
    @ResponseBody
    public String getSystemMsg() {
    	List<String> msgList = new ArrayList<String>();
    	msgList.add("{\"msg\":\"2018-08-02 解放路终端2586  断电\"}");
    	msgList.add("{\"msg\":\"2018-08-02 解放路终端1592  双工异常\"}");
    	String listJson = JSON.toJSONString(msgList, true);
    	return listJson;
    }
    
    /**
     *获取验证码 第一步先判断用户是否存在，如果存在发送获取验证码指令
     **/
    @RequestMapping(value="/getIdentifCode", method=RequestMethod.POST) 
    @ResponseBody
    public String getIdentifCode(@ModelAttribute LoginReq login, HttpServletResponse rsp
    		, String errorCode) {
    	PrintStream ps = null;
    	InputStream is = null;
    	Socket s = null;
    	String id = login.getId();
    	String pwd = login.getPwd();
    	pwd = MD5Encrypt.encrypt(pwd);
    	String identifyCode = "";
    	String isUserExist = "";
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			byte[] ipt4IdentifyCode = new byte[22];
			System.arraycopy(BytesUtil2.strToByteArray(CMD_UILA),
					0, ipt4IdentifyCode, 0, 4);
			System.arraycopy(BytesUtil2.int2byte(id.length()),
					0, ipt4IdentifyCode, 4, 4);
			System.arraycopy(BytesUtil2.strToByteArray(id),
					0, ipt4IdentifyCode, 8, id.length());

			ps.write(ipt4IdentifyCode);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[8];
			int len = is.read(dataBytes);
			System.out.println("getIdentifCode receive byste length:"+len);
			byte[] userExist = new byte[2];
			System.arraycopy(dataBytes, 0, userExist, 0, 2);
			isUserExist = new String(userExist);
			System.out.println("getIdentifCode receive isUserExist:"+isUserExist);
			//用户存在取验证码
			if(ERROR_LA.equals(isUserExist)) {
				byte[] identifyCodeArr = new byte[6];
				System.arraycopy(dataBytes, 2, identifyCodeArr, 0, 6);
				identifyCode = new String(identifyCodeArr);
				System.out.println("Client login receive identifyCode:"+identifyCode);
			}

			s.close();
			ps.close();
			is.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != s) { s.close(); }
				if(null != ps) { ps.close(); }
				if(null != is) { is.close(); }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "{\"isUserExist\":\""+isUserExist+"\",\"identifyCode\":\""+identifyCode+"\"}";
    }

    @RequestMapping(value="/loginSubmit", method=RequestMethod.POST)  
    public String loginSubmit(@ModelAttribute LoginReq login, Model model
    		, HttpServletRequest req, HttpServletResponse response) {
    	PrintStream ps = null;
    	InputStream is = null;
    	Socket s = null;
    	String id = login.getId();
    	String pwd = login.getPwd();
    	pwd = MD5Encrypt.encrypt(pwd);
    	String identifyCode = login.getIdentifyCode();
    	String token = null;
    	String loginFlag = "";
    	String roleId = "";
		try {
			s = new Socket(Constants.SOCKET_URL, Constants.SOCKET_PORT);
			ps = new PrintStream(s.getOutputStream());
			byte[] loginParams = new byte[18+id.length()+pwd.length()];
			System.arraycopy(BytesUtil2.strToByteArray(CMD_UILI),
					0, loginParams, 0, 4);
			System.arraycopy(BytesUtil2.int2byte(id.length()),
					0, loginParams, 4, 4);
			System.arraycopy(BytesUtil2.strToByteArray(id),
					0, loginParams, 8, id.length());
			System.arraycopy(BytesUtil2.int2byte(pwd.length()),
					0, loginParams, 8+id.length(), 4);
			System.arraycopy(BytesUtil2.strToByteArray(pwd),
					0, loginParams, 12+id.length(), pwd.length());
			//验证码
			System.arraycopy(BytesUtil2.strToByteArray(identifyCode),
					0, loginParams, 12+id.length()+pwd.length(), identifyCode.length());

			ps.write(loginParams);
			ps.flush();
			
			//读取server返回值
			is = s.getInputStream();
			byte[] dataBytes = new byte[128];
			int len = is.read(dataBytes);
			System.out.println("Client login receive byste length:"+len);
			
			byte[] endFlagLen = new byte[4];
			System.arraycopy(dataBytes, 0, endFlagLen, 0, 4);
			int endPos = BytesUtil2.byteArrayToInt(endFlagLen);
			System.out.println("Client login receive endPos:"+endPos);

			byte[] endFlag = new byte[endPos];
			System.arraycopy(dataBytes, 4, endFlag, 0, endPos);
		    token = new String(endFlag);
			if(ERROR_LE.equals(token)) {
				System.out.println("login user name or password is wrong:"+token);
			} else if(ERROR_LC.equals(token)) {
				System.out.println("login identify Code is wrong:"+token);
			} else if(ERROR_LT.equals(token)) {
				System.out.println("login identify Code is timeout:"+token);
			} else {
				System.out.println("login success token:"+token);
			}

			//首次登陆:MP,非首次登陆:LS,信息错误:LE
			byte[] flag = new byte[2];
			System.arraycopy(dataBytes, 4+token.length(), flag, 0, 2);
		    loginFlag = new String(flag);
		    if("MP".equals(loginFlag)) {
		    	System.out.println("首次登陆!");
		    } else if("LS".equals(loginFlag)) {
		    	System.out.println("非首次登陆!");
		    } else if("LE".equals(loginFlag)) {
		    	System.out.println("信息错误,登陆失败!");
		    }
		    //用户角色
			byte[] role = new byte[4];
			System.arraycopy(dataBytes, 6+token.length(), role, 0, 4);
			roleId = ""+BytesUtil2.byteArrayToInt(role);

			s.close();
			ps.close();
			is.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != s) { s.close(); }
				if(null != ps) { ps.close(); }
				if(null != is) { is.close(); }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		login.setToken(token);
		req.getSession().setAttribute("token", token);
		req.getSession().setAttribute("userId", id);
		req.getSession().setAttribute("roleId", roleId);
    	model.addAttribute("user", login);
    	model.addAttribute("loginFlag", loginFlag);
    	LoginToken ticket = new LoginToken();
    	ticket.setStatus(1);
    	ticket.setToken(token);
    	ticket.setExpired(DateUtil.getDateAfterMin(new Date(),30));
    	ticket.setUserId(id);
    	Cookie cookie = new Cookie(id, ticket.toString());
        cookie.setPath("/");//可在同一应用服务器内共享cookie
        response.addCookie(cookie);

        if(token.length() != 24 || "LE".equals(loginFlag)) {
    		//转发
            return "redirect:login?errCode=1";
        } else {
    		//转发
            return "business/index";
        }
    }
} 