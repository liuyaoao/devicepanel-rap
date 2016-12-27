package com.weadmin.devicepanel_rap.example;


import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ExampleTwo extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new GridLayout(5,false));
		Button button = new Button(parent, SWT.PUSH);
		button.setText("refresh");
		Button zoomInBtn = new Button(parent, SWT.PUSH);
		zoomInBtn.setText("zoomIn(+)");
		Button zoomOutBtn = new Button(parent, SWT.PUSH);
		zoomOutBtn.setText("zoomOut(-)");
		
		Combo combo = new Combo(parent, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		combo.setItems(new String[]{"svg011","svg00","svg01","svg02","svg03","svg04","svg05"});
		combo.select(6);
		
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
		
		openPanelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = combo.getText();
				DevicePanel devicepanel = new DevicePanel(parent.getShell(),text);
				zoomInBtn.addSelectionListener(new SelectionAdapter() { //zoom in (+)
					@Override
					public void widgetSelected(SelectionEvent e) {
						devicepanel.zommIn();
					}
				});
				zoomOutBtn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						devicepanel.zommOut();
					}
				});
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
