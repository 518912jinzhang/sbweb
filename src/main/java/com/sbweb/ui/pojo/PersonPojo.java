package com.sbweb.ui.pojo;

public class PersonPojo {
	private String retCode;
	private int total;
	//用户Id
	private String id;
	//用户名
	private String name;
	//密码
	private String pwd;
	//新密码
	private String newPwd;
	//确认新密码
	private String newPwd2;
	private String identifCode;
	//手机号
	private String telphone;
	//角色  系统管理员Mri： 0， 用户管理员： 1， 普通用户： 2
	private int role;
	//角色  系统管理员Mri： 0， 用户管理员： 1， 普通用户： 2
	private String roleDesc;
	//性别
	private String sex;
	//职务
	private String job;
	//出生年月
	private String birthday;
	//邮箱
	private String email;
	//办公地址
	private String workAddress;
	//备用手机
	private String bakPhone;
	//办公电话
	private String workPhone;
	//所在单位
	private String company;
	//上级用户ID
	private String spid;
	//个人头像
	private String pic;
	private String token;
	
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	/**
	 * @return the roleDesc
	 */
	public String getRoleDesc() {
		return roleDesc;
	}
	/**
	 * @param roleDesc the roleDesc to set
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	/**
	 * @return the identifCode
	 */
	public String getIdentifCode() {
		return identifCode;
	}
	/**
	 * @param identifCode the identifCode to set
	 */
	public void setIdentifCode(String identifCode) {
		this.identifCode = identifCode;
	}
	/**
	 * @return the newPwd
	 */
	public String getNewPwd() {
		return newPwd;
	}
	/**
	 * @param newPwd the newPwd to set
	 */
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	/**
	 * @return the newPwd2
	 */
	public String getNewPwd2() {
		return newPwd2;
	}
	/**
	 * @param newPwd2 the newPwd2 to set
	 */
	public void setNewPwd2(String newPwd2) {
		this.newPwd2 = newPwd2;
	}
	/**
	 * @return the retCode
	 */
	public String getRetCode() {
		return retCode;
	}
	/**
	 * @param retCode the retCode to set
	 */
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return the telphone
	 */
	public String getTelphone() {
		return telphone;
	}
	/**
	 * @param telphone the telphone to set
	 */
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	/**
	 * @return the role
	 */
	public int getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(int role) {
		this.role = role;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the workAddress
	 */
	public String getWorkAddress() {
		return workAddress;
	}
	/**
	 * @param workAddress the workAddress to set
	 */
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	/**
	 * @return the bakPhone
	 */
	public String getBakPhone() {
		return bakPhone;
	}
	/**
	 * @param bakPhone the bakPhone to set
	 */
	public void setBakPhone(String bakPhone) {
		this.bakPhone = bakPhone;
	}
	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}
	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the pic
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * @param pic the pic to set
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	public void format() {
		if(null != this.getId() && this.getId().length()>2) {
			this.setId(this.getId().substring(2));
		}
		if(null != this.getName() && this.getName().startsWith("NM")) {
			this.setName(this.getName().substring(2));
		}
		if(null != this.getPwd() && this.getPwd().length()>2) {
			this.setPwd(this.getPwd().substring(2));
		}
		if(null != this.getTelphone() && this.getTelphone().length()>2) {
			this.setTelphone(this.getTelphone().substring(2));
		}
		if(null != this.getSex() && this.getSex().length()>2) {
			this.setSex(this.getSex().substring(2));
		}
		if(null != this.getJob() && this.getJob().length()>2) {
			this.setJob(this.getJob().substring(2));
		}
		if(null != this.getBirthday() && this.getBirthday().length()>2) {
			this.setBirthday(this.getBirthday().substring(2));
		}
		if(null != this.getEmail() && this.getEmail().length()>2) {
			this.setEmail(this.getEmail().substring(2));
		}
		if(null != this.getWorkAddress() && this.getWorkAddress().length()>2) {
			this.setWorkAddress(this.getWorkAddress().substring(2));
		}
		if(null != this.getBakPhone() && this.getBakPhone().length()>2) {
			this.setBakPhone(this.getBakPhone().substring(2));
		}
		if(null != this.getWorkPhone() && this.getWorkPhone().length()>2) {
			this.setWorkPhone(this.getWorkPhone().substring(2));
		}
		if(null != this.getCompany() && this.getCompany().length()>2) {
			this.setCompany(this.getCompany().substring(2));
		}
	}
	
	public String toString() {
		return "id:["+id+"]"+
				"name["+name+"]"+
				"pwd["+pwd+"]"+
				"telphone["+telphone+"]"+
				"role["+role+"]"+
				"sex["+sex+"]"+
				"job["+job+"]"+
				"birthday["+birthday+"]"+
				"email["+email+"]"+
				"workAddress["+workAddress+"]"+
				"bakPhone["+bakPhone+"]"+
				"workPhone["+workPhone+"]"+
				"spid["+spid+"]"+
				"company["+company+"]"
				;
	}
}
