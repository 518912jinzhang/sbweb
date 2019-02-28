package com.sbweb.ui.pojo;

import java.util.List;

public class SktResponse<T> {

	private String retCode;

	private int retTotal;
	
	private List<T> datas;

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
	 * @return the datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * @param datas the datas to set
	 */
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	/**
	 * @return the retTotal
	 */
	public int getRetTotal() {
		return retTotal;
	}

	/**
	 * @param retTotal the retTotal to set
	 */
	public void setRetTotal(int retTotal) {
		this.retTotal = retTotal;
	}
	
}
