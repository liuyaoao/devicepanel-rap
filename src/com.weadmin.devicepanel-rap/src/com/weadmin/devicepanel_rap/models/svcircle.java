package com.weadmin.devicepanel_rap.models;

public class svcircle {

	/**
	 * 圆
	 */
	public svcircle() {
		// TODO Auto-generated constructor stub
	}
	String cx="100" ,cy="50", r="40";
	/**
	 * 圆点x坐标
	 * @return
	 */
	public String getCx() {
		return cx;
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	/**
	 * 圆点y坐标
	 * @return
	 */
	public String getCy() {
		return cy;
	}
	public void setCy(String cy) {
		this.cy = cy;
	}
	/**
	 * 半径
	 * @return
	 */
	public String getR() {
		return r;
	}
	public void setR(String r) {
		this.r = r;
	}
}
