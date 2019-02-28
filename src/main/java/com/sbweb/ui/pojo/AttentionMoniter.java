package com.sbweb.ui.pojo;

import java.util.List;

/*
 * 关注终端实体类
 * */
public class AttentionMoniter {

	//关注数量
	private int num;
	//关注用户名
	private String userId;
	//
	private String ids;
	//终端ID
	private String moniterId;
	//关注类型
	private String attentionType;
	//关注日期
	private String attentionDate;
	//关注用户名
	private List<Integer> moniterIdList;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the attentionType
	 */
	public String getAttentionType() {
		return attentionType;
	}
	/**
	 * @param attentionType the attentionType to set
	 */
	public void setAttentionType(String attentionType) {
		this.attentionType = attentionType;
	}
	/**
	 * @return the moniterIdList
	 */
	public List<Integer> getMoniterIdList() {
		return moniterIdList;
	}
	/**
	 * @param moniterIdList the moniterIdList to set
	 */
	public void setMoniterIdList(List<Integer> moniterIdList) {
		this.moniterIdList = moniterIdList;
	}
	/**
	 * @return the attentionDate
	 */
	public String getAttentionDate() {
		return attentionDate;
	}
	/**
	 * @param attentionDate the attentionDate to set
	 */
	public void setAttentionDate(String attentionDate) {
		this.attentionDate = attentionDate;
	}
	/**
	 * @return the moniterId
	 */
	public String getMoniterId() {
		return moniterId;
	}
	/**
	 * @param moniterId the moniterId to set
	 */
	public void setMoniterId(String moniterId) {
		this.moniterId = moniterId;
	}
	
}
