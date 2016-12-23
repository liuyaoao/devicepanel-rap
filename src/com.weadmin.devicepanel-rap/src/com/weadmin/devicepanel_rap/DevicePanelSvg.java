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
	 private JsonObject svgSize = null;
	 private int[] statuss;
	 private String tooltipdesc;
	 private String menudesc;
	 private String[] tooltipdata;

	public DevicePanelSvg(Composite parent, int style) {
		super(parent,style);
		this.setTooltipdesc("端口信息\n端口类型：p1\n端口索引：p2\n端口描述：p3\n接口索引：p4\n端口状态：p5\n管理状态：p6\n接收流量：p7\n发送流量：p8\n速率   ：p9");
		this.setMenudesc("打开端口:关闭端口:当前端口连接设备");

	}

	public void addOneSvgPanel(String sysObjId){
		this.sysObjId = sysObjId;
		ClassLoader classLoader = SVWidgetBase.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("resources/" + "svgs/"+sysObjId+".svg");
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
//		svgTxt = URLDecoder.decode(svgTxt, "UTF-8");
    	svgTxt = new String(bt,"UTF-8");
    }catch(UnsupportedEncodingException e){
    	throw new IllegalArgumentException("Failed to load resources", e);
    }
		int index = svgTxt.indexOf("<svg");
		svgTxt = svgTxt.substring(index);
		setSvgTxt(svgTxt);
	}
	public void refreshAll(int[] statuss, String[] tooltipdata) {
		setStatuss(statuss);
		setTooltipdata(tooltipdata);
		super.callRemoteMethod("refreshAll", JsonObject.readFrom("{}"));
	}
	public void refreshSize(JsonObject size){
		this.svgSize = size;
		super.callRemoteMethod("refreshSize",this.svgSize);
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
	public String[] getTooltipdata() {
		checkWidget();
		return tooltipdata == null ? null : tooltipdata.clone();
	}

	public void setTooltipdata(String... tooltipdata) {
		//checkWidget();
		if (!Arrays.equals(this.tooltipdata, tooltipdata)) {
			this.tooltipdata = tooltipdata.clone();
			remoteObject.set("tooltipdata", jsonArray(tooltipdata));
		}
	}
	public int[] getStatuss() {
		checkWidget();
		return statuss == null ? null : statuss.clone();
	}

	public void setStatuss(int... statuss) {
		//checkWidget();
		if (!Arrays.equals(this.statuss, statuss)) {
			this.statuss = statuss.clone();
			remoteObject.set("statuss", jsonArray(statuss));
		}
	}
	public String getTooltipdesc() {
		checkWidget();
		return tooltipdesc;
	}

	public void setTooltipdesc(String tooltipdesc) {
		checkWidget();
		if(this.tooltipdesc!=tooltipdesc){
			this.tooltipdesc = tooltipdesc;
			remoteObject.set( "tooltipdesc", tooltipdesc );
		}
	}
	/* 获取svg文件内容文本字符串*/
	public String getSvgTxt() {
		checkWidget();
		return svgTxt;
	}
	public void setSvgTxt(String svgTxt) {
		checkWidget();
		this.svgTxt = svgTxt;
		remoteObject.set( "svgTxt", svgTxt );
	}
	public JsonObject getSvgSize(){
		return this.svgSize;
	}
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
      if( sizeValue != null ) {
        String sizeStr = sizeValue.asString();
        
				JsonObject obj = JsonObject.readFrom(sizeStr);
				this.svgSize = obj;
				System.out.println("svgSize:"+sizeStr);
				System.out.println("svgSize obj.width:"+obj.get("width"));
				System.out.println("svgSize obj.height:"+obj.get("height"));
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
		res.add(new CustomRes("d3.v3.min.js", true, false));
		res.add(new CustomRes("jquery.js", true, false));
		res.add(new CustomRes("menuPanel.js", true, false));
		res.add(new CustomRes("svgChartPanel.js", true, false));
		res.add(new CustomRes("handler.js", true, false));
		return res;
	}

}
