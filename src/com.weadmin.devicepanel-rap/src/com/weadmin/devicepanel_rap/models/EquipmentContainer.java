package com.weadmin.devicepanel_rap.models;

import java.awt.Rectangle;

public final  class EquipmentContainer
{
	public int ContainerIndex; // 容器编号
	public String Alias; //别名
	public Rectangle rect;

	public EquipmentContainer clone()
	{
		EquipmentContainer varCopy = new EquipmentContainer();

		varCopy.ContainerIndex = this.ContainerIndex;
		varCopy.Alias = this.Alias;
		varCopy.rect = this.rect;

		return varCopy;
	}
}
