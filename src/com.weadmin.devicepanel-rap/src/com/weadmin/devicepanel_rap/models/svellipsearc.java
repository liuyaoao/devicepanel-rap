package com.weadmin.devicepanel_rap.models;

public class svellipsearc {

	public float rx;//弧的半长轴长度
	public float ry;//半短轴长度
	public String largearcflag;//为1 表示大角度弧线，0 代表小角度弧线。
	public String sweepflag;//为1代表从起点到终点弧线绕中心顺时针方向，0 代表逆时针方向。

	public  svellipsearc clone()
	{
		svellipsearc varCopy = new svellipsearc();

		varCopy.rx = this.rx;
		varCopy.ry = this.ry;
		varCopy.largearcflag=this.largearcflag;
		varCopy.sweepflag=this.sweepflag;

		return varCopy;
	}
}
