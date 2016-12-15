package com.weadmin.devicepanel_rap.models;

public class vsellipse {

	/**
	 * 椭圆
	 */
	public vsellipse() {
		// TODO Auto-generated constructor stub
	}
	String cx="240" ,cy="100", rx="220", ry="30";
	/**
	 * 圆点的 x 坐标
	 * @return
	 */
	public String getCx() {
		return cx;
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	/**
	 * 圆点的 y 坐标
	 * @return
	 */
	public String getCy() {
		return cy;
	}
	public void setCy(String cy) {
		this.cy = cy;
	}
	/**
	 * 水平半径
	 * @return
	 */
	public String getRx() {
		return rx;
	}
	public void setRx(String rx) {
		this.rx = rx;
	}
	/**
	 * 垂直半径
	 * @return
	 */
	public String getRy() {
		return ry;
	}
	public void setRy(String ry) {
		this.ry = ry;
	}
}
