package com.weadmin.devicepanel_rap.example;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.weadmin.devicepanel_rap.DevicePanelSvg;

public class ExampleOne extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new GridLayout(3,false));
//		parent.setLayout(null);
		Composite parents = new Composite(parent, SWT.NONE);
		parents.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parents.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parents, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		composite.setLayout(new GridLayout(4, false));
		
		Button button = new Button(composite, SWT.PUSH);
		button.setText("Refresh");
		Button zoomInBtn = new Button(composite, SWT.PUSH);
		zoomInBtn.setText("zoomIn(+)");
		Button zoomOutBtn = new Button(composite, SWT.PUSH);
		zoomOutBtn.setText("zoomOut(-)");
		Button saveModifiedSvgBtn = new Button(composite, SWT.PUSH);
		saveModifiedSvgBtn.setText("save");

		Composite composite2 = new Composite(parents, SWT.NONE);
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite2.setLayout(new GridLayout(1, false));
		
		DevicePanelSvg deviceSvg = new DevicePanelSvg(composite2, SWT.NONE);
//		deviceSvg.setBounds(20, 0, 1000, 600);
		String sysObjId = "svg07"; //svg file name.
		deviceSvg.addOneSvgPanelById(sysObjId);
		deviceSvg.setLayoutData(new GridData(GridData.FILL_BOTH));

		deviceSvg.addListener(SWT.Selection, new Listener() {
			private static final long serialVersionUID = 1L;
			public void handleEvent(Event event) {
				String eventag = event.text;
				if (eventag.equals("openport")) {
//					MsgBox.ShowError("打开端口！");
				} else if (eventag.equals("deviceip")) {
//					MsgBox.ShowError("查看端口连接的设备！");
				}else if(eventag.toLowerCase().equals("portmenu")){
					String networkNodeName = (String) event.data;
					Shell shell = deviceSvg.getShell();
					Menu menu = new Menu(shell,SWT.POP_UP);
					shell.setMenu(menu);
					menu.setLocation(event.x, event.y);
					menu.setVisible(true);
					MenuItem portProperties = new MenuItem(menu, SWT.PUSH);
					portProperties.setText("端口属性");
					portProperties.addSelectionListener(new SelectionAdapter() {
						private static final long serialVersionUID = 1L;
						@Override
						public void widgetSelected(SelectionEvent e) {
							PortPropertiesDlg portDlg = new PortPropertiesDlg("huawei", "xiaomi");
							portDlg.setInterfaceNames(createInterfaceNameArr(50));
							portDlg.setInterfaceName(networkNodeName);
							portDlg.open();
						}
					});
				}else if(eventag.toLowerCase().equals("svg_initialized")){
					System.out.println("svg_initialized");
					deviceSvg.setStatuss(createStatusMap(50));
					deviceSvg.setTooltipdata(createTooltipMap(50));
				}else if(eventag.toLowerCase().equals("svgtxtchanged")){
					System.out.println("svgtxtchanged....");
					System.out.println("svgtxtchanged,data:"+event.data);
				}
			}
		});

		parent.getDisplay().timerExec(4000, new Runnable() { //refresh data after 2s later.
			public void run() {
				deviceSvg.setStatuss(createStatusMap(50));
				deviceSvg.setTooltipdata(createTooltipMap(50));
				deviceSvg.setInterfaceNameList(createInterfaceNameArr(50));
			}
		});


		button.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unused")
				JsonObject tempSize = deviceSvg.getSvgSize();
				deviceSvg.refreshAll(createStatusMap(50),createTooltipMap(50));

			}
		});
		zoomInBtn.addSelectionListener(new SelectionAdapter() { //zoom in (+)
			private static final long serialVersionUID = 1L;
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
			private static final long serialVersionUID = 1L;

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
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				String svgTxt = deviceSvg.getNewSvgTxt();
				try {
					FileUtils.writeStringToFile(new File("D:/liuyaoao/132.txt"), svgTxt,"utf-8");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				System.out.println("modified svgtxt:"+svgTxt);
			}
		});
	}

	public static String getRandom(int t){
		int i = (int) (Math.random()*t);
		String s = (i<10?"0"+i:i+"");
		return s;
	}
	private JsonObject createStatusMap(int num){
		JsonObject statusMap = new JsonObject();
		for(int i=0;i<num;i++){
			statusMap.set("Ethernet0/"+(i+1), getRangeRandomNum(0,5)); //键值为端口名称，也就是接口名。
		}
		return statusMap;
	}
	private JsonObject createTooltipMap(int num){
		JsonObject tooltipMap = new JsonObject();
		for(int i=0;i<num;i++){ //键值为端口名称，也就是接口名。
			tooltipMap.set("Ethernet0/"+(i+1), "端口信息<br>端口类型：p1<br>端口索引：p2<br>端口描述：p3<br>接口索引：p4<br>端口状态：p5<br>管理状态：p6<br>接收流量：p7<br>发送流量：p8<br>速率   ：p9");
		}
		return tooltipMap;
	}
	private String[] createInterfaceNameArr(int num){
		String[] nameArr = new String[num];
		for(int i=0;i<num;i++){
			nameArr[i] = "Ethernet0/"+(i+1);
		}
		return nameArr;
	}
	private int getRangeRandomNum(int min, int max){
		return (new Random().nextInt(max - min) + min);
	}

}
