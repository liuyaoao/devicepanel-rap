
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
      this.dialogJq.append('<div class="showIn_Out">收起</div>');
      // this.dialogJq.append('<div class="dialogCloseBtn">x</div>');
      var ulul = $("<ul></ul>");
      this.dialogJq.append(ulul);
			this.addNameListToContainer(ulul);
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
          _this.dialogJq.css({"left":(evt.clientX-offsetPos.left-20)+"px","top":(evt.clientY-offsetPos.top-10)+"px"});
        }
      });
      this.dialogJq.find("ul").on('mousedown','li a',function(evt){
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
        var portName = $(".mousedown"+_this.uniqueId).attr('data-portname');
        var portEle = $('.mouseover'+_this.uniqueId);
        if(portName && portEle.size()>0){
          var changePortArr = _this.setPortName(portName,portEle);
          changePortArr.length>0 ? _this.refreshNameState() : null;
          changePortArr.length>0 ? $(_this.container).trigger("customEvt.portChanged",changePortArr) : null;
        }
        _this.mouseUpDispose();
      });
      $(this.container).on('mouseleave',function(evt){
        _this.mouseUpDispose();
      });
      $(this.container).find('.portNameDialog').on('click','.showIn_Out',function(evt){
        var that = $(this);
        var parent = that.closest(".portNameDialog");
        if(that.text() =="收起"){
          parent.find("ul").slideUp();
          that.text("展示");
        }else{
          parent.find("ul").slideDown();
          that.text("收起");
        }
      });
    },
    mouseUpDispose:function(){ //当鼠标弹起或这移出了最外层容器的时候。
      var _this = this;
      $(".mousedownBack").removeClass('mousedownBack');
      $(_this.container).find(".mousedown"+_this.uniqueId).remove();
      _this.mousedownEle = null;
      _this.canMoveDialog = false;
    },
    // 绑定接口名到某个端口位置。返回所改变的端口的索引好和接口名数组。[oldPortNum,oldPortName,newPortNum,newPortName]
    setPortName:function(portName,portEle){
      var changePortArr = [];
      var portNum = portEle.attr('data-portnum');
      var oldPortEle = $(this.container).find("svg g[data-portname='"+portName+"']");
      var oldPortNum = oldPortEle.attr('data-portnum');
      var oldPortName = portEle.attr('data-portname');
      if(portNum == oldPortNum ){
        return changePortArr;
      }
      //delete old
      changePortArr = [oldPortNum,oldPortName,portNum,portName];
      if(oldPortEle && oldPortEle.size()>0){ //如果选定的该接口名已经绑定了某个端口位置了。则要在旧的端口先去掉绑定。
        oldPortEle.attr('data-portname',"");
        var v_cpOldArr = oldPortEle[0].getElementsByTagName("v:cp");
        for(var i=0;i<v_cpOldArr.length;i++){
          var el = $(v_cpOldArr[i]);
          var nameCn = el.attr('v:lbl') || "";
          if(nameCn == "interfaceName"){
            el.attr("v:val","");
          }
        }
      }
      //add new
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
      return changePortArr;
    },
    addNameListToContainer:function(ulContainer){
      for (var i = 0; i < this.portNameList.length; i++) {
        var lia = $("<li></li>");
				ulContainer.append(lia);
        var aTag = $("<a href='javascript:;' data-portname='"+this.portNameList[i]+"'>"+this.portNameList[i]+"</a>");
        lia.append(aTag);
			}
    },
    updateNameList:function(interfaceNameList){
      this.portNameList = interfaceNameList;
      this.dialogJq.find('ul').empty();
      this.addNameListToContainer(this.dialogJq.find('ul'));
      this.refreshNameState();
    },
    refreshNameState:function(){ //更新接口名列表的状态，标识哪些已经绑定了端口。
      var _this = this;
      var portGElArr = $(this.container).find("svg g[data-portname]");
      $(this.container).find(".portNameDialog .nameUsed").removeClass("nameUsed");
      portGElArr.each(function(){
        var that = $(this);
        var portName = that.attr("data-portname");
        $(_this.container).find(".portNameDialog a[data-portname='"+portName+"']").addClass("nameUsed");
      });
    },
    dispose:function(){
      $("#portNameDialog_"+this.uniqueId).off().remove();
    }

  };
  cxt.svgdevicepanel.PortNameDialog = PortNameDialog;
})(window);
