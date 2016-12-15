package com.weadmin.devicepanel_rap.models;

public final class EquipmentInfo
{
	public String Sysoid; //设备sysoid
	public int Width; //设备宽度
	public int Height; //设备高度
	public int Sockets; //设备模块数(插槽数)
	public int Ports; //设备端口数

	public EquipmentInfo clone()
	{
		EquipmentInfo varCopy = new EquipmentInfo();

		varCopy.Sysoid = this.Sysoid;
		varCopy.Width = this.Width;
		varCopy.Height = this.Height;
		varCopy.Sockets = this.Sockets;
		varCopy.Ports = this.Ports;

		return varCopy;
	}
}
