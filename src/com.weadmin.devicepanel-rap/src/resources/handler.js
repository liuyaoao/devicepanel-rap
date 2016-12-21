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
		properties : ["spacing", "statuss", "tooltipdesc", "menudesc", "tooltipdata","svgTxt"],
		events : ["Selection"]
	});

	svgdevicepanel.devicepanelsvgjs = function(properties) {
		this._parent = rap.getObject(properties.parent);
		bindAll(this, [ "resizeLayout", "onReady", "onSend", "onRender","refreshSize","portBeSelected"]);
		this.element = document.createElement("div");
		this.element.style.width = '100%';
		this.element.style.height = '100%';
		this._parent.append(this.element);
		this._parent.addListener("Resize", this.resizeLayout);
		this.ready = false;
		this._svgTxt = "";
		var area = this._parent.getClientArea();
		this._size = {width:area[2]||300,height:area[3]||300};

		this._updatedata = true;
		this._tooltipdesc = "";
		this._menudesc = "";
		this._tooltipdata = [];
		this._selectnodeid = "";
		this._statuss = []; //指示灯的状态: 0down 1 up 2  Testing 3 Alarm 4 Other  5 Unknown
		rap.on("render", this.onRender);

	};
	svgdevicepanel.devicepanelsvgjs.prototype = {
		onReady : function() {
			this.ready = true;
			// this.resizeLayout();
			console.log("svgdevicepanel.devicepanelsvgjs is on ready!!");
		},
		onRender : function() {
      var _this = this;
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				// Creates the graph inside the given container
				this.menuPanel = new svgdevicepanel.MenuPanel({
					container:this.element,
					menuDesc:this._menudesc,
					clickMenuCall:this.clickMenuCall
				});
				this.svgChartPanel = new svgdevicepanel.SvgChartPanel({
					container:this.element,
					menuPanel:this.menuPanel,
					svgTxt:this._svgTxt,
					portBeSelectedCall:this.portBeSelected,
					tooltipdesc:this._tooltipdesc
				});
				rap.on("send", this.onSend);
				this.ready = true;
			}
		},

		destroy : function () {
			// this._svgMap && this._svgMap.destroy();
			rap.off("send", this.onSend);
			(this.element && this.element.parentNode) ? this.element.parentNode.removeChild(this.element): null;
		},
		onSend : function() {
			// rap.getRemoteObject( this ).set( "model", "123456789"); //设置后端的值，还有其他两个方法:call(method,properties):调用后端的方法,notify(event,properties);
			// rap.getRemoteObject( this ).call( "handleCallRefreshData", "123456789"); //设置后端的值，还有其他两个方法:call(method,properties):调用后端的方法,notify(event,properties);
		},
		clickMenuCall:function(eventName){
			this.portBeSelected(eventName, that._selectnodeid);
		},
		setTooltipdesc : function (tooltipdesc) {
			this._tooltipdesc = tooltipdesc;
		},
		getTooltipdesc : function () {
			return this._tooltipdesc;
		},
		setMenudesc : function (menudesc) {
			this._menudesc = menudesc;
		},
		getMenudesc : function () {
			return this._menudesc;
		},
		setStatuss : function (statuss) {
			this._statuss = statuss;
			this._updateStatus(statuss);
		},
		setTooltipdata : function (tooltipdata) {
			this._tooltipdata = tooltipdata;
			var that = this;
			tooltipdata.map(function (value, index) {
				var nindex = index + 1;
				var nid = "#c" + nindex;
				var selection1 = d3.select(nid);
				selection1.text("");
				var text2 = that.getTooltipdesc();
				if (value != null) {
					var values = value.split(":");
					if (values.length > 0) {
						var text3 = text2.replace('p1', values[0])
							.replace('p2', values[1])
							.replace('p3', values[2])
							.replace('p4', values[3])
							.replace('p5', values[4])
							.replace('p6', values[5])
							.replace('p7', values[6])
							.replace('p8', values[7])
							.replace('p9', values[8]);
						selection1.append("svg:title").text(text3);
					}
				}
			});
		},
		_updateStatus : function (statuss) {
			this._updateTimer1.setEnabled(false);
			statuss.map(function (value, index) {
				var nindex = index + 1;
				var selection1;
				var sid = "#pl" + nindex;
				var sid1 = "#p" + nindex;
				selection1 = d3.select(sid);
				selection2 = d3.select(sid1);
				if (value == 0) {
					selection1.attr("fill", "#d6d6d6"); //gray
					selection2.attr("fill", "#d6d6d6"); //gray
				} else if (value == 1) {
					selection1.attr("fill", "#006400"); //green #00FF00 lightgreen
					selection1.attr("class", "up1");
					selection2.attr("fill", "#00FF00"); //
				} else if (value == 2) {
					selection1.attr("fill", "#FFFF00"); //yellow
					selection2.attr("fill", "#FFFF00");
				} else if (value == 3) {
					selection1.attr("fill", "#FF0000"); //red
					selection2.attr("fill", "#FF0000");
				} else if (value == 4) {
					selection1.attr("fill", "#0000FF"); //blue
					selection2.attr("fill", "#0000FF");
				} else if (value == 5) {
					selection1.attr("fill", "#FFA500"); //orange
					selection2.attr("fill", "#FFA500");
				} else {
					selection1.attr("fill", "#d6d6d6"); ////gray#808080
					selection2.attr("fill", "#d6d6d6");
				}
			});
			this._updateTimer1.setEnabled(true);
		},
		setSvgTxt:function(svgTxt){
			this._svgTxt = svgTxt || "";
		},


		_updatePaths : function (selection) {
			var that = this;
			selection
			.transition()
			.duration(1000)
			.attr("d", function (item) {
				return item.getValue();
			});
		},
		_updateGroup : function (selection) {
			var that = this;
			selection
			.transition()
			.duration(1000)
			.attr("transform", function (item) {
				return item.getTransform();
			});
		},
		_removeElements : function (selection) {
			selection
			.transition()
			.duration(400)
			.attr("opacity", 0.0)
			.remove();
		},
		// 当对端口有任何操作时触发服务端更新。svid 也就是nodeid
		portBeSelected : function (eventName, svid) {
			var remoteObject = rap.getRemoteObject(this);
			remoteObject.notify("Selection", {
				"index" : eventName,
				"data" : svid
			});
		},
		// 大小自适应
		resizeLayout : function() {
			if (this.ready) {
				var area = this._parent.getClientArea();
				if(Math.abs(area[2]-this._size.width)<5 && Math.abs(area[3]-this._size.height)<5){ return; }
				this.refreshSize(area[0],area[1],area[2],area[3]);
			}
		},
		refreshSize:function(left,top,width,height){
			this._size = {width:width,height:height};
			this.svgChartPanel.refreshSize(this._size);
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

}());
