
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
      this.uniqueId = options.uniqueId;
      this.clickMenuCall = options.clickMenuCall;
      this.eventNameMap = {"":"openport", "0":"openport", "1":"closeport", "2":"deviceip"};
      this.selectedNodeId = 0;
      this.menuWidth = 0;
      this.menuHeight = 0;
      this.isMouseIn = false;
      this.menuPanelD3 = null;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var ele = document.createElement( "div" );
      ele.setAttribute("id","menuPanel"+this.uniqueId);
      ele.setAttribute("class","menu");
      ele.style.display="none";
      $(this.container).append( ele );
      this.menuPanelD3 = d3.select(ele);
      var ulul = this.menuPanelD3.append("ul");
			var arraymenu = this.menuDesc.split(":");
			for (var i = 0; i < arraymenu.length; i++) {
				var lia = ulul.append("li").append("a").style("cursor", "pointer").text(arraymenu[i]);
			}
      this.menuWidth = $(ele).width();
      this.menuHeight = $(ele).height();
    },
    addEvent:function(){
      var _this = this;
      var ulul = this.menuPanelD3.select("ul");
      ulul.selectAll("a").on("click", function (d, i) {
        var eventName = _this.eventNameMap[i] || 'openport';
				_this.clickMenuCall && _this.clickMenuCall(eventName,_this.selectedNodeId);
			});
      this.menuPanelD3.on('mouseenter', function(){
        _this.isMouseIn = true;
      })
      .on('mouseleave', function(){
        _this.isMouseIn = false;
        _this.hideMenuPanel();
      });
    },
    showMenuPanel:function(position, selectedNodeId){
      this.selectedNodeId = selectedNodeId;
			this.menuPanelD3.style('left', (position.left+20) + "px")
			.style('top', (position.top) + "px")
			.style('display', 'inline-block');
    },
    hideMenuPanel:function(){
      if(!this.isMouseIn){
        this.menuPanelD3.style('display', 'none');
      }
    },
    getWidth:function(){
      return this.menuWidth;
    },
    getHeight:function(){
      return this.menuHeight;
    },
    dispose:function(){
      $("#menuPanel"+this.uniqueId).off().remove();
    }

  };
  cxt.svgdevicepanel.MenuPanel = MenuPanel;
})(window);
