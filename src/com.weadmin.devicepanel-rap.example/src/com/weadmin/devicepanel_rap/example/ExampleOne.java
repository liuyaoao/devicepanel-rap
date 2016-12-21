package com.weadmin.devicepanel_rap.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
		String sysoid = "";
		deviceSvg.addOneSvgPanel(sysoid);
		deviceSvg.setLayoutData(new GridData(GridData.FILL_BOTH));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList list = new ArrayList();
				//TODO
			}
		});
	}

	public static String getRandom(int t){
		int i = (int) (Math.random()*t);
		String s = (i<10?"0"+i:i+"");
		return s;
	}
	private void readTxtFile(String filePath){
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ�����
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//���ǵ�������ʽ
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        System.out.println(lineTxt);
                    }
                    read.close();
		        }else{
		            System.out.println("�Ҳ���ָ�����ļ�-example");
		        }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݳ���-example");
            e.printStackTrace();
        }

    }

}
