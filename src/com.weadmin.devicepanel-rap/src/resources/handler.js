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
		methods: ["refreshAll","refreshSize"],
		properties : ["svgSize", "spacing", "statuss", "menudesc", "tooltipdata","svgTxt"],
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
		this._tooltipDataArr = [];
		this._selectnodeid = "";
		this._statuss = []; //指示灯的状态: 0down 1 up 2  Testing 3 Alarm 4 Other  5 Unknown
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
				this.svgChartPanel = new svgdevicepanel.SvgChartPanel({
					container:this.element,
					uniqueId:this._uniqueId,
					menuPanel:this.menuPanel,
					svgTxt:this._svgTxt,
					statusArr:this._statuss,
					tooltipDataArr:this._tooltipDataArr,
					portBeSelectedCall:function(eventName,svid){
						_this.portBeSelected(eventName,svid);
					}
				});
				this.getSizeFromSvg();
				this.svgInitializedCall();
				// console.log("this._svgSize:----",this._svgSize);
				setTimeout(function(){
					_this.refreshAll();
				}, 100);
				setTimeout(function(){ _this.updateContainerSize(); }, 200);
				rap.getRemoteObject( this ).set( "svgSize", JSON.stringify(this._svgSize));
				rap.on("send", this.onSend);
				this.ready = true;
			}
		},

		destroy : function () {
			// this._svgMap && this._svgMap.destroy();
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
		setStatuss : function (statuss) {
			// console.log('statusArr:',statuss);
			this._statuss = statuss;
		},
		setTooltipdata : function (tooltipdata) {
			this._tooltipDataArr = tooltipdata;
		},
		setSvgSize:function(svgSize){
			this._svgSize = svgSize || "";
		},
		setSvgTxt:function(svgTxt){
			this._svgTxt = svgTxt || "";
		},
		getSizeFromSvg:function(){
			this._svgSize = this.svgChartPanel.getSize();
		},
		refreshAll:function(){ //更新所有显示。状态和提示。
			// console.log('refreshAll!!!!!!!!!!');
			this.svgChartPanel.updateStatus(this._statuss);
			this.svgChartPanel.updateTooltip(this._tooltipDataArr);
		},
		// 当对端口有任何操作时触发服务端更新。svid 也就是nodeid
		portBeSelected : function (eventName, svid) {
			// switch(eventName){
			// 	case "portport":
			// 		alert("端口被点击，查看端口详情！");
			// 		break;
			// 	case "openport":
			// 		alert("打开端口！");
			// 		break;
			// 	case "closeport":
			// 		alert("关闭端口！");
			// 		break;
			// 	case "deviceip":
			// 		alert("查看当前端口连接设备！");
			// 		break;
			// 	case "":
			// 		alert("不知道点了哪里了！");
			// 		break;
			// }
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
