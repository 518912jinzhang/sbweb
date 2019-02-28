package com.sbweb.init;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbweb.common.MD5Encrypt;
import com.sbweb.common.StringLogUtil;
import com.sbweb.init.service.PersonService;
import com.sbweb.ui.pojo.PersonPojo;

@Controller
public class PersonController {

	@Autowired 
	private PersonService personService;
	
    @RequestMapping(value="/memberList", method=RequestMethod.GET)  
    public String memberList(Model model, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	String userId = (String) req.getSession().getAttribute("userId");
    	System.out.println("--memberList--token:-------"+token);
    	List<PersonPojo> list = personService.getPersonList(token);
    	model.addAttribute("list", list);
    	model.addAttribute("size", list.size());
    	model.addAttribute("userId", userId);
    	model.addAttribute("roleId", req.getSession().getAttribute("roleId"));
        return "business/memberlist";
    }

    /*
     * 跳转修改密码页
     * */
    @RequestMapping(value="/changePassword", method=RequestMethod.GET)  
    public String changePassword(Model model, String id, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	PersonPojo personPojo = new PersonPojo();
    	personPojo = personService.getPersonInfo(token, id);
    	model.addAttribute("person", personPojo);
        return "business/change-password";
    }
    
    /*
     * 修改密码获取验证码
     * */
    @RequestMapping(value="/changePwdValid", method=RequestMethod.POST)
    @ResponseBody
    public String changePwdValid(HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	String identifCode = personService.changePwdValid(token);
    	return "{\"identifCode\":\""+identifCode+"\"}";
    }
    
    /*
     * 修改密码提交
     * */
    @RequestMapping(value="/changePwdSubmit", method=RequestMethod.POST)
    @ResponseBody
    public String changePwdSubmit(Model model, @ModelAttribute PersonPojo person, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	person.setPwd(MD5Encrypt.encrypt(person.getNewPwd()));
    	String retCode = personService.changePassword(token, person.getId(), person.getPwd(), person.getIdentifCode());
    	model.addAttribute("retCode", retCode);
		return "{\"retCode\":\""+retCode+"\"}";
    }
    
    @RequestMapping(value="/personEditShow", method=RequestMethod.GET)  
    public String personEditShow(Model model, String id, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	PersonPojo personPojo = new PersonPojo();
    	personPojo = personService.getPersonInfo(token, id);
    	model.addAttribute("person", personPojo);
        return "business/member-edit";
    }
    
    @RequestMapping(value="/personViewShow", method=RequestMethod.GET)  
    public String personViewShow(Model model, String id, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	PersonPojo personPojo = new PersonPojo();
    	personPojo = personService.getPersonInfo(token, id);
    	model.addAttribute("person", personPojo);
        return "business/member-view";
    }
    
    @RequestMapping(value="/personAddShow", method=RequestMethod.GET)  
    public String personAddShow(Model model, HttpServletRequest req) {
    	PersonPojo personPojo = new PersonPojo();
    	model.addAttribute("personPojo", personPojo);
    	model.addAttribute("roleId", req.getSession().getAttribute("roleId"));
        return "business/member-add";
    }

    /*
     * 用户添加
     * */
    @RequestMapping(value="/personAdd", method=RequestMethod.POST)
    @ResponseBody
    public String personAdd(Model model, @ModelAttribute PersonPojo personPojo, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	personPojo.setPwd(MD5Encrypt.encrypt(personPojo.getPwd()));
    	personPojo.setTelphone(personPojo.getTelphone());
    	personPojo.setName(personPojo.getName());
    	String retCode = personService.add(token, personPojo);
    	return "{\"retCode\":\""+retCode+"\"}";
    }

    @RequestMapping(value="/personDelete", method=RequestMethod.POST)
    @ResponseBody
    public String personDelete(@ModelAttribute PersonPojo personPojo, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	System.out.println("----personDelete token:-------"+token);
    	String retCode = personService.deleteByToken(token, personPojo.getId());
    	return "{\"retCode\":\""+retCode+"\"}";
    }
    
    @RequestMapping(value="/personEdit", method=RequestMethod.POST)
    @ResponseBody
    public String personEdit(@ModelAttribute PersonPojo personPojo, HttpServletRequest req) {
    	String token = (String) req.getSession().getAttribute("token");
    	System.out.println("----personEdit token:-------"+token);
    	String retCode = personService.editByToken(token, personPojo);
    	return "{\"retCode\":\""+retCode+"\"}";
    }
}
