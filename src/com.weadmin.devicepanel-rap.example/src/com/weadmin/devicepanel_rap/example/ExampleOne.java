package com.weadmin.devicepanel_rap.example;

import java.util.Random;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.weadmin.devicepanel_rap.DevicePanelSvg;

public class ExampleOne extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new GridLayout(3,false));
//		parent.setLayout(null);
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Refresh");
		Button zoomInBtn = new Button(parent, SWT.PUSH);
		zoomInBtn.setText("zoomIn(+)");
		Button zoomOutBtn = new Button(parent, SWT.PUSH);
		zoomOutBtn.setText("zoomOut(-)");
		Button saveModifiedSvgBtn = new Button(parent, SWT.PUSH);
		saveModifiedSvgBtn.setText("save");

		DevicePanelSvg deviceSvg = new DevicePanelSvg(parent, SWT.NONE);
//		deviceSvg.setBounds(20, 0, 1000, 600);
		String sysObjId = "svg07"; //svg file name.
		deviceSvg.addOneSvgPanelById(sysObjId);
		deviceSvg.setLayoutData(new GridData(GridData.FILL_BOTH));

		deviceSvg.addListener(SWT.Selection, new Listener() {
			private static final long serialVersionUID = 1L;
			public void handleEvent(Event event) {
				String eventag = event.text;
//				System.out.println(portindex);
				if (eventag.equals("openport")) {
//					MsgBox.ShowError("打开端口！");
				} else if (eventag.equals("deviceip")) {
//					MsgBox.ShowError("查看端口连接的设备！");
				}else if(eventag.toLowerCase().equals("portport")){
					System.out.println("点击了端口！");
				}else if(eventag.toLowerCase().equals("svg_initialized")){
					System.out.println("svg_initialized");
					deviceSvg.setStatuss(createStatusMap(50));
					deviceSvg.setTooltipdata(createTooltipMap(50));
				}
			}
		});

		parent.getDisplay().timerExec(4000, new Runnable() { //refresh data after 2s later.
			public void run() {
				deviceSvg.setStatuss(createStatusMap(50));
				deviceSvg.setTooltipdata(createTooltipMap(50));
			}
		});


		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JsonObject tempSize = deviceSvg.getSvgSize();
				deviceSvg.refreshAll(createStatusMap(50),createTooltipMap(50));

			}
		});
		zoomInBtn.addSelectionListener(new SelectionAdapter() { //zoom in (+)
			@Override
			public void widgetSelected(SelectionEvent e) {
				JsonObject tempSize = deviceSvg.getSvgSize();
				double valWidth = tempSize.get("width").asDouble();
				double valHeight = tempSize.get("height").asDouble();
				tempSize.set("width",valWidth*1.1 );
				tempSize.set("height",valHeight*1.1 );
				deviceSvg.refreshSize(tempSize);
			}
		});
		zoomOutBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JsonObject tempSize = deviceSvg.getSvgSize();
				double valWidth = tempSize.get("width").asDouble();
				double valHeight = tempSize.get("height").asDouble();
				tempSize.set("width",valWidth*0.9 );
				tempSize.set("height",valHeight*0.9 );
				deviceSvg.refreshSize(tempSize);
			}
		});
		saveModifiedSvgBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deviceSvg.rewriteSvgFile();
			}
		});
	}

	public static String getRandom(int t){
		int i = (int) (Math.random()*t);
		String s = (i<10?"0"+i:i+"");
		return s;
	}
	private int[] createStatusArr(int num){
		int[] statusArr = new int[num];
		for(int i=0;i<num;i++){
			statusArr[i] = getRangeRandomNum(0,5);
		}
		return statusArr;
	}
	private JsonObject createStatusMap(int num){
		JsonObject statusMap = new JsonObject();
		for(int i=0;i<num;i++){
			statusMap.set("Ethernet0/"+(i+1), getRangeRandomNum(0,5)); //键值为端口名称，也就是接口名。
		}
		return statusMap;
	}
	private String[] createTooltipArr(int num){
		String[] tooltipArr = new String[num];
		for(int i=0;i<num;i++){
			tooltipArr[i] = "端口信息<br>端口类型：p1<br>端口索引：p2<br>端口描述：p3<br>接口索引：p4<br>端口状态：p5<br>管理状态：p6<br>接收流量：p7<br>发送流量：p8<br>速率   ：p9";
		}
		return tooltipArr;
	}
	private JsonObject createTooltipMap(int num){
		JsonObject tooltipMap = new JsonObject();
		for(int i=0;i<num;i++){ //键值为端口名称，也就是接口名。
			tooltipMap.set("Ethernet0/"+(i+1), "端口信息<br>端口类型：p1<br>端口索引：p2<br>端口描述：p3<br>接口索引：p4<br>端口状态：p5<br>管理状态：p6<br>接收流量：p7<br>发送流量：p8<br>速率   ：p9");
		}
		return tooltipMap;
	}
	private int getRangeRandomNum(int min, int max){
		return (new Random().nextInt(max - min) + min);
	}

}
