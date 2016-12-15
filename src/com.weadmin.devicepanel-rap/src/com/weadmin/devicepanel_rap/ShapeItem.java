package com.weadmin.devicepanel_rap;

import org.eclipse.rap.rwt.RWT;
//import org.eclipse.rap.rwt.internal.lifecycle.WidgetAdapter;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;

@SuppressWarnings("restriction")
public class ShapeItem extends Item {

	private static final String REMOTE_TYPE = "d3svgdevicepanel.ShapeItem";
	private final RemoteObject remoteObject;
	private final SvgMap svgMap;
	private String value;
	private int svgtype;
	private String transform;
	private String itemid;
	private String svid;
	

	private int linewidth;
	private String fillcolor;//填充 颜色
	private String linecolor;//line颜色
	private String fontname;
	private String fontsize;
	private String textanchor;
	//private String fillopacity;
	private String cssclass;

	//private float[] values;
	//private Color color;
	
	

	private String ecx;
	private String ecy;
	private String erx;
	private String ery;

	
	public ShapeItem(SvgMap svgMap) {
		super(svgMap, SWT.NONE);
		this.svgMap=svgMap;
		svgMap.addItem(this);
		remoteObject = RWT.getUISession().getConnection()
				.createRemoteObject(REMOTE_TYPE);
		remoteObject.set("parent", svgMap.getRemoteId());
	}
	public String getSvid() {
		checkWidget();
		return svid;
	}
	public void setSvid(String svid) {
		checkWidget();
		if(this.svid!=svid)
		{
		this.svid = svid;
		remoteObject.set("svid",svid);
		}
	}
	public int getSvgtype() {
		checkWidget();
		return svgtype;
	}

	public void setSvgtype(int svgtype) {
		checkWidget();
		if(this.svgtype!=svgtype)
		{
		this.svgtype = svgtype;
		remoteObject.set("svgtype",svgtype);
		}
	}

	public String getFontname() {
		checkWidget();
		return fontname;
	}
	public void setFontname(String fontname) {
		checkWidget();
		if(this.fontname!=fontname)
		{
		this.fontname = fontname;
		remoteObject.set("fontname",fontname);
		}
	}
	public String getFontsize() {
		checkWidget();
		return fontsize;
	}
	public void setFontsize(String fontsize) {
		checkWidget();
		if(this.fontsize!=fontsize)
		{
		this.fontsize = fontsize;
		remoteObject.set("fontsize",fontsize);
		}
	}
	public String getTextanchor() {
		checkWidget();
		return textanchor;
	}
	public void setTextanchor(String textanchor) {
		checkWidget();
		if(this.textanchor!=textanchor)
		{
		this.textanchor = textanchor;
		remoteObject.set("textanchor",textanchor);
		}
	}
	
	public void setsFont(String fontname1,String fontsize1,String textanchor1)
	{
		this.setFontname(fontname1);
		this.setFontsize(fontsize1);
		this.setTextanchor(textanchor1);
	}

	public int getLinewidth() {
		checkWidget();
		return linewidth;
	}
	public void setLinewidth(int linewidth) {
		checkWidget();
		if(this.linewidth!=linewidth)
		{
		this.linewidth = linewidth;
		remoteObject.set("linewidth", linewidth);
		}
	}
	public String getFillcolor() {
		checkWidget();
		return fillcolor;
	}
	public void setFillcolor(String fillcolor) {
		checkWidget();
		if(this.fillcolor!=fillcolor)
		{
		this.fillcolor = fillcolor;
		remoteObject.set("fillcolor", fillcolor);
		}
	}
	//public String getFillopacity() {
	//	checkWidget();
	//	return fillopacity;
	//}
	//public void setFillopacity(String fillopacity) {
	//	checkWidget();
	//	if(this.fillopacity!=fillopacity)
	//	{
	//	this.fillopacity = fillopacity;
	//	remoteObject.set("fillopacity", fillopacity);
	//	}
	//}
	public String getLinecolor() {
		checkWidget();
		return linecolor;
	}
	public void setLinecolor(String linecolor) {
		checkWidget();
		if(this.linecolor!=linecolor)
		{
		this.linecolor = linecolor;
		remoteObject.set("linecolor", linecolor);
		}
	}

	public String getTransform() {
		checkWidget();
		return transform;
	}
	public void setTransform(String transform) {
		if(this.transform != transform)
		{
		this.transform = transform;
		remoteObject.set("transform",transform);
		}
	}
	public String getItemid() {
		checkWidget();
		return itemid;
	}
	public void setItemid(String itemid) {
		checkWidget();
		if(this.itemid != itemid )
		{
			this.itemid=itemid;
			remoteObject.set("itemid", itemid);
		}
	}
	public String getCssclass(){
		checkWidget();
		return cssclass;
	}
	public void setCssclass( String cssclass){
		checkWidget();
		if(this.cssclass != cssclass)
		{
			this.cssclass = cssclass;
			remoteObject.set("cssclass", cssclass);
		}
	}
	
	
	public String getValue() {
		checkWidget();
		return value;
	}

	public void setValue(String value) {
		checkWidget();
		if (this.value != value) {
			this.value = value;
			remoteObject.set("value", value);
		}
	}

//	public float[] getValues() {
//		checkWidget();
//		return values == null ? null : values.clone();
//	}
//
//	public void setValues(float... values) {
//		checkWidget();
//		if (!Arrays.equals(this.values, values)) {
//			this.values = values.clone();
//			remoteObject.set("values", jsonArray(values));
//		}
//	}

//	public Color getColor() {
//		checkWidget();
//		return color == null ? getSvgMap().getDisplay().getSystemColor(
//				SWT.COLOR_BLACK) : color;
//	}
//
//	public void setColor(Color color) {
//		checkWidget();
//		if (color == null ? this.color != null : !color.equals(this.color)) {
//			this.color = color;
//			remoteObject.set("color",
//					ProtocolUtil.getJsonForColor(getColor(), false));
//		}
//	}
	public String getEcx() {
		checkWidget();
		return ecx;
	}
	public void setEcx(String ecx) {
		checkWidget();
		if(this.ecx != ecx)
		{
		this.ecx = ecx;
		remoteObject.set("ecx", ecx);
		}
	}
	public String getEcy() {
		checkWidget();
		return ecy;
	}
	public void setEcy(String ecy) {
		checkWidget();
		if(this.ecy != ecy )
		{
		this.ecy = ecy;
		remoteObject.set("ecy", ecy);
		}
	}
	public String getErx() {
		checkWidget();
		return erx;
	}
	public void setErx(String erx) {
		checkWidget();
		if(this.erx != erx)
		{
		this.erx = erx;
		remoteObject.set("erx", erx);
		}
	}
	public String getEry() {
		checkWidget();
		return ery;
	}
	public void setEry(String ery) {
		checkWidget();
		if(this.ery != ery)
		{
		this.ery = ery;
		remoteObject.set("ery", ery);
		}
	}
	/**
	 * 椭圆
	 * @param cx
	 * @param cy
	 * @param rx
	 * @param ry
	 */
	public void setEllipse(String cx,String cy ,String rx,String ry)
	{
		this.setEcx(cx);
		this.setEcy(cy);
		this.setErx(rx);
		this.setEry(ry);
       	
	}
	/**
	 * 矩形
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setRect(String x,String y ,String width,String height)
	{
		this.setEcx(x);
		this.setEcy(y);
		this.setErx(width);
		this.setEry(height);
       	
	}
	@Override
	public void setText(String text) {
		super.setText(text);
		remoteObject.set("text", text);
	}

	@Override
	public void dispose() {
		super.dispose();
		getSvgMap().removeItem(this);
		remoteObject.destroy();
	}

	private SvgMap getSvgMap() {
		return svgMap;
		//return (SvgMap) getAdapter(WidgetAdapter.class).getParent();
	}

//	private static JsonArray jsonArray(float[] values) {
//		// TODO use array.addAll in future versions
//		JsonArray array = new JsonArray();
//		for (int i = 0; i < values.length; i++) {
//			array.add(values[i]);
//		}
//		return array;
//	}

}
