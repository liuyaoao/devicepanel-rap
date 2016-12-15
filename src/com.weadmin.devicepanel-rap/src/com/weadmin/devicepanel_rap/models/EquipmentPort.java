package com.weadmin.devicepanel_rap.models;

public final  class EquipmentPort
{
	public int ContainerIndex; //容器编号
	public int PhysicsPortIndex; //设备物理端口号
	public String Alias; //别名

	public EquipmentPort clone()
	{
		EquipmentPort varCopy = new EquipmentPort();

		varCopy.ContainerIndex = this.ContainerIndex;
		varCopy.PhysicsPortIndex = this.PhysicsPortIndex;
		varCopy.Alias = this.Alias;

		return varCopy;
	}
}
