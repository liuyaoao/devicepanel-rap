package com.weadmin.devicepanel_rap.example;

import java.util.Random;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.weadmin.devicepanel_rap.DevicePanelSvg;


public class DevicePanel extends Dialog {

	private int width = 600;
	private int height = 700;
	private Shell shell = null;
	String nodeid = "";
	String sysObjId = "svg05"; //svg file name.

	int[] statuss = null;
	String[] tooltips = null;
	private String portkey="";
	private String commm ="public";
	int rowCount=0;
	int models = 0;
	int svhide = 0;
	int rr=-1;

	/**
	 * Create the dialog.
	 *
	 * @param parentShell
	 */
	public DevicePanel(Shell parentShell, String nodeid, String lab) {
		super(parentShell);
		this.nodeid = nodeid;
		setShellStyle(SWT.MAX | SWT.RESIZE);

	}

	public DevicePanel(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE);

	}

	protected void configureShell(Shell newShell) {
		// newShell.setSize(450, 320);
		// newShell.setLocation(400, 175);
		newShell.setText("面板图("+this.sysObjId+")");
		super.configureShell(newShell);
		shell = newShell;
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		DevicePanelSvg deviceSvg = new DevicePanelSvg(parent, SWT.NONE);
		deviceSvg.setStatuss(createStatusArr(50));
		deviceSvg.setTooltipdata(createTooltipArr(50));
		deviceSvg.addOneSvgPanel(sysObjId);

		deviceSvg.setLayoutData(new GridData(GridData.CENTER));
		
		parent.getDisplay().timerExec(1000, new Runnable() {
			public void run() {
				width = (int)(deviceSvg.getWidth());
				height = (int)(deviceSvg.getHeight());
				deviceSvg.setLayoutData(new GridData(width+10, height+50));
				System.out.println("width:"+width);
				System.out.println("height:"+height);
				shell.setSize(new Point(width+10,height+50));
			}
		});
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
//					MsgBox.ShowError("点击了端口！");
				}
			}
	});
	return deviceSvg;
}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	}
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(width, height);
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
