package com.sbweb.ui.query;

public class AttentionMoniterQuery {

	private String queryMoniterId;
	private String queryAttentionType;
	private String warnType;
	
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	/**
	 * @return the queryMoniterId
	 */
	public String getQueryMoniterId() {
		return queryMoniterId;
	}
	/**
	 * @param queryMoniterId the queryMoniterId to set
	 */
	public void setQueryMoniterId(String queryMoniterId) {
		this.queryMoniterId = queryMoniterId;
	}
	/**
	 * @return the queryAttentionType
	 */
	public String getQueryAttentionType() {
		return queryAttentionType;
	}
	/**
	 * @param queryAttentionType the queryAttentionType to set
	 */
	public void setQueryAttentionType(String queryAttentionType) {
		this.queryAttentionType = queryAttentionType;
	}
	
}
