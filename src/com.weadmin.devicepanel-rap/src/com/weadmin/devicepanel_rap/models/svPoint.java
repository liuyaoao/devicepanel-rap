package com.weadmin.devicepanel_rap.models;

public final class svPoint {

	public float x;
	public float y;

	public  svPoint clone()
	{
		svPoint varCopy = new svPoint();

		varCopy.x = this.x;
		varCopy.y = this.y;

		return varCopy;
	}
}
