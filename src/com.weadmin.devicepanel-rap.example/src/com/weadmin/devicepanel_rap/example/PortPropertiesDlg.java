package com.weadmin.devicepanel_rap.example;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;


public class PortPropertiesDlg extends Dialog{
	private static final long serialVersionUID = 1L;

	private String[] interfaceNames;

	private String networkNodeName;

	private String serverNodeName;

	private String interfaceName;

	private Combo interfaceCombo;

	private Spinner spokflow;
	private Label lbmidflow;
	private Spinner sperrorflow;

	private Spinner spokpkts;
	private Label lbmidpkts;
	private Spinner sperrorpkts;

	private Spinner spokbroad;
	private Label lbmidbroad;
	private Spinner sperrorbroad;

	private Spinner spokPercent;
	private Label lbmidPercent;// Percent
	private Spinner sperrorPercent;

	private int warnflow = 50;
	private int errorflow = 2000;
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("端口属性");
	}
	
	public void setInterfaceNames(String[] interfaceNames){
		this.interfaceNames = interfaceNames;
	}

	public String getInterfaceName() {
		return this.interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	protected PortPropertiesDlg(String networkNodeName, String serverNodeName) {
		super(Display.getCurrent().getActiveShell());
		this.networkNodeName = networkNodeName;
		this.serverNodeName = serverNodeName;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(null);

		Group group = new Group(content, SWT.NONE);
		group.setText("基本信息");
		group.setBounds(10, 0, 374, 142);

		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setBounds(10, 35, 90, 17);
		lblNewLabel_2.setText("被连接的设备:");

		Label lblServerName = new Label(group, SWT.NONE);
		lblServerName.setBounds(106, 35, 239, 17);
		lblServerName.setText(serverNodeName);

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(10, 69, 90, 17);
		lblNewLabel_1.setText("网络设备名称:");

		Label lblNetWorkName = new Label(group, SWT.NONE);
		lblNetWorkName.setBounds(106, 69, 239, 17);
		lblNetWorkName.setText(networkNodeName);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(10, 109, 90, 17);
		lblNewLabel.setText("网络接口名称:");

		interfaceCombo = new Combo(group, SWT.NONE);
		interfaceCombo.setBounds(106, 106, 239, 25);

		Group composite = new Group(content, SWT.NONE);
		composite.setText("阀值信息");
		composite.setBounds(10, 145, 374, 200);

		composite.setLayout(new GridLayout(3, false));
		Label lbflow = new Label(composite, SWT.NONE);
		lbflow.setText("总流量(Kbps)");
		Label lbokflow = new Label(composite, SWT.NONE);
		GridData gd_okflow = new GridData(GridData.FILL_HORIZONTAL);
		gd_okflow.widthHint = 80;
		lbokflow.setLayoutData(gd_okflow);
		lbokflow.setText("<span style=\"width:90px; height:22px; background-color:green;display:block;\"></span>");
		lbokflow.setData(RWT.MARKUP_ENABLED, true);

		spokflow = new Spinner(composite, SWT.BORDER);
		spokflow.setMaximum(80000000);
		spokflow.setSelection(warnflow);
		spokflow.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spokflow.addSelectionListener(new SelectionAdapter() {

			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				flowEvent();
			}
		});
		Label lbflow1 = new Label(composite, SWT.NONE);
		lbflow1.setText("  ");
		Label lbwarnflow = new Label(composite, SWT.NONE);
		gd_okflow = new GridData(GridData.FILL_HORIZONTAL);
		gd_okflow.widthHint = 80;
		lbwarnflow.setLayoutData(gd_okflow);
		lbwarnflow.setText("            ");
		lbwarnflow.setText("<span style=\"width:90px; height:22px; background-color:yellow;display:block;\"></span>");
		lbwarnflow.setData(RWT.MARKUP_ENABLED, true);
		lbmidflow = new Label(composite, SWT.NONE);
		lbmidflow.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lbflow2 = new Label(composite, SWT.NONE);
		lbflow2.setText("");
		Label lberrorflow = new Label(composite, SWT.NONE);
		gd_okflow = new GridData(GridData.FILL_HORIZONTAL);
		gd_okflow.widthHint = 80;
		lberrorflow.setLayoutData(gd_okflow);
		lberrorflow.setText("<span style=\"width:90px; height:22px; background-color:red;display:block;\"></span>");
		lberrorflow.setData(RWT.MARKUP_ENABLED, true);
		sperrorflow = new Spinner(composite, SWT.BORDER);
		sperrorflow.setMaximum(900000000);
		sperrorflow.setSelection(errorflow);
		sperrorflow.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sperrorflow.addSelectionListener(new SelectionAdapter() {

			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				flowEvent();
			}
		});
		lbmidflow.setText("> " + spokflow.getText() + " && < " + sperrorflow.getText());

		Label downf = new Label(composite, SWT.NONE);
		downf.setText("");
		Label down = new Label(composite, SWT.NONE);
		GridData gd_down = new GridData(GridData.FILL_HORIZONTAL);
		gd_down.widthHint = 80;
		down.setLayoutData(gd_down);
		down.setText("<span style=\"width:90px; height:22px; background-color:black;display:block;\"></span>");
		down.setData(RWT.MARKUP_ENABLED, true);
		Label downtext = new Label(composite, SWT.NONE);
		downtext.setText("down");
		downtext.setLayoutData(gd_down);
		
		Label unbindf = new Label(composite, SWT.NONE);
		unbindf.setText("");
		Label unbind = new Label(composite, SWT.NONE);
		GridData gd_unbind = new GridData(GridData.FILL_HORIZONTAL);
		gd_unbind.widthHint = 80;
		unbind.setLayoutData(gd_unbind);
		unbind.setText("<span style=\"width:90px; height:22px; background-color:gray;display:block;\"></span>");
		unbind.setData(RWT.MARKUP_ENABLED, true);
		Label unbindtext = new Label(composite, SWT.NONE);
		unbindtext.setText("未绑定");
		unbindtext.setLayoutData(gd_unbind);

		return content;
	}
	
	public void flowEvent(){
		lbmidflow.setText("> " + spokflow.getText() + " && < " + sperrorflow.getText());
	}
	
	public void percentEvent(){
		lbmidPercent.setText("> " + spokPercent.getText() + " && < " + sperrorPercent.getText());
	}
	
	public void broadEvent(){
		lbmidbroad.setText("> " + spokbroad.getText() + " && < " + sperrorbroad.getText());
	}
	
	public void pktsEvent(){
		lbmidpkts.setText("> " + spokpkts.getText() + " && < " + sperrorpkts.getText());
	}

	public void loadData() {
		
		if(interfaceNames!=null&&interfaceNames.length>0){
			interfaceCombo.setItems(interfaceNames);
			if (interfaceName != null && interfaceName.trim().length() > 0) {
				interfaceCombo.select(interfaceCombo.indexOf(interfaceName));
			} else {
				interfaceCombo.select(0);
			}
		}
		
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		loadData();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 450);
	}

	@Override
	protected void okPressed() {
		interfaceName = interfaceCombo.getText();
		super.okPressed();
	}
}
