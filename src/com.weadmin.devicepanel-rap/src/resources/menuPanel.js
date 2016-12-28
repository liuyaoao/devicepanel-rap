
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
      this.menuPanelJq = null;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var ele = document.createElement( "div" );
      ele.setAttribute("id","menuPanel"+this.uniqueId);
      ele.setAttribute("class","menu");
      ele.style.display="none";
      $(this.container).append( ele );
      this.menuPanelJq = $(ele);
      var ulul = $("<ul></ul>");
      this.menuPanelJq.append(ulul);
			var arraymenu = this.menuDesc.split(":");
			for (var i = 0; i < arraymenu.length; i++) {
        var lia = $("<li></li>");
				ulul.append(lia);
        var aTag = $("<a href='javascript:;' data-index='"+i+"'></a>");
        lia.append(aTag);
        aTag.css("cursor", "pointer").text(arraymenu[i]);
			}
      this.menuWidth = this.menuPanelJq.width();
      this.menuHeight = this.menuPanelJq.height();
    },
    addEvent:function(){
      var _this = this;
      var ulul = this.menuPanelJq.find("ul");
      ulul.find("a").on("click", function () {
        var index = $(this).attr("data-index");
        var eventName = _this.eventNameMap[index] || 'openport';
				_this.clickMenuCall && _this.clickMenuCall(eventName,_this.selectedNodeId);
			});
      this.menuPanelJq.on('mouseenter', function(){
        _this.isMouseIn = true;
      })
      .on('mouseleave', function(){
        _this.isMouseIn = false;
        _this.hideMenuPanel();
      });
    },
    showMenuPanel:function(position, selectedNodeId){
      this.selectedNodeId = selectedNodeId;
			this.menuPanelJq.css('left', (position.left+20) + "px")
			.css('top', (position.top) + "px")
			.css('display', 'inline-block');
    },
    hideMenuPanel:function(){
      if(!this.isMouseIn){
        this.menuPanelJq.css('display', 'none');
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
