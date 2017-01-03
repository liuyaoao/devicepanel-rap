
;(function(cxt){
  if (!window.svgdevicepanel) {
		window.svgdevicepanel = {};
	}
  var PortNameDialog = function(){
    this.init.apply(this, arguments);
  }
  PortNameDialog.prototype = {
    init:function(options){
      this.container = options.container;
      this.uniqueId = options.uniqueId;
      this.dialogJq = null;
      // this.clickMenuCall = options.clickMenuCall;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var ele = document.createElement( "div" );
      ele.setAttribute("id","portNameDialog_"+this.uniqueId);
      ele.setAttribute("class","portNameDialog");
      $(this.container).append( ele );
      this.dialogJq = $(ele);

    },
    addEvent:function(){
      var _this = this;

    },

    dispose:function(){
      $("#portNameDialog_"+this.uniqueId).off().remove();
    }

  };
  cxt.svgdevicepanel.PortNameDialog = PortNameDialog;
})(window);
