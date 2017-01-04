package com.weadmin.devicepanel_rap.example;

import java.util.Random;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.weadmin.devicepanel_rap.DevicePanelSvg;


public class DevicePanel extends Dialog {

	private static final long serialVersionUID = 1L;
	private int width = 400;
	private int height = 300;
	private Shell shell = null;
	String nodeid = "";
	String sysObjId = "svg06"; //svg file name.
	DevicePanelSvg deviceSvg;

	int[] statuss = null;
	String[] tooltips = null;
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

	public DevicePanel(Shell parentShell,String sysObjId) {
		super(parentShell);
		this.sysObjId = sysObjId;
		setShellStyle(SWT.MAX | SWT.RESIZE);

	}

	protected void configureShell(Shell newShell) {
		// newShell.setSize(450, 320);
		// newShell.setLocation(400, 175);
		newShell.setText("面板图("+this.sysObjId+")");
		super.configureShell(newShell);
		shell = newShell;
	}

	public void zommIn(){
		JsonObject tempSize = deviceSvg.getSvgSize();
		double valWidth = tempSize.get("width").asDouble();
		double valHeight = tempSize.get("height").asDouble();
		tempSize.set("width",(valWidth*1.1));
		tempSize.set("height",(valHeight*1.1));
		deviceSvg.refreshSize(tempSize);
		deviceSvg.setLayoutData(new GridData((int)(valWidth*1.1), (int)(valHeight*1.1)));
		shell.pack();
	}

	public void zommOut(){
		JsonObject tempSize = deviceSvg.getSvgSize();
		double valWidth = tempSize.get("width").asDouble();
		double valHeight = tempSize.get("height").asDouble();
		tempSize.set("width",(valWidth*0.9));
		tempSize.set("height",(valHeight*0.9));
		deviceSvg.refreshSize(tempSize);
		deviceSvg.setLayoutData(new GridData((int)(valWidth*0.9), (int)(valHeight*0.9)));
		shell.pack();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(0, 0, 0, -30).spacing(0, 0).applyTo(parent);
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
		deviceSvg = new DevicePanelSvg(parent, SWT.NONE);
		deviceSvg.setStatuss(createStatusMap(50));
		deviceSvg.setTooltipdata(createTooltipMap(50));
		deviceSvg.addOneSvgPanelById(sysObjId);

		parent.getDisplay().timerExec(100, new Runnable() {
			public void run() {
				width = (int)(deviceSvg.getWidth());
				height = (int)(deviceSvg.getHeight());
				deviceSvg.setLayoutData(new GridData(width, height));
				System.out.println("width:"+width + "--" +"height:"+height);
				shell.pack();
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
				}else if(eventag.toLowerCase().equals("svg_initialized")){
					System.out.println("svg_initialized");
				}
			}
		});
		parent.getDisplay().timerExec(1000, new Runnable() {
			public void run() {
				parent.addControlListener(new ControlListener() {

					private static final long serialVersionUID = 1L;
					@Override
					public void controlResized(ControlEvent e) {
						Point size = parent.getSize();
						double shellx = size.x;
						double shelly = size.y;
						JsonObject tempSize = deviceSvg.getSvgSize();
						double svgx = deviceSvg.getWidth();
						double svgy = deviceSvg.getHeight();
						if(((int)shellx>(int)svgx&&(int)shelly==(int)svgy) ||
								((int)shelly>=(int)svgy&&(int)shellx==(int)svgx) ||
								((int)shellx>(int)svgx&&(int)shelly>=(int)svgy)){
							double xx = shellx/svgx;
							double yy = shelly/svgy;
							double value = Math.max(xx, yy);
							tempSize.set("width",(svgx*value) );
							tempSize.set("height",(svgy*value));
							deviceSvg.refreshSize(tempSize);
							deviceSvg.setLayoutData(new GridData((int)(svgx*value), (int)(svgy*value)));
							shell.pack();
						}else if(((int)shellx<(int)svgx&&(int)shelly==(int)svgy) ||
								((int)shelly<(int)svgy&&(int)shellx==(int)svgx) ||
								((int)shelly<(int)svgy&&(int)shellx<(int)svgx)){
							double xx = shellx/svgx;
							double yy = shelly/svgy;
							double value = Math.min(xx, yy);
							tempSize.set("width",(svgx*value) );
							tempSize.set("height",(svgy*value));
							deviceSvg.refreshSize(tempSize);
							deviceSvg.setLayoutData(new GridData((int)(svgx*value), (int)(svgy*value)));
							shell.pack();
						}
					}
					@Override
					public void controlMoved(ControlEvent e) {
					}
				});
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

	private JsonObject createStatusMap(int num){
		JsonObject statusMap = new JsonObject();
		for(int i=0;i<num;i++){
			statusMap.set(""+(i+1), getRangeRandomNum(0,5));
		}
		return statusMap;
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
