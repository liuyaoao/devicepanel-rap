package com.weadmin.devicepanel_rap.models;

public final  class EquipmentLamp
{
	public int ContainerIndex; //容器编号
	public int PhysicsPortLampIndex; //设备物理端口号
	public String Alias; //别名

	public EquipmentLamp clone()
	{
		EquipmentLamp varCopy = new EquipmentLamp();

		varCopy.ContainerIndex = this.ContainerIndex;
		varCopy.PhysicsPortLampIndex = this.PhysicsPortLampIndex;
		varCopy.Alias = this.Alias;

		return varCopy;
	}
}
