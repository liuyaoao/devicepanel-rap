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

public class ExampleTwo extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new GridLayout());
//		parent.setLayout(null);
		Button button = new Button(parent, SWT.PUSH);
		button.setText("refresh");
		Button zoomInBtn = new Button(parent, SWT.PUSH);
		zoomInBtn.setText("zoomIn(+)");
		Button zoomOutBtn = new Button(parent, SWT.PUSH);
		zoomOutBtn.setText("zoomOut(-)");
		Button openPanelBtn = new Button(parent, SWT.PUSH);
		openPanelBtn.setText("openPanel");
		//add event listeners
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				JsonObject tempSize = deviceSvg.getSvgSize();
//				deviceSvg.refreshAll(createStatusArr(50),createTooltipArr(50));

			}
		});
		zoomInBtn.addSelectionListener(new SelectionAdapter() { //zoom in (+)
			@Override
			public void widgetSelected(SelectionEvent e) {
//				JsonObject tempSize = deviceSvg.getSvgSize();
//				double valWidth = tempSize.get("width").asDouble();
//				double valHeight = tempSize.get("height").asDouble();
//				tempSize.set("width",valWidth*1.1 );
//				tempSize.set("height",valHeight*1.1 );
//				deviceSvg.refreshSize(tempSize);
			}
		});
		zoomOutBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				JsonObject tempSize = deviceSvg.getSvgSize();
//				double valWidth = tempSize.get("width").asDouble();
//				double valHeight = tempSize.get("height").asDouble();
//				tempSize.set("width",valWidth*0.9 );
//				tempSize.set("height",valHeight*0.9 );
//				deviceSvg.refreshSize(tempSize);
			}
		});
		openPanelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DevicePanel devicepanel = new DevicePanel(parent.getShell());
				devicepanel.open();
			}
		});
	}

	public static String getRandom(int t){
		int i = (int) (Math.random()*t);
		String s = (i<10?"0"+i:i+"");
		return s;
	}


}
