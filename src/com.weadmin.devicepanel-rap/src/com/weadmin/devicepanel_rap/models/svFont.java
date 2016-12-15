package com.weadmin.devicepanel_rap.models;

public final class svFont {

	public svFont() {
		// TODO Auto-generated constructor stub
	}
	public String fontname;//字体名称
	public float fontsize;//字体大小
	public String textanchor;//字体对齐 start middle end
	public int fontstyle;//bold 1 italic 2 underline3
	public String val;// text

	public svFont clone()
	{
		svFont varCopy = new svFont();

		varCopy.fontname = this.fontname;
		varCopy.fontsize = this.fontsize;
		varCopy.textanchor = this.textanchor;
		varCopy.fontstyle = this.fontstyle;
		varCopy.val=this.val;
		return varCopy;
	}
}
