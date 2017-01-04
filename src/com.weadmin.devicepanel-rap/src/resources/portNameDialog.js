
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
      this.mousedownEle = null;
      this.canMoveDialog = false;
      this.portNameList = ["portName_1","portName_2","portName_3","portName_4","portName_5","portName_6","portName_7","portName_8","portName_9","portName_10"
        ,"portName_11","portName_12","portName_13","portName_14","portName_15","portName_16","portName_17","portName_18","portName_19","portName_10"
        ];
      // this.clickMenuCall = options.clickMenuCall;
      this.initElement();
      this.addEvent();
    },
    initElement:function(){
      var _this = this;
      var ele = document.createElement( "div" );
      ele.setAttribute("id","portNameDialog_"+this.uniqueId);
      ele.setAttribute("class","portNameDialog menu");
      $(this.container).append( ele );
      this.dialogJq = $(ele);
      this.dialogJq.append('<div class="dialogHeader"></div>');
      // this.dialogJq.append('<div class="dialogCloseBtn">x</div>');
      var ulul = $("<ul></ul>");
      this.dialogJq.append(ulul);
			for (var i = 0; i < this.portNameList.length; i++) {
        var lia = $("<li></li>");
				ulul.append(lia);
        var aTag = $("<a href='javascript:;' data-index='"+i+"'></a>");
        lia.append(aTag);
        aTag.css("cursor", "pointer").text(this.portNameList[i]);
			}
    },
    addEvent:function(){
      var _this = this;
      this.dialogJq.find(".dialogHeader").on('mousedown',function(evt){
        _this.canMoveDialog = true;
      });
      this.dialogJq.find(".dialogHeader .dialogCloseBtn").on('click',function(evt){

      });
      $(this.container).on('mousemove',function(evt){
        var offsetPos = $(_this.container).offset();
        if(_this.mousedownEle){
          // console.log("mousemove event object:",evt);
          _this.mousedownEle.css({"left":(evt.clientX-offsetPos.left)+"px","top":(evt.clientY-offsetPos.top+10)+"px"});
        }
        if(_this.canMoveDialog){
          // var pos = _this.dialogJq.position();
          _this.dialogJq.css({"left":(evt.clientX-offsetPos.left-20)+"px","top":(evt.clientY-offsetPos.top-10)+"px"});
        }
      });
      this.dialogJq.find("a").on('mousedown',function(evt){
        $(this).addClass('mousedownBack');
        var portName = $.trim($(this).text());
        _this.mousedownEle = $(this).clone();
        _this.mousedownEle.attr("class","mousedown"+_this.uniqueId);
        _this.mousedownEle.attr("data-portname",portName);
        _this.mousedownEle.addClass("mousedownEle");
        $(_this.container).append( _this.mousedownEle );
        // _this.mousedownEle.css({"position":"absolute","left":evt.clientX+"px","top":evt.clienY+"px"});
        // console.log("mousedown portName:"+portName);
      });
      $(this.container).on('mouseup',function(evt){
        // console.log("mouseup event object:",evt);
        $(".mousedownBack").removeClass('mousedownBack');
        var portName = $(".mousedown"+_this.uniqueId).attr('data-portname');
        var portEle = $('.mouseover'+_this.uniqueId);
        if(portName && portEle.size()>0){
          _this.setPortName(portName,portEle);
        }
        $(_this.container).find(".mousedown"+_this.uniqueId).remove();
        _this.mousedownEle = null;
        _this.canMoveDialog = false;
      });
    },
    setPortName:function(portName,portEle){
      portEle.attr('data-portname', portName);
      var v_cpEleArr = portEle[0].getElementsByTagName("v:cp");
      for(var i=0;i<v_cpEleArr.length;i++){
        var el = $(v_cpEleArr[i]);
        var nameCn = el.attr('v:lbl') || "";
        if(nameCn == "interfaceName"){
          el.attr("v:val","VT4("+portName+")");
          console.log("setPortName succ:"+portName);
        }
      }
    },
    updateNameList:function(interfaceNameList){
      this.portNameList = interfaceNameList;
      //TODO
    },
    dispose:function(){
      $("#portNameDialog_"+this.uniqueId).off().remove();
    }

  };
  cxt.svgdevicepanel.PortNameDialog = PortNameDialog;
})(window);
