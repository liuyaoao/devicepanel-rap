
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
      this.uniqueId = options.uniqueId;
      this.svgTxt = options.svgTxt || '';
      this.portBeSelectedCall = options.portBeSelectedCall;
      this.statusMap = options.statusMap || {};
      this.tooltipDataMap = options.tooltipDataMap || {};
      this.svgContainer = null;
      this.tooltipTitle = null;
      this.svgJqObj = null; //svg的jquery对象
      this.portJqEleMap = {}; //端口号的jquery对象map
      this.portLightJqElMap = []; //端口灯号的jquery对象map
      this.portHandleJqElMap = []; //端口号的处理操作的图形，
      this.portTipTitleJqMap = {};
      this.selectedNodeId = '';
      this.svgWidth = 0;
      this.svgHeight = 0;
      this.blinkFlag = 0; //
      this.intervalTimer = null;
      this.paramNameMap = {"":"", "SysOid":"sysObjId", "容器号":"containerNum", "端口号":"portNum", "端口灯号":"portLightNum", "端口数":"portCount", "端口灯数":"portLightCount"};
      this.noNeedBlinkstatusMap = [0,4,5];
      // 默认：黑色， 0：深灰色， 1：绿色。2：黄色。3：红色。 4：蓝色，5：橘黄色
      this.statusColorMap = {"":"#080808", 0:"#080808", 1:"#19E807", 2:"#FFF20B", 3:"#FF1411", 4:"#2813E8", 5:"#FF6600"};
      this.strokeColorMap = {"":"#8819E8", 0:"#FF1411", 1:"#FF11FF", 2:"#9011FF", 3:"#3B12E8", 4:"#FF5C08", 5:"#00FFDC"};

      this.blinkLightMap = {}; //哪些指示灯需要闪烁。
      this.initElement();
    },
    initElement:function(){
      var _this = this;
      var element = this.svgContainer= document.createElement( "div" );
	    element.style.position = "absolute";
	    element.style.left = "0";
	    element.style.top = "0";
			element.style.overflow="auto";
			// element.style.backgroundColor ="#7f707f";
			$(element).addClass("svgContainer").html(this.svgTxt);
      this.tooltipTitle = $(element).find("svg title:first").clone();
			$(element).find("svg title").remove();
      this.svgJqObj = $(element).find("svg");
      this.svgWidth = (this.svgJqObj.attr("width")).split("in")[0] *96; //unit 'in' to 'px' have to multiply by 100.
      this.svgHeight = (this.svgJqObj.attr("height")).split("in")[0] *96;
      $(this.container).append( element );
      // console.log("this.svgTxt------>:",this.svgTxt);
      //重新格式一下svg的结构，获取网线端口元素数组。
      setTimeout(function(){
        _this.formatSvgXml();
        _this.createPortHandleEl();
        _this.createToolTip();
        _this.createIntervalTimer();
        _this.addEvent();
      },10);
    },
    addEvent:function(){
      var _this = this;
      for(var key in this.portHandleJqElMap){
        var portRect = this.portHandleJqElMap[key];
        portRect.on("contextmenu", function(event){ //响应鼠标右键事件
          var pos = $(this).position();
          var menuWidth = _this.menuPanel.getWidth();
          var menuHeight = _this.menuPanel.getHeight();
          var svgPos = $(this).closest("svg").position();
    			_this.selectedNodeId = $(this).attr("data-nodeid");
          var destPos = {left:pos.left-svgPos.left,top:pos.top-svgPos.top};
          if(destPos.left+menuWidth+20>_this.svgWidth){
            destPos.left = _this.svgWidth-menuWidth-20;
          }
          if(destPos.top+menuHeight>_this.svgHeight){
            destPos.top = _this.svgHeight-menuHeight;
          }
    			_this.menuPanel.showMenuPanel(destPos,_this.selectedNodeId);
    			event.preventDefault();
        });
  			portRect.on("click", function (d, index) {
  				_this.portBeSelected("portport" , $(this).attr('data-nodeid'));
  			});
        portRect.on("mouseover", function () {
  				$(this).attr("stroke-width", "1");
  			});
  			portRect.on("mouseout", function () {
  				$(this).attr("stroke-width", "0");
          setTimeout(function(){ //this is necessary
            _this.menuPanel.hideMenuPanel();
          },50);
  			});
      }
    },
    formatSvgXml:function(){
      // because the minimum font in visio is normal, but transform to svg and show in web ,the font-size will become big.
      // because minimum font-size in web browser is 10px.
      this.refreshSize(this.svgWidth, this.svgHeight);

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
        this.portHandleJqElMap[key] = $(cloneEl[0]);
        this.portHandleJqElMap[key].attr("data-nodeid", key)
        .attr("fill", "#FFF")
        .attr("fill-opacity", "0")
        .attr("stroke", "#fff")
  			.attr("stroke-width", "0");
      }
    },
    createToolTip:function(){
      var _this = this;
      for(var key in this.portHandleJqElMap){
        var portRect = _this.portHandleJqElMap[key];
        var titleTip = this.tooltipTitle.clone();
        titleTip.attr("tooltip","1");
        var tooltipStr = this.tooltipDataMap[key] || "端口信息";
  			titleTip.text(tooltipStr.replace(/<br>/g,'\n'));
        this.portTipTitleJqMap[key] = titleTip;
        this.portHandleJqElMap[key].prepend(titleTip);
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
        var colorValue = this.statusColorMap[this.statusMap[key] ||""];
        g_el.find("path") && g_el.find("path").css("fill",colorValue);
        g_el.find("ellipse") && g_el.find("ellipse").css("fill",colorValue);
      }
    },
    stopIndicatorLightBlink:function(){
      for(var key in this.blinkLightMap){
        var g_el = this.blinkLightMap[key];
        var statusVal = this.statusMap[key] || "";
        if(this.noNeedBlinkstatusMap.indexOf(statusVal) != -1){
          g_el.find("path") && g_el.find("path").css("fill",this.statusColorMap[statusVal]);
          g_el.find("ellipse") && g_el.find("ellipse").css("fill",this.statusColorMap[statusVal]);
        }else{
          g_el.find("path") && g_el.find("path").css("fill","black");
          g_el.find("ellipse") && g_el.find("ellipse").css("fill","black");
        }
      }
    },
    // 更新端口状态
    updateStatus : function (statusMap) {
      this.statusMap = statusMap;
      for(var key in statusMap){
        var value = statusMap[key] || '';
        var portEl = this.portJqEleMap[key];
        var portLightEl = this.portLightJqElMap[key];
        var colorValue = this.statusColorMap[value];
        portEl && portEl.find('path').css('fill',colorValue);
        portLightEl && portLightEl.find('path').css('fill',colorValue);
      }
      this.updatePortHandleStrokeColor();
		},
    // 更新端口的鼠标悬停提示面板
    updateTooltip:function(tooltipDataMap){
      this.tooltipDataMap = tooltipDataMap;
      for(var key in tooltipDataMap){
        var tooltipStr = tooltipDataMap[key] || '';
        var jqTitle = this.portTipTitleJqMap[key];
        jqTitle && jqTitle.text(tooltipStr.replace(/<br>/g,'\n'));
      }
    },
    portBeSelected:function(eventName,nodeid){
      this.portBeSelectedCall && this.portBeSelectedCall.apply(null,[eventName,nodeid]);
    },
    getSize:function(){
      return {width:+(this.svgWidth.toFixed(2)), height:+(this.svgHeight.toFixed(2))};
    },
    refreshSize:function(svgWidth,svgHeight){
      this.svgWidth = +(svgWidth.toFixed(2));
      this.svgHeight = +(svgHeight.toFixed(2));
      this.svgJqObj.css({"width":2 * this.svgWidth,height:2 * this.svgHeight});
      this.svgJqObj.closest("div").css("transform","scale(0.5,0.5) translate(-50%,-50%)");
    },
    updatePortHandleStrokeColor:function(){
      for(var key in this.portHandleJqElMap){
        this.portHandleJqElMap[key].attr("stroke",this.strokeColorMap[this.statusMap[key]||""]);
      }
    },
    getValueFromStr:function(str){
      var start = str.indexOf('(');
      var end = str.indexOf(')');
      return str.substring(start+1,end);
    },
    dispose:function(){
      $(this.svgContainer).off().remove();
      clearInterval(this.intervalTimer);
    }

  };
  cxt.svgdevicepanel.SvgChartPanel = SvgChartPanel;
})(window);
