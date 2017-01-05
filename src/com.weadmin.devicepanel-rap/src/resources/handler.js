/*****************************
 *date: 2016/12/15
 *auther: yaoao.liu
 *svg to rap
 *******************************/
var DEVICEPANEL_RAP_BASEPATH = "rwt-resources/devicepanelsvgjs/";
(function() {
	'use strict';
	if (!window.svgdevicepanel) {
		window.svgdevicepanel = {};
	}
	rap.registerTypeHandler("svgdevicepanel.devicepanelsvgjs", {
		factory : function(properties) {
			return new svgdevicepanel.devicepanelsvgjs(properties);
		},
		destructor : "destroy",
		methods: ["refreshAll","refreshSize","getModifiedSvgTxt"],
		properties : ["svgSize", "spacing", "statuss", "interfaceNameList", "menudesc", "tooltipdata","svgTxt"],
		events : ["Selection"]
	});

	svgdevicepanel.devicepanelsvgjs = function(properties) {
		this._parent = rap.getObject(properties.parent);
		bindAll(this, [ "resizeLayout", "onSend", "onRender","refreshSize","portBeSelected","getSizeFromSvg","svgInitializedCall","updateContainerSize"]);
		this.element = document.createElement("div");
		this.element.style.position = 'absolute';
		this.element.style.top = '0';
		this.element.style.left = '0';
		this.element.style.overflow = 'auto';
		// this.element.style.width = '100%';
		// this.element.style.height = '100%';
		this._parent.append(this.element);
		this._parent.addListener("Resize", this.resizeLayout);
		this.ready = false;
		this._svgTxt = "";
		var area = this._parent.getClientArea();
		this._svgSize = {width:area[2]||300,height:area[3]||300};

		this._updatedata = true;
		this._tooltipdesc = "";
		this._menudesc = "";
		this._tooltipDataMap = {};
		this._interfaceNameList = [];
		this._selectnodeid = "";
		this._statussMap = {}; //指示灯的状态: 0down 1 up 2  Testing 3 Alarm 4 Other  5 Unknown
		this._uniqueId = Math.random().toString(36).split(".")[1];
		rap.on("render", this.onRender);

	};
	svgdevicepanel.devicepanelsvgjs.prototype = {
		onRender : function() {
      var _this = this;
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				// Creates the graph inside the given container
				this.menuPanel = new svgdevicepanel.MenuPanel({
					container:this.element,
					uniqueId:this._uniqueId,
					menuDesc:this._menudesc,
					clickMenuCall:function(eventName,svid){
						_this.clickMenuCall(eventName,svid);
					}
				});
				this.portNameDialog = new svgdevicepanel.PortNameDialog({
					container:this.element,
					uniqueId:this._uniqueId
				});
				this.svgChartPanel = new svgdevicepanel.SvgChartPanel({
					container:this.element,
					uniqueId:this._uniqueId,
					menuPanel:this.menuPanel,
					portNameDialog:this.portNameDialog,
					svgTxt:this._svgTxt,
					statusMap:this._statussMap,
					tooltipDataMap:this._tooltipDataMap,
					interfaceNameList:this._interfaceNameList,
					getModifiedSvgTxtCall:function(svgtxt){
						_this.getModifiedSvgTxt(svgtxt);
					},
					portBeSelectedCall:function(eventName,svid){
						_this.portBeSelected(eventName,svid);
					}
				});
				this.getSizeFromSvg();
				this.svgInitializedCall();
				// setTimeout(function(){
				// 	_this.refreshAll();
				// }, 100);
				setTimeout(function(){ _this.updateContainerSize(); }, 200);
				rap.getRemoteObject( this ).set( "svgSize", JSON.stringify(this._svgSize));
				rap.on("send", this.onSend);
				this.ready = true;
			}
		},

		destroy : function () {
			rap.off("send", this.onSend);
			this.menuPanel && this.menuPanel.dispose();
			this.svgChartPanel && this.svgChartPanel.dispose();
			(this.element && this.element.parentNode) ? this.element.parentNode.removeChild(this.element): null;
		},
		onSend : function() {
			// rap.getRemoteObject( this ).set( "model", "123456789"); //设置后端的值，还有其他两个方法:call(method,properties):调用后端的方法,notify(event,properties);
			// rap.getRemoteObject( this ).call( "handleCallRefreshData", "123456789"); //设置后端的值，还有其他两个方法:call(method,properties):调用后端的方法,notify(event,properties);
		},
		clickMenuCall:function(eventName,selectedNodeId){
			this._selectnodeid = selectedNodeId;
			this.portBeSelected(eventName, selectedNodeId);
		},
		setMenudesc : function (menudesc) {
			this._menudesc = menudesc;
		},
		getMenudesc : function () {
			return this._menudesc;
		},
		setInterfaceNameList : function (interfaceNameList) {
			this._interfaceNameList = interfaceNameList;
			this.svgChartPanel && this.svgChartPanel.updateInterfaceName(this._interfaceNameList);
		},
		setStatuss : function (statuss) {
			this._statussMap = statuss;
			this.svgChartPanel && this.svgChartPanel.updateStatus(this._statussMap);
		},
		setTooltipdata : function (tooltipdata) {
			this._tooltipDataMap = tooltipdata;
			this.svgChartPanel && this.svgChartPanel.updateTooltip(this._tooltipDataMap);
		},
		setSvgSize:function(svgSize){
			this._svgSize = svgSize || "";
		},
		setSvgTxt:function(svgTxt){
			this._svgTxt = svgTxt || "";
		},
		getModifiedSvgTxt:function(svgtxt){
			this._svgTxt = svgtxt;//this.svgChartPanel.getModifiedSvgTxt();
			rap.getRemoteObject( this ).set( "svgTxt", this._svgTxt);
		},
		getSizeFromSvg:function(){
			this._svgSize = this.svgChartPanel.getSize();
		},
		refreshAll:function(){ //更新所有显示。状态和提示。
			// console.log('refreshAll!!!!!!!!!!');
			var _this = this;
			setTimeout(function(){
				_this.svgChartPanel.updateStatus(_this._statussMap);
				_this.svgChartPanel.updateTooltip(_this._tooltipDataMap);
			},10);
		},
		// 当对端口有任何操作时触发服务端更新。svid 也就是nodeid,也即端口名（接口名）
		portBeSelected : function (eventName, svid) {
			switch(eventName){
				case "portport":
					console.log("端口( "+svid+" )被点击，查看端口详情！");
					break;
				case "openport":
					console.log("打开( "+svid+" )端口！");
					break;
				case "closeport":
					console.log("关闭( "+svid+" )端口！");
					break;
				case "deviceip":
					console.log("查看当前端口( "+svid+" )连接设备！");
					break;
				case "":
					console.log("不知道点了哪里了！");
					break;
			}
			var remoteObject = rap.getRemoteObject(this);
			remoteObject.notify("Selection", {
				"index" : eventName,
				"data" : svid
			});
		},
		svgInitializedCall:function(){
			var remoteObject = rap.getRemoteObject(this);
			remoteObject.notify("Selection", {
				"index" : "svg_initialized",
				"data" : -1
			});
		},
		// 大小自适应
		resizeLayout : function() {
			if (this.ready) {
				var area = this._parent.getClientArea();
				 this.updateContainerSize();
				// console.log("resizeLayout:",area);
				if(Math.abs(area[2]-this._svgSize.width)<5 && Math.abs(area[3]-this._svgSize.height)<5){ return; }
				// this.refreshSize(area[0],area[1],area[2],area[3]);
			}
		},
		refreshSize:function(obj){
			this._svgSize = {width:obj.width,height:obj.height};
			this.svgChartPanel.refreshSize(obj.width,obj.height);
		},
		updateContainerSize:function(){
			if(this.element.parentNode){
				$(this.element).width($(this.element.parentNode).width());
				$(this.element).height($(this.element.parentNode).height());
			}
		}
	};
	var bind = function(context, method) {
		return function() {
			return method.apply(context, arguments);
		};
	};
	var bindAll = function(context, methodNames) {
		for (var i = 0; i < methodNames.length; i++) {
			var method = context[methodNames[i]];
			context[methodNames[i]] = bind(context, method);
		}
	};
	var async = function(context, func) {
		window.setTimeout(function() {
			func.apply(context);
		}, 0);
	};
	var randomNumBoth = function(Min,Max){
      var Range = Max - Min;
      var Rand = Math.random();
      var num = Min + Math.round(Rand * Range); //四舍五入
      return num;
	};

}());
