
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
      this.selectedNodeId = 0;
      this.isMouseIn = false;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var ele = document.createElement( "div" );
      ele.setAttribute("id","menuPanel");
      ele.setAttribute("class","menu");
      ele.style.display="none";
      $(this.container).append( ele );
      var ulul = d3.select("#menuPanel").append("ul");
			var arraymenu = this.menuDesc.split(":");
			for (var i = 0; i < arraymenu.length; i++) {
				var lia = ulul.append("li").append("a").style("cursor", "pointer").text(arraymenu[i]);
			}
    },
    addEvent:function(){
      var _this = this;
      var ulul = d3.select("#menuPanel").select("ul");
      ulul.selectAll("a").on("click", function (d, i) {
        var eventName = _this.eventNameMap[i] || 'openport';
				_this.clickMenuCall && _this.clickMenuCall(eventName,_this.selectedNodeId);
			});
      d3.select("#menuPanel").on('mouseenter', function(){
        _this.isMouseIn = true;
      })
      .on('mouseleave', function(){
        _this.isMouseIn = false;
        _this.hideMenuPanel();
      });
    },
    showMenuPanel:function(position, selectedNodeId){
      this.selectedNodeId = selectedNodeId;
      var height = $("#munuPanel").height();
			d3.select('#menuPanel')
			.style('left', (position.left+20) + "px")
			.style('top', (position.top) + "px")
			.style('display', 'inline-block');
    },
    hideMenuPanel:function(){
      if(!this.isMouseIn){
        d3.select('#menuPanel').style('display', 'none');
      }
    }

  };
  cxt.svgdevicepanel.MenuPanel = MenuPanel;
})(window);
