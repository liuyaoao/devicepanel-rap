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

		DevicePanelSvg deviceSvg = new DevicePanelSvg(parent, SWT.NONE);
//		deviceSvg.setBounds(20, 0, 1000, 600);
		String sysObjId = "svg01"; //svgÎÄ¼þÃû¡£
		deviceSvg.setStatuss(createStatusArr(50));
		deviceSvg.setTooltipdata(createTooltipArr(50));

		deviceSvg.addOneSvgPanel(sysObjId);
		deviceSvg.setLayoutData(new GridData(GridData.FILL_BOTH));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				deviceSvg.refreshAll(createStatusArr(50),createTooltipArr(50));

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
			statusArr[i] = getRangeRandomNum(0,4);
		}
		return statusArr;
	}
	private String[] createTooltipArr(int num){
		String[] tooltipArr = new String[num];
		for(int i=0;i<num;i++){
			tooltipArr[i] = getRangeRandomNum(0,4)+"";
		}
		return tooltipArr;
	}
	private int getRangeRandomNum(int min, int max){
		return (new Random().nextInt(max - min) + min);
	}

}
