
;(function(cxt){
  if (!window.svgdevicepanel) {
		window.svgdevicepanel = {};
	}
  var SvgChartPanel = function(){
    this.init.apply(this, arguments);
  }
  SvgChartPanel.prototype = {
    init:function(options){
      this.container = options.container;
      this.menuPanel = options.menuPanel;
      this.svgTxt = options.svgTxt || '';
      this.tooltipdesc = options.tooltipdesc;
      this.portBeSelectedCall = options.portBeSelectedCall;
      this.statusArr = options.statusArr || [];
      this.svgContainer = null;
      this.svgJqObj = null; //svg的jquery对象
      this.portJqEleMap = {}; //端口号的jquery对象map
      this.portLightJqElMap = []; //端口灯号的jquery对象map
      this.portHandleD3ElMap = []; //端口号的处理操作的图形，每个元素都是d3对象
      this.portTipTitleD3Map = {};
      this.selectedNodeId = '';
      this.blinkFlag = 0; //
      this.intervalTimer = null;
      this.paramNameMap = {"":"", "SysOid":"sysObjId", "容器号":"containerNum", "端口号":"portNum", "端口灯号":"portLightNum", "端口数":"portCount", "端口灯数":"portLightCount"};
      // 默认：黑色， 0：深灰色， 1：深绿色。2：黄色。3：红色。 4：蓝色，5：橘黄色
      this.statusColorMap = {"":"#080808", 0:"#464141", 1:"#006400", 2:"#FFFF00", 3:"#FF0000", 4:"#0000FF", 5:"#FFA500"};
      // this.xStart = options.xStart;
      // this.yStart = options.yStart;
      this.blinkLightMap = {}; //哪些指示灯需求闪烁。
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var element = this.svgContainer= document.createElement( "div" );
	    element.style.position = "absolute";
	    element.style.left = "0";
	    element.style.top = "0";
	    // element.style.width = "100%";
	    // element.style.height = "100%";
			element.style.overflow="auto";
			element.style.backgroundColor ="#7f707f";
      // element.style.transform = "scale(50%,50%) translate(-50%,-50%)";
			$(element).addClass("svgContainer").html(this.svgTxt);
			$(element).find("svg title").remove();
      this.svgJqObj = $(element).find("svg");
      $(this.container).append( element );
      // console.log("this.svgTxt------>:",this.svgTxt);
      //重新格式一下svg的结构，获取网线端口元素数组。
      this.formatSvgXml();
      this.createPortHandleEl();
      this.createToolTip();
      this.createIntervalTimer();
    },
    addEvent:function(){
      var _this = this;
      for(var key in this.portHandleD3ElMap){
        var portRect = this.portHandleD3ElMap[key];
        portRect.on("contextmenu", function(){ //响应鼠标右键事件
          var position = $(this).position();
    			_this.selectedNodeId = d3.select(this).attr("data-nodeid");
    			_this.menuPanel.showMenuPanel(position,_this.selectedNodeId);
    			d3.event.preventDefault();
        });
  			portRect.on("click", function (d, index) {
  				_this.portBeSelected("portport" , d3.select(this).attr('data-nodeid'));
  			});
        portRect.on("mouseover", function () {
  				d3.select(this).attr("fill-opacity", "1");
  			});
  			portRect.on("mouseout", function () {
  				d3.select(this).attr("fill-opacity", "0");
          setTimeout(function(){ //this is necessary
            _this.menuPanel.hideMenuPanel();
          },50);
  			});
      }
    },
    formatSvgXml:function(){
      // because the minimum font in visio is normal, but transform to svg and show in web ,the font-size will become big.
      // because minimum font-size in web browser is 10px.
      this.svgJqObj.width(2 * this.svgJqObj.width());
      this.svgJqObj.height(2 * this.svgJqObj.height());
      this.svgJqObj.closest("div").css("transform","scale(0.5,0.5) translate(-50%,-50%)");

      var v_cpEleArr = this.svgJqObj[0].getElementsByTagName("v:cp");
      for(var i=0;i<v_cpEleArr.length;i++){
        var el = $(v_cpEleArr[i]);
        var parentG_jq = el.closest('g');
        var nameCn = el.attr('v:lbl') || "";
        var valueStr = el.attr('v:val');
        if(!valueStr){ //过滤掉无用的参数。
          continue;
        }
        var key = this.getValueFromStr(valueStr);
        var nameKey = this.paramNameMap[nameCn] || "default";
        parentG_jq.attr('class',nameKey+'_'+key);
        if(nameCn == '端口号'){
          this.portJqEleMap[key+""] = parentG_jq;
        }else if(nameCn == '端口灯号'){
          this.portLightJqElMap[key+""] = parentG_jq;
          this.blinkLightMap[key+""] = parentG_jq;
        }
      }
    },
    // 创建端口用于事件交互的图形。是原端口图形的副本
    createPortHandleEl:function(){
      for(var key in this.portJqEleMap){
        var el = this.portJqEleMap[key];
        var cloneEl = el.clone();
        cloneEl.removeAttr("id").removeAttr("class").find("path").removeAttr("class").css("cursor","pointer");
        el.after(cloneEl);
        this.portHandleD3ElMap[key] = d3.select(cloneEl[0]);
        this.portHandleD3ElMap[key].attr("data-nodeid", key)
        .attr("fill", "blue")
  			.attr("fill-opacity", "0");
      }
    },
    createToolTip:function(){
      for(var key in this.portHandleD3ElMap){
        var ttitel = this.portHandleD3ElMap[key].append("svg:title");
        var indexid = 1;
  			var text2 = this.tooltipdesc;
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
        this.portTipTitleD3Map[key] = ttitel;
      }
    },
    createIntervalTimer:function(){
      var _this = this;
      this.intervalTimer = setInterval(function(){
        if(_this.blinkFlag == 1){
          _this.blinkFlag = 0;
          _this.startIndicatorLightBlink();
        }else{
          _this.blinkFlag = 1;
          _this.stopIndicatorLightBlink();
        }
      },1000);
    },
    // indicator light blink ,指示灯的闪烁
    startIndicatorLightBlink:function(){
      for(var key in this.blinkLightMap){
        var g_el = this.blinkLightMap[key];
        var colorValue = this.statusColorMap[this.statusArr[+key] ||""];
        g_el.find("path").css("fill",colorValue);
      }
    },
    stopIndicatorLightBlink:function(){
      for(var key in this.blinkLightMap){
        var g_el = this.blinkLightMap[key];
        g_el.find("path").css("fill","black");
      }
    },
    // 更新端口状态
    updateStatus : function (statusArr) {
      this.statusArr = statusArr;
      for(var i=0;i<statusArr.length;i++){
        var key = i + 1;
        var value = statusArr[i] || '';
        var portEl = this.portJqEleMap[key];
        var portLightEl = this.portLightJqElMap[key];
        var colorValue = this.statusColorMap[value];
        portEl && portEl.find('path').css('fill',colorValue);
        portLightEl && portLightEl.find('path').css('fill',colorValue);
      }
		},
    updateTooltip:function(tooltipData){
      this.tooltipData = tooltipData;
      var that = this;
      for(var key in this.portTipTitleD3Map){
        var d3Title = this.portTipTitleD3Map[key];
        //TODO
      }
			tooltipData.map(function (value, index) {
				var nindex = index + 1;
				var nid = "#c" + nindex;
				// var selection1 = d3.select(nid);
				// selection1.text("");
				// var text2 = that.getTooltipdesc();
				// if (value != null) {
				// 	var values = value.split(":");
				// 	if (values.length > 0) {
				// 		var text3 = text2.replace('p1', values[0])
				// 			.replace('p2', values[1])
				// 			.replace('p3', values[2])
				// 			.replace('p4', values[3])
				// 			.replace('p5', values[4])
				// 			.replace('p6', values[5])
				// 			.replace('p7', values[6])
				// 			.replace('p8', values[7])
				// 			.replace('p9', values[8]);
				// 		selection1.append("svg:title").text(text3);
				// 	}
				// }
			});
    },
    portBeSelected:function(eventName,nodeid){
      this.portBeSelectedCall && this.portBeSelectedCall.apply(null,[eventName,nodeid]);
    },
    refreshSize:function(){
    },
    getValueFromStr:function(str){
      var start = str.indexOf('(');
      var end = str.indexOf(')');
      return str.substring(start+1,end);
    },
    dispose:function(){
      clearInterval(this.intervalTimer);
    }

  };
  cxt.svgdevicepanel.SvgChartPanel = SvgChartPanel;
})(window);
