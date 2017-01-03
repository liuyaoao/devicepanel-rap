package com.weadmin.devicepanel_rap;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Listener;

import com.weadmin.devicepanel_rap.SVWidgetBase.CustomRes;

public class DevicePanelSvg extends SVWidgetBase {

	 private static final String REMOTE_TYPE = "svgdevicepanel.devicepanelsvgjs";
	 private static final long serialVersionUID = -7580109674486263420L;
	 private String sysObjId = "";
	 private String svgTxt = "";
	 private String svgTxtPrefix = ""; //svg files header,当重新写入svg的时候还要加上的。
	 private JsonObject svgSize = null;
	 private JsonObject statuss;
	 private String menudesc;
	 private JsonObject tooltipdata;
	 private String[] interfaceNameList;

	public DevicePanelSvg(Composite parent, int style) {
		super(parent,style);
		// this.setTooltipdesc("端口信息\n端口类型：p1\n端口索引：p2\n端口描述：p3\n接口索引：p4\n端口状态：p5\n管理状态：p6\n接收流量：p7\n发送流量：p8\n速率   ：p9");
		this.setMenudesc("打开端口:关闭端口:当前端口连接设备");
	}

	public void addOneSvgPanelById(String sysobjid){
		setSysObjId(sysobjid);
		ClassLoader classLoader = SVWidgetBase.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("resources/" + "svgs/"+sysobjid+".svg");
		byte bt[] = new byte[5242880]; //最大可放5M大小字节
    int len = 0;
    int temp=0;          //所有读取的内容都使用temp接收
		int startIndex = 0;
    try {
    	while((temp=inputStream.read())!=-1){    //当没有读取完时，继续读取
          bt[len]=(byte)temp;
          len++;
      }
      inputStream.close();
    }catch(IOException ioe){
    	throw new IllegalArgumentException("Failed to load resources", ioe);
    }
    try{
    	svgTxt = new String(bt,"UTF-8");
    }catch(UnsupportedEncodingException e){
    	throw new IllegalArgumentException("Failed to load resources", e);
    }
		setSvgTxt(svgTxt);
	}
  // add svg chart by svg xml string.
  public void addOneSvgPanelBySvgTxt(String svgtxt){
    setSvgTxt(svgtxt);
  }
	public void refreshAll(JsonObject statuss, JsonObject tooltipdata) {
		setStatuss(statuss);
		setTooltipdata(tooltipdata);
		super.callRemoteMethod("refreshAll", JsonObject.readFrom("{}"));
	}
	public void refreshSize(JsonObject size){
		this.svgSize = size;
		super.callRemoteMethod("refreshSize",this.svgSize);
	}
	public String getSysObjId(){
		checkWidget();
		return sysObjId;
	}
	public void setSysObjId(String objid){
		checkWidget();
		sysObjId = objid;
	}
	public String getMenudesc() {
		checkWidget();
		return menudesc;
	}

	public void setMenudesc(String menudesc) {
		checkWidget();
		if(this.menudesc != menudesc)
		{
		this.menudesc = menudesc;
		remoteObject.set( "menudesc", menudesc );
		}
	}
	public JsonObject getTooltipdata() {
		checkWidget();
		return tooltipdata == null ? null : JsonObject.readFrom(tooltipdata.asString());
	}

	public void setTooltipdata(JsonObject tooltipdata) {
		//checkWidget();
		this.tooltipdata = tooltipdata;
		remoteObject.set("tooltipdata", tooltipdata);//JsonObject.readFrom(tooltipdata.asString()));
	}
	public JsonObject getStatuss() {
		checkWidget();
		return statuss == null ? new JsonObject() : JsonObject.readFrom(statuss.asString());
	}

	public void setStatuss(JsonObject statuss) {
		//checkWidget();
		this.statuss = statuss;
		remoteObject.set("statuss", statuss);//JsonObject.readFrom(statuss.asString()));
	}
	/* 获取svg文件内容文本字符串*/
	public String getSvgTxt() {
		checkWidget();
		return svgTxt;
	}
	public void setSvgTxt(String svgtxt) {
		checkWidget();
    int index = svgtxt.indexOf("<svg");
		this.svgTxtPrefix = svgtxt.substring(0,index);
		// System.out.println("svgTxtPrefix:"+svgTxtPrefix);
		svgtxt = svgtxt.substring(index);
		this.svgTxt = svgtxt;
		remoteObject.set( "svgTxt", svgtxt );
	}

	public JsonObject getSvgSize(){
		return this.svgSize;
	}
	public double getWidth(){
		return this.svgSize.get("width").asDouble();
	}
	public double getHeight(){
		return this.svgSize.get("height").asDouble();
	}

	public String[] getInterfaceNameList(){
		return this.interfaceNameList;
	}
	public void setInterfaceNameList(String[] list){
		this.interfaceNameList = list;
		remoteObject.set( "interfaceNameList", jsonArray(list) );
	}
	public void rewriteSvgFile(){
		super.callRemoteMethod("getModifiedSvgTxt", JsonObject.readFrom("{}"));
		System.out.println("modified svgtxt:"+this.svgTxt);
		String wholeTxt = this.svgTxtPrefix + this.svgTxt;
		// TODO
	}

// ==================================
	private static JsonArray jsonArray(int[] values) {
		JsonArray array = new JsonArray();
		for (int i = 0; i < values.length; i++) {
			array.add(values[i]);
		}
		return array;
	}
	private static JsonArray jsonArray(String[] values) {
		JsonArray array = new JsonArray();
		for (int i = 0; i < values.length; i++) {
			array.add(values[i]);
		}
		return array;
	}

	String getRemoteId() {
		return remoteObject.getId();
	}

	@Override
	public void addListener( int eventType, Listener listener ) {
		boolean wasListening = isListening( SWT.Selection );
		super.addListener( eventType, listener );
		if( eventType == SWT.Selection && !wasListening ) {
			remoteObject.listen( "Selection", true );
		}
	}

	@Override
	public void removeListener( int eventType, Listener listener ) {
		boolean wasListening = isListening( SWT.Selection );
		super.removeListener( eventType, listener );
		if( eventType == SWT.Selection && wasListening ) {
			if( !isListening( SWT.Selection ) ) {
				remoteObject.listen( "Selection", false );
			}
		}
	}

	@Override
	protected void removeListener( int eventType, SWTEventListener listener ) {
		super.removeListener( eventType, listener );
	}
	@Override
	protected void handleSetProp(JsonObject properties) {
		JsonValue sizeValue = properties.get( "svgSize" );
		JsonValue svgtxt = properties.get( "svgTxt" );
    if( sizeValue != null ) {
      String sizeStr = sizeValue.asString();
			JsonObject obj = JsonObject.readFrom(sizeStr);
			this.svgSize = obj;
			// System.out.println("svgSize:"+sizeStr);
    }
		if( svgtxt != null ) {
			this.svgTxt = svgtxt.asString();
		}
	}

	@Override
	protected void handleCallMethod(String method, JsonObject parameters) {
		// System.out.println("handleCallMethod :"+method);
	}

	@Override
	protected void handleCallNotify(String eventName, JsonObject parameters) {
		if ("Selection".equals(eventName)) {
			Event event = new Event();
			event.text = parameters.get("index").asString();
			event.data =  parameters.get("data");
			notifyListeners(SWT.Selection, event);
//			notifyListeners(SWT.INI, event);
		}
	}

	@Override
	protected String getWidgetName() {
		return "devicepanelsvgjs";
	}

	@Override
	protected ArrayList<CustomRes> getCustomRes() {
		ArrayList<CustomRes> res = new ArrayList<>();
		res.add(new CustomRes("main.css", true, true));
		res.add(new CustomRes("jquery.js", true, false));
		res.add(new CustomRes("portNameDialog.js", true, false));
		res.add(new CustomRes("menuPanel.js", true, false));
		res.add(new CustomRes("svgChartPanel.js", true, false));
		res.add(new CustomRes("handler.js", true, false));
		return res;
	}

}
