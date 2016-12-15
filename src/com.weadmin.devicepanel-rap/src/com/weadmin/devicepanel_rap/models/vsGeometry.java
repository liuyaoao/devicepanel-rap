package com.weadmin.devicepanel_rap.models;

public final class vsGeometry
{
	public String name;
	public float x;
	public float y;
	public String a;
	public String b;
	public String c;
	public String d;
	public String e;

	public vsGeometry clone()
	{
		vsGeometry varCopy = new vsGeometry();

		varCopy.name = this.name;
		varCopy.x = this.x;
		varCopy.y = this.y;
		varCopy.a = this.a;
		varCopy.b = this.b;
		varCopy.c = this.c;
		varCopy.d = this.d;
		varCopy.e = this.e;

		return varCopy;
	}
}
