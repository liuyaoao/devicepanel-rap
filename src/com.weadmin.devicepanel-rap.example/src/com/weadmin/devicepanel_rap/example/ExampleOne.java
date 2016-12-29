package com.weadmin.devicepanel_rap.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

import com.alibaba.fastjson.JSONObject;
import com.weadmin.devicepanel_rap.DevicePanelSvg;

public class ExampleOne extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new GridLayout());
//		parent.setLayout(null);
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Refresh");
		Button zoomInBtn = new Button(parent, SWT.PUSH);
		zoomInBtn.setText("zoomIn(+)");
		Button zoomOutBtn = new Button(parent, SWT.PUSH);
		zoomOutBtn.setText("zoomOut(-)");

		DevicePanelSvg deviceSvg = new DevicePanelSvg(parent, SWT.NONE);
//		deviceSvg.setBounds(20, 0, 1000, 600);
		String sysObjId = "svg01"; //svg file name.
		deviceSvg.setStatuss(createStatusMap(50));
		deviceSvg.setTooltipdata(createTooltipMap(50));
		deviceSvg.addOneSvgPanelById(sysObjId);
		deviceSvg.setLayoutData(new GridData(GridData.FILL_BOTH));
		//add number 2 svg panel
		DevicePanelSvg deviceSvg2 = new DevicePanelSvg(parent, SWT.NONE);
		String sysObjId2 = "svg06"; //svg file name.
		deviceSvg2.setStatuss(createStatusMap(50));
		deviceSvg2.setTooltipdata(createTooltipMap(50));
		deviceSvg2.addOneSvgPanelById(sysObjId2);
		deviceSvg2.setLayoutData(new GridData(GridData.FILL_BOTH));


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
			statusMap.set(""+(i+1), getRangeRandomNum(0,5));
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
		for(int i=0;i<num;i++){
			tooltipMap.set(""+(i+1), "端口信息<br>端口类型：p1<br>端口索引：p2<br>端口描述：p3<br>接口索引：p4<br>端口状态：p5<br>管理状态：p6<br>接收流量：p7<br>发送流量：p8<br>速率   ：p9");
		}
		return tooltipMap;
	}
	private int getRangeRandomNum(int min, int max){
		return (new Random().nextInt(max - min) + min);
	}

}
