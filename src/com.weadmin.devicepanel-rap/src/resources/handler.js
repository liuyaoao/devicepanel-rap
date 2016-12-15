/*****************************
 *date: 2016/12/15
 *auther: yaoao.liu
 *svg to rap
 *******************************/
var DEVICEPANEL_RAP_BASEPATH = "rwt-resources/devicepanelsvgjs/";

(function() {
	'use strict';
	if (!window.d3svgdevicepanel) {
		window.d3svgdevicepanel = {};
	}
	rap.registerTypeHandler("d3svgdevicepanel.devicepanelsvgjs", {

		factory : function(properties) {
			return new d3svgdevicepanel.devicepanelsvgjs(properties);
		},
		destructor : "destroy",

		properties : ["barWidth", "spacing", "statuss", "tooltipdesc", "menudesc", "tooltipdata"],

		events : ["Selection"]

	});


	d3svgdevicepanel.devicepanelsvgjs = function(properties) {
		var parent = rap.getObject(properties.parent);
		// bindAll(this, [ "layout", "onReady", "onSend", "onRender","refreshSize"]);
		this._barWidth = 25;
		this._spacing = 2;
		this._items = new d3svgdevicepanel.ItemList();
		this._svgMap = new d3svgdevicepanel.SvgMap(parent, this);
		this._updatedata = true;
		this._tag = 0;
		this._layer = null;
		this._tooltipdesc = "";
		this._menudesc = "";
		this._tooltipdata = [];
		this._updateTimer1 = null;
		this._selectnodeid = "";
		this._statuss = []; //指示灯的状态: 0down 1 up 2  Testing 3 Alarm 4 Other  5 Unknown
	};
	eclipsesource.devicepanelsvgjs.prototype = {
		ready : false,
		addItem : function (item) {
			this._items.add(item);
			//this._svgMap._scheduleUpdate();
			//alert("addItem");
		},

		removeItem : function (item) {
			this._items.remove(item);
			//this._svgMap._scheduleUpdate();
		},

		destroy : function () {
			this._svgMap.destroy();
		},

		initialize : function (svgMap) {
			this._svgMap = svgMap;
			this._layer = svgMap.getLayer("layer");
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
				//alert(nindex);
				// alert(value);
				var selection1;
				var sid = "#pl" + nindex;
				var sid1 = "#p" + nindex;
				// alert(this._layer);
				selection1 = d3.select(sid);
				selection2 = d3.select(sid1);
				//alert(selection1);
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
			//var statusups=d3.selectAll(".up1");
		},
		setBarWidth : function (barWidth) {
			alert("setBarWidth");
			this._barWidth = barWidth;
			this._svgMap._scheduleUpdate();
		},

		setSpacing : function (spacing) {
			alert("setSpacing");
			this._spacing = spacing;
			this._svgMap._scheduleUpdate();
		},

		render : function (svgMap) {

			//this._xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, svgMap._width - svgMap._padding * 2 ] );
			//var selection = this._layer.selectAll( "g.item" )
			//  .data( this._items, function( item ) { return item.id(); } );
			//  alert(selection);
			var head = document.getElementsByTagName('HEAD').item(0);
			var style = document.createElement('link');
			style.href = 'rwt-resources/main.css';
			style.rel = 'stylesheet';
			style.type = 'text/css';
			head.appendChild(style);
			thatmap = this;
			//��ʼ���˵�
			this._initMenu();

			for (var i = 0; i < this._items.length; i++) {

				var item = this._items[i];
				var itemid = item.getItemid();
				var svgtype = item.getSvgtype();
				var lastindex = itemid.lastIndexOf("-");
				var selection;
				if (lastindex > -1) {
					var parentid = "#" + itemid.substr(0, lastindex);
					selection = this._layer.select(parentid);
				} else {
					selection = this._layer;
				}
				this._createElements(selection, svgtype, item);
				// this._updateElements( selection, svgtype );

			}
			//alert("a");
			if (this._updatedata) {
				this._updatedata = false;
				//selection=this._layer.selectAll( "ellipse" );
				//this.changeColor(selection);
				//alert("start");
				this._updateTimer1 = new rwt.client.Timer(1000);
				this._updateTimer1.addEventListener("interval", this.changeColor, this);
				//this._updateTimer1.start();


			}

		},
		// 0 grean 1
		changeColor : function () {
			try {
				//alert(this._tag);
				var sups = this._layer.selectAll(".up1");
				if (this._tag == 0) {
					this._tag = 1;
					this._updateTimer1.stop();
					this._updateTimer1.restart();
					// alert(selection);
					sups.style("fill", "#00FF00");
					// rwt.client.Timer.once(this.changeColor(selection),this,30000)

				} else {
					this._tag = 0;
					this._updateTimer1.stop();
					this._updateTimer1.restart();
					//alert(selection);
					sups.style("fill", "#006400");
					//rwt.client.Timer.once(this.changeColor(selection),this,30000)
				}
			} catch (e) {}
		},
		/**
		 ** ��ʼ���˵�
		 **/
		_initMenu : function () {
			var that = this;
			var ulul = d3.select("#panelmenu").append("ul");
			var menudes = this.getMenudesc();
			var arraymenu = menudes.split(":");
			for (var i = 0; i < arraymenu.length; i++) {
				var lia = ulul.append("li").append("a")
					.style("cursor", "pointer")
					.text(arraymenu[i]);
				//lia.on("click",function (d, i) { that._selectItem(i) });
			}
			ulul.selectAll("a").on("click", function (d, i) {
				var eventstring = 'openport';
				if (i == 0) {
					eventstring = 'openport';
				} else if (i == 1) {
					eventstring = 'closeport';
				} else if (i == 2) {
					eventstring = 'deviceip';
				}
				that._savedata(eventstring, that._selectnodeid);
			});
			//ulul.append("li").append("a")
			//.style("cursor","pointer")
			//.text("test1")
			//.on("click",function () {that._selectItem(1) });
		},
		_createElements : function (selection, svgtype, item) {
			var that = this;
			switch (svgtype) {
			case 1:
				this._createGroup(selection, item);
				break;
			case 2:
				this._createPaths(selection, item);
				break;
			case 3:
				this._createEllipse(selection, item)
				break;
			case 4:
				this._createBars(selection, item);
				break;
			case 5:
				this._createPolylines(selection, item);
				break;
			case 10:
				this._createTexts(selection, item);
			default:

			}
			// items.on( "click", function( datum, index ) { that._selectItem( index ); } );
			//this._createBars( items );
			//this._createTexts( items );

		},

		_createGroup : function (selection, item) {

			var items = selection.append("svg:g")
				.attr("class", "item")
				.attr("id", item.getItemid())
				.attr("transform", item.getTransform())
				.attr("opacity", 1.0);
		},
		_createEllipse : function (selection, item) {

			var sel1 = selection.append("svg:ellipse")
				.attr("cx", item.getEcx())
				.attr("cy", item.getEcy())
				.attr("rx", item.getErx())
				.attr("ry", item.getEry())
				.attr("fill", item.getFillcolor())
				//.attr( "fill-opacity", item.getFillopacity())
				.attr("stroke", item.getLinecolor())
				.attr("stroke-linecap", "round")
				.attr("stroke-linejoin", "round")
				.attr("stroke-width", item.getLinewidth());
			if (item.getSvid() != "") {
				sel1.attr("id", item.getSvid());
			}

		},
		_createBars : function (selection, item) {
			var that = this;
			var sel1 = selection.append("svg:rect")
				.attr("x", item.getEcx())
				.attr("y", item.getEcy())
				.attr("width", item.getErx())
				.attr("height", item.getEry())
				.attr("fill", item.getFillcolor())
				//.attr( "fill-opacity", item.getFillopacity())
				.attr("stroke", item.getLinecolor())
				.attr("stroke-linecap", "round")
				.attr("stroke-linejoin", "round")
				.attr("stroke-width", item.getLinewidth());

			if (item.getSvid() != "") {
				var cid = item.getSvid();
				var indexid = cid.substring(1);
				sel1.attr("id", cid);
				//��ʼ��tooltip
				var ttitel = sel1.append("svg:title");
				var text2 = this.getTooltipdesc();
				var text3 = text2.replace('p1', '-')
					.replace('p2', indexid)
					.replace('p3', '-')
					.replace('p4', '-')
					.replace('p5', '-')
					.replace('p6', '-')
					.replace('p7', "-")
					.replace('p8', "-")
					.replace('p9', "-");
				ttitel.text(text3);
				sel1.on("contextmenu", this.contextmenu);
				sel1.on("click", function (d, i) {
				that._savedata("portport" , cid)
			});

			}
			if (item.getCssclass() != "") {
				sel1.attr("class", item.getCssclass())
				.attr("fill", "blue")
				.attr("fill-opacity", "0")
				.on("mouseover", function () {
					d3.select(this)
					.attr("fill-opacity", "0.5");
				})
				.on("mouseout", function () {
					d3.select(this)
					.attr("fill-opacity", "0");
				});
			}

		},
		//�����˵�
		contextmenu : function () {
			var position = d3.mouse(this);
			//var topy=position[1]-12;
			var topy=5;
			//alert(position);
			thatmap._selectnodeid = d3.select(this).attr("id");
			d3.select('#panelmenu')
			.style('position', 'absolute')
			.style('left', position[0] + "px")
			.style('top', topy + "px")
			.style('display', 'inline-block')
			.on('mouseleave', function () {
				d3.select('#panelmenu').style('display', 'none');
			});

			d3.event.preventDefault();
			//document.getElementById('nodeId').value= node
		},
		_createPolylines : function (selection, item) {
			var that = this;
			var sel1 = selection.append("svg:polyline")
				.attr("points", item.getValue())
				.attr("fill", item.getFillcolor())
				//.attr( "fill-opacity", item.getFillopacity())
				.attr("stroke", item.getLinecolor())
				.attr("stroke-linecap", "round")
				.attr("stroke-linejoin", "round")
				.attr("transform", item.getTransform())
				.attr("stroke-width", item.getLinewidth());
			if (item.getSvid() != "") {
				sel1.attr("id", item.getSvid());

			}
		},

		_createTexts : function (selection, item) {

			//var items = selection.append( "svg:g" )
			// .attr( "class", "tt1" )
			//.attr( "transform","translate("+item.getEcx()+","+item.getEcy()+")")
			//.attr( "opacity", 1.0 );
			selection.append("svg:text")
			.attr("x", item.getEcx())
			.attr("y", item.getEcy())
			.attr("text-anchor", item.getTextanchor())
			.style("fill", item.getFillcolor())
			.style("font-family", item.getFontname())
			.style("font-size", item.getFontsize())
			.text(item.getValue());

		},
		_createPaths : function (selection, item) {
			var that = this;
			var vpath = selection.append("svg:path")
				//.attr( "d",  "M 10 25 L 10 75L 60 75L 10 25")
				// .attr( "class" , item.getCssclass() )
				.attr("d", item.getValue())
				.attr("transform", item.getTransform())
				// .attr( "fill-opacity", item.getFillopacity())
				.attr("fill", item.getFillcolor())
				.attr("stroke", item.getLinecolor())
				.attr("stroke-linecap", "round")
				.attr("stroke-linejoin", "round")
				.attr("stroke-width", item.getLinewidth());
			if (item.getSvid() != "") {
				vpath.attr("id", item.getSvid());
			}
			d3.select(".st8").on("mouseover", function (d, i) {
				d3.select(".st8").transition().duration(300).style("fill", "#00ffff");
			})
			.on("mouseout", function (d, i) {
				d3.select(".st8").transition().duration(300).style("fill", "#FF6347");
			});
		},
		_updateElements : function (selection, svgtype) {
			// this._updateBars( selection.select( "rect" ) );
			// this._updateTexts( selection.select( "text" ) );
			switch (svgtype) {
			case 1:
				this._updateGroup(selection.select("group"));
				break;
			case 2:
				this._updatePaths(selection.select("path"));
				break;
			case 3:

				break;
			case 4:
				break;
			default:

			}
			//this._updatePaths( selection.select( "path" ) );
		},

		_updateBars : function (selection) {
			var that = this;
			selection
			.transition()
			.duration(1000)
			.attr("y", function (item, index) {
				return that._getOffset(index);
			})
			.attr("width", function (item) {
				return that._xScale(item.getValue());
			})
			.attr("height", that._barWidth)
			.attr("fill", function (item) {
				return item.getColor();
			});
		},

		_updateTexts : function (selection) {
			var that = this;
			selection
			.transition()
			.duration(1000)
			.attr("x", function (item) {
				return that._svgMap._padding + 6 + that._xScale(item.getValue());
			})
			.attr("y", function (item, index) {
				return that._getOffset(index) + that._barWidth / 2;
			})
			.text(function (item) {
				return item.getText();
			});
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

		_savedata : function (index, data) {
			var remoteObject = rap.getRemoteObject(this);
			remoteObject.notify("Selection", {
				"index" : index,
				"data" : data
			});
		},

		_selectItem : function (index) {
			var remoteObject = rap.getRemoteObject(this);
			remoteObject.notify("Selection", {
				"index" : index,
				"data" : ""
			});
		},

		_getOffset : function (index) {
			return this._svgMap._padding + index * (this._barWidth + this._spacing);
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
