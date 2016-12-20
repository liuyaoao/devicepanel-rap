
;(function(cxt){
  if (!window.svgdevicepanel) {
		window.svgdevicepanel = {};
	}
  var MenuPanel = function(){
    this.init.apply(this, arguments);
  }
  MenuPanel.prototype = {
    init:function(options){
      this.container = options.container;
      this.menuDesc = options.menuDesc;
      this.clickMenuCall = options.clickMenuCall;
      this.eventNameMap = {"":"openport", "0":"openport", "1":"closeport", "2":"deviceip"};
      // this.yBoxNum = options.yBoxNum;
      // this.xStart = options.xStart;
      // this.yStart = options.yStart;

      this.canClickEnlarge = true;
      this.isCtrlKeyDown = false;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var ele = document.createElement( "div" );
      ele.setAttribute("id","panelmenu");
      ele.setAttribute("class","menu");
      ele.style.display="none";
      this.container.append( ele );
      var ulul = d3.select("#panelmenu").append("ul");
			var arraymenu = this.menuDesc.split(":");
			for (var i = 0; i < arraymenu.length; i++) {
				var lia = ulul.append("li").append("a").style("cursor", "pointer").text(arraymenu[i]);
			}
    },
    addEvent:function(){
      var _this = this;
      var ulul = d3.select("#panelmenu").select("ul");
      ulul.selectAll("a").on("click", function (d, i) {
        var eventName = _this.eventNameMap[i] || 'openport';
				_this.clickMenuCall && _this.clickMenuCall(eventName);
			});
    },
    showMenuPanel:function(){
      var topy=5;
			d3.select('#panelmenu')
			.style('position', 'absolute')
			.style('left', position[0] + "px")
			.style('top', topy + "px")
			.style('display', 'inline-block')
			.on('mouseleave', function () {
				d3.select('#panelmenu').style('display', 'none');
			});
    },
    hideMenuPanel:function(){

    },
    setEnlargeBox:function(enlargeBox){
      this.enlargeBox = enlargeBox;
    },
    setLineColor:function(lineColor){
      this.lineColor = lineColor;
    }

  };
  cxt.svgdevicepanel.MenuPanel = MenuPanel;
})(window);
