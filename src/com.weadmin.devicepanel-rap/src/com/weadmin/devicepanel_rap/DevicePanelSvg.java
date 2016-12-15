package com.weadmin.devicepanel_rap;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Listener;

public class DevicePanelSvg extends SVWidgetBase {

	 private static final String REMOTE_TYPE = "d3svgdevicepanel.devicepanelsvgjs";
	 private static final long serialVersionUID = -7580109674486263420L;
	 private int barWidth = 25;
	 private int spacing = 2;
	 private int[] statuss;
	 private String tooltipdesc;
	 private String menudesc;
	 private String[] tooltipdata;
	 private final List<ShapeItem> items;

	public DevicePanelSvg(Composite parent, int style) {
		super(parent,style);
		items = new LinkedList<ShapeItem>();
		this.setTooltipdesc("端口信息\n端口类型：p1\n端口索引：p2\n端口描述：p3\n接口索引：p4\n端口状态：p5\n管理状态：p6\n接收流量：p7\n发送流量：p8\n速率   ：p9");
		this.setMenudesc("打开端口:关闭端口:当前端口连接设备");
	}
	public ShapeItem[] getItems() {
		checkWidget();
		return items.toArray( new ShapeItem[ 0 ] );
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
		if(this.tooltipdesc!=tooltipdesc)
		{
		this.tooltipdesc = tooltipdesc;
		remoteObject.set( "tooltipdesc", tooltipdesc );
		}
	}
	public int getBarWidth() {
	    checkWidget();
	    return barWidth;
	  }

	  public void setBarWidth( int width ) {
	    checkWidget();
	    if( width != barWidth ) {
	      barWidth = width;
	      remoteObject.set( "barWidth", width );
	    }
	  }

	  public int getSpacing() {
	    checkWidget();
	    return spacing;
	  }

	  public void setSpacing( int width ) {
	    checkWidget();
	    if( width != spacing ) {
	      spacing = width;
	      remoteObject.set( "spacing", width );
	    }
	  }
		private static JsonArray jsonArray(int[] values) {
		// TODO use array.addAll in future versions
		JsonArray array = new JsonArray();
		for (int i = 0; i < values.length; i++) {
			array.add(values[i]);
		}
		return array;
	}
	private static JsonArray jsonArray(String[] values) {
		// TODO use array.addAll in future versions
		JsonArray array = new JsonArray();
		for (int i = 0; i < values.length; i++) {
			array.add(values[i]);
		}
		return array;
	}

	void addItem( ShapeItem item ) {
		items.add( item );
	}

	void removeItem( ShapeItem item ) {
		items.remove( item );
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
		//  System.out.println("handleSetProp from the js trigger!!");
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
			//event.item = items.get(event.index);
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
		res.add(new CustomRes("fishEyeCalendar.css", true, true));
		res.add(new CustomRes("esl.js", true, false));
		res.add(new CustomRes("zrender.js", true, false));
		res.add(new CustomRes("jquery.js", true, false));
		res.add(new CustomRes("echarts.min.js", true, false));
		res.add(new CustomRes("piccolo2d.js", true, false));
		res.add(new CustomRes("detailCharts.js", true, false));
		res.add(new CustomRes("fishEyeCalendar.js", true, false));
		res.add(new CustomRes("handler.js", true, false));
		return res;
	}

}
