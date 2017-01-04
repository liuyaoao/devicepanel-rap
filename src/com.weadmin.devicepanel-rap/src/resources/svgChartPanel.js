
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
      this.portNameDialog = options.portNameDialog;
      this.uniqueId = options.uniqueId;
      this.svgTxt = options.svgTxt || '';
      this.portBeSelectedCall = options.portBeSelectedCall;
      this.statusMap = options.statusMap || {};
      this.tooltipDataMap = options.tooltipDataMap || {};
      this.interfaceNameList = options.interfaceNameList || [];
      this.svgContainer = null;
      this.tooltipTitle = null;
      this.svgJqObj = null; //svg的jquery对象
      this.portJqEleMap = {}; //端口号的jquery对象map
      this.portLightJqElMap = []; //端口灯号的jquery对象map
      this.portTipTitleJqMap = {};
      this.portNum2InterfaceNameMap = {}; // 端口号对应的接口名的map结构。
      this.interfaceName2portNumMap = {}; // 接口名对应的端口号的map结构。
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
      this.tooltipTitle = $(this.svgContainer).find("svg title:first").clone(); //clone first one title tag. because by jquery create not work.
			$(this.svgContainer).find("svg title").remove();
      this.svgJqObj = $(this.svgContainer).find("svg");
      this.svgWidth = (this.svgJqObj.attr("width")).split("in")[0] *96; //unit 'in' to 'px' have to multiply by 100.
      this.svgHeight = (this.svgJqObj.attr("height")).split("in")[0] *96;
      $(this.container).append( element );
      //重新格式一下svg的结构，获取网线端口元素数组。
      setTimeout(function(){
        _this.formatSvgXml();
        _this.createToolTip();
        console.log("svg xml:-----");
        _this.createIntervalTimer();
        _this.addEvent();
      },10);
    },
    addEvent:function(){
      var _this = this;
      for(var key in this.portJqEleMap){
        var portRect = this.portJqEleMap[key];
        portRect.on("contextmenu", function(event){ //响应鼠标右键事件
          var pos = $(this).position();
          var menuWidth = _this.menuPanel.getWidth();
          var menuHeight = _this.menuPanel.getHeight();
          var svgPos = $(this).closest("svg").position();
    			_this.selectedNodeId = $(this).attr("data-portname");
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
  				_this.portBeSelected("portport" , $(this).attr('data-portname'));
  			});
        portRect.on("mouseover", function () {
          var that = $(this);
          var portNum = $(this).attr("data-portnum");
          $(this).attr('class',that.attr('class')+' mouseover'+_this.uniqueId);
          // $(this).removeClass('mouseover'+_this.uniqueId);
  				$(this).find("path").css("stroke-width", "1");
  			});
  			portRect.on("mouseout", function () {
          var that = $(this);
          $(this).attr('class',that.attr('class').split(" ")[0]);
          $(this).removeClass('mouseover'+_this.uniqueId);
  				$(this).find("path").css("stroke-width", "0");
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
        var key = this.getValueFromStr(valueStr||"");
        var nameKey = this.paramNameMap[nameCn] || nameCn;
        if(nameCn == '端口号' || nameCn == 'portNum'){
          parentG_jq.attr('class',nameKey+'_'+key);
          this.portJqEleMap[key+""] = parentG_jq;
          parentG_jq.css("cursor","pointer");
          parentG_jq.attr("data-portnum", key).find("path").css("stroke", "#fff").css("stroke-width", "0");
        }if(nameCn == 'interfaceName'){
          parentG_jq.attr("data-portname", key);
          this.updatePortNum2InterfaceMap(parentG_jq);
        }else if(nameCn == '端口灯号' || nameCn == 'portLightNum'){
          parentG_jq.attr('class',nameKey+'_'+key);
          this.portLightJqElMap[key+""] = parentG_jq;
          this.blinkLightMap[key+""] = parentG_jq;
        }
      }
    },
    createToolTip:function(){
      var _this = this;
      for(var key in this.portJqEleMap){
        var portRect = _this.portJqEleMap[key];
        var titleTip = this.tooltipTitle.clone();
        titleTip.attr("tooltip","1");
        var tooltipStr = this.tooltipDataMap[key] || "端口信息";
  			titleTip.text(tooltipStr.replace(/<br>/g,'\n'));
        this.portTipTitleJqMap[key] = titleTip;
        portRect.append(titleTip);
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
      for(var portNum in this.blinkLightMap){
        var g_el = this.blinkLightMap[portNum];
        var portName = this.portNum2InterfaceNameMap[portNum]||"";
        var colorValue = this.statusColorMap[this.statusMap[portName] ||""];
        g_el.find("path") && g_el.find("path").css("fill",colorValue);
        g_el.find("ellipse") && g_el.find("ellipse").css("fill",colorValue);
      }
    },
    stopIndicatorLightBlink:function(){
      for(var portNum in this.blinkLightMap){
        var g_el = this.blinkLightMap[portNum];
        var portName = this.portNum2InterfaceNameMap[portNum]||"";
        var statusVal = this.statusMap[portName] || "";
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
      for(var portName in statusMap){
        var value = statusMap[portName] || '';
        var portNum = this.interfaceName2portNumMap[portName] ||'';
        var portEl = this.portJqEleMap[portNum];
        var portLightEl = this.portLightJqElMap[portNum];
        var colorValue = this.statusColorMap[value];
        portEl && portEl.find('path').css('fill',colorValue);
        portLightEl && portLightEl.find('path').css('fill',colorValue);
      }
      this.updatePortPathStrokeColor();
		},
    // 更新端口的鼠标悬停提示面板
    updateTooltip:function(tooltipDataMap){
      this.tooltipDataMap = tooltipDataMap;
      for(var portName in tooltipDataMap){
        var tooltipStr = tooltipDataMap[portName] || '';
        var portNum = this.interfaceName2portNumMap[portName] ||'';
        var jqTitle = this.portTipTitleJqMap[portNum];
        jqTitle && jqTitle.text(tooltipStr.replace(/<br>/g,'\n'));
      }
    },
    //
    updateInterfaceName:function(interfaceNameList){
      var _this = this;
      setTimeout(function(){
        _this.portNameDialog.updateNameList(interfaceNameList);
      },100);
    },
    portBeSelected:function(eventName,portName){
      this.portBeSelectedCall && this.portBeSelectedCall.apply(null,[eventName,portName]);
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
    updatePortPathStrokeColor:function(){
      for(var key in this.portJqEleMap){
        this.portJqEleMap[key].find("path").css("stroke",this.strokeColorMap[this.statusMap[key]||""]);
      }
    },
    updatePortNum2InterfaceMap:function(parentG_jq){
      var gvcpElArr = parentG_jq[0].getElementsByTagName("v:cp");
      var portNum = '';
      var interfaceName = '';
      for(var k=0;k<gvcpElArr.length;k++){
        var name = $(gvcpElArr[k]).attr('v:lbl') || "";
        var value = this.getValueFromStr($(gvcpElArr[k]).attr('v:val')||'');
        if(name=="端口号" || name=="portNum"){
          portNum = value;
        }else if(name=="interfaceName"){
          interfaceName = value;
        }
      }
      this.portNum2InterfaceNameMap[portNum] = interfaceName;
      this.interfaceName2portNumMap[interfaceName] = portNum;
    },
    getModifiedSvgTxt:function(){
      this.svgTxt = this.svgContainer.innerHTML;
      return this.svgTxt;
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
