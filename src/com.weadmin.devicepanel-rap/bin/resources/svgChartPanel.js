
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
      this.svgContainer = null;
      this.svgJqObj = null; //svg的jquery对象
      this.portEleArr = [];
      this.portHandleRectArr = []; //每个元素都是d3对象
      this.portTipTitleArr = [];
      this.selectSvid = '';
      // this.xStart = options.xStart;
      // this.yStart = options.yStart;

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
      element.style.transform = "scale(50%,50%) translate(-50%,-50%)";
			$(element).addClass("svgContainer").html(this.svgTxt);
			// $(element).find("svg title").remove();
      this.svgJqObj = $(element).find("svg");
      $(this.container).append( element );
      console.log("this.svgTxt------>:",this.svgTxt);
      //重新格式一下svg的结构，获取网线端口元素数组。
      this.formatSvgXml();
      this.createPortRect();
      this.createToolTip();
    },
    addEvent:function(){
      var _this = this;
      for(var i=0;i<this.portHandleRectArr.length;i++){
        var portRect = this.portHandleRectArr[i];
        portRect.on("contextmenu", this.contextmenu);
  			portRect.on("click", function (d, i) {
          var protRectEle = d3.select(this);
  				_this.portBeSelected("portport" , protRectEle.attr('data-svid'));
  			});
        portRect.on("mouseover", function () {
  				d3.select(this).attr("fill-opacity", "0.5");
  			});
  			portRect.on("mouseout", function () {
  				d3.select(this).attr("fill-opacity", "0");
  			});
      }
    },
    formatSvgXml:function(){

    },
    createPortRect:function(){
      for(var i=0;i<this.portEleArr.length;i++){
        this.portHandleRectArr[i] = d3.select('svg').append("svg:rect")
        .attr("data-svid", $(portEleArr[0]).attr("data-svid"))
        .attr("x", 0)
        .attr("y", 0)
        .attr("width", 20)
        .attr("height", 20)
        .attr("fill", "#a13243")
        .attr("stroke", "#a13243")
        .attr("fill", "blue")
  			.attr("fill-opacity", "0")
        .attr("stroke-linecap", "round")
        .attr("stroke-linejoin", "round")
        .attr("stroke-width", 1);

      }
    },
    createToolTip:function(){
      for(var i=0;i<this.portHandleRectArr.length;i++){
        var ttitel = this.portHandleRectArr[i].append("svg:title");
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
        this.portTipTitleArr[i] = ttitel;
      }
    },
    //响应鼠标右键事件
		contextmenu : function () {
			var position = d3.mouse(this);
			this.selectSvid = d3.select(this).attr("id");
			this.menuPanel.showMenuPanel(position);
			d3.event.preventDefault();
		},
    // indicator light blink ,指示灯的闪烁
		indicatorLightBlink : function () {
			try {
				var sups = this._layer.selectAll(".up1");
				if (this._tag == 0) {
					this._tag = 1;
					this._updateTimer1.stop();
					this._updateTimer1.restart();
					sups.style("fill", "#00FF00");
				} else {
					this._tag = 0;
					this._updateTimer1.stop();
					this._updateTimer1.restart();
					sups.style("fill", "#006400");
				}
			} catch (e) {}
		},
    portBeSelected:function(eventName,svid){
      this.portBeSelectedCall && this.portBeSelectedCall.apply(null,[eventName,svid]);
    },
    // setEnlargeBox:function(enlargeBox){
    //   this.enlargeBox = enlargeBox;
    // },
    // setLineColor:function(lineColor){
    //   this.lineColor = lineColor;
    // },
    refreshSize:function(){
    },
    formatSvgTxt:function(){
      var paramNameMap = {"SysOid":"sysObjId", "端口号":"portNum", "端口灯号":"portLightNum", "端口数":"portCount", "端口灯数":"portLightCount"};
      var v_cpEleArr = document.getElementsByTagName("v:cp");
      for(var i=0;i<v_cpEleArr.length;i++){
        var el = $(v_capEleArr[i]);
        var jq_parent = el.closest('g');
        var nameCn = el.attr('v:lbl');
        var value = el.attr('v:val');
        var nameKey = paramNameMap[nameCn] || "default";
        jq_parent.attr('class',nameKey+'_'+value);
        if(nameCn == '端口号'){
          portEleArr.push(jq_parent);
        }else if(nameCn == '端口灯号'){
          portLightEleArr.push(jq_parent);
        }
      }
    },
    getValueFromStr:function(str){
      var start = str.indexOf('(');
      var end = str.indexOf(')');
      return str.subString(start,end+1);
    }

  };
  cxt.svgdevicepanel.SvgChartPanel = SvgChartPanel;
})(window);
