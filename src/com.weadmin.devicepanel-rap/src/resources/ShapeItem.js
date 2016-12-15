/********************************************
*date: 2016/12/15
*auther: yaoao.liu
* svgtype 1-<g> 2-<path>  3 -<ellipse> 4 - <rect> 5 -<polyline> 10-<text>
* base  linewidth fillcolor linecolor
* <g> itemid transform
* <path> ccsclass value
* <ellipse> ccsclass ecx ecy erx ery
* <polyline> value
* <rect> ecx-x ecy-y erx-width ery-height
*  fill-opacity
*  <text>  fontname  fontsize textanchor
*shapeitem
********************************************/
// TYPE HANDLER
if (!window.d3svgdevicepanel) {
  window.d3svgdevicepanel = {};
}
rap.registerTypeHandler( "d3svgdevicepanel.ShapeItem", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3svgdevicepanel.ShapeItem( parent );
  },

  destructor: "destroy",
  //"values", "color", ,"fillopacity" "text",
  properties: [ "value", "svid", "cssclass" , "fontname" , "fontsize" , "textanchor" , "linewidth" , "fillcolor" , "linecolor" , "svgtype" , "transform" , "itemid" , "ecx" ,"ecy","erx","ery"  ]
} );
d3svgdevicepanel.ShapeItem = function( parent ) {
  this._parent = parent;
  this._value = "";
  this._linewidth = 1;
  this._fillcolor = "";
  this._fontname  = "";
  this._fontsize  = "";
  this._textanchor = "";
  //this._fillopacity="1";
  this._linecolor = "";
  this._svgtype  = 0;
  this._transform = "";//group
  this._cssclass = "";
  this._ecx = "";
  this._ecy = "";
  this._erx = "";
  this._ery = "";
  this._itemid = "";
  this._svid="";
  //this._values = [];

  this._parent.addItem( this );
};

d3svgdevicepanel.ShapeItem.prototype = {

  getParent: function() {
    return this._parent;
  },
  getSvid: function(){
   return this._svid;
  },

  setSvid: function( svid ){
   this._svid=svid;
  },
  getLinewidth: function(){
   return this._linewidth;
  },

  setLinewidth: function( linewidth ){
   this._linewidth=linewidth;
  },
  getFillcolor: function(){
   return this._fillcolor;
  },

  setFillcolor: function( fillcolor ){
   this._fillcolor=fillcolor;
  },

  getFontname: function(){
   return this._fontname;
  },

  setFontname: function( fontname ){
   this._fontname=fontname;
  },

  getFontsize: function(){
   return this._fontsize;
  },

  setFontsize: function( fontsize ){
   this._fontsize=fontsize;
  },

  getTextanchor: function(){
   return this._textanchor;
  },

  setTextanchor: function( textanchor ){
   this._textanchor=textanchor;
  },
  // getFillopacity: function(){
  // return this._fillopacity;
  //},

  //setFillopacity: function( fillopacity ){
  // this._fillopacity=fillopacity;
 // },
  getLinecolor: function(){
   return this._linecolor;
  },

  setLinecolor: function( linecolor ){
   this._linecolor=linecolor;
  },
  getTransform: function(){
    return this._transform;
  },
  setTransform: function(transform){
    this._transform=transform;
  },

  getSvgtype: function(){
   return this._svgtype;
  },

  setSvgtype: function( svgtype ){
   this._svgtype=svgtype;
  },

  getItemid: function(){
    return this._itemid;
  },
  setItemid: function( itemid ){
    this._itemid = itemid;
  },
  getValue: function() {
    return this._value;
  },
  setValue: function( value ) {
    this._value = value;
   // this._parent._svgMap._scheduleUpdate();
  },
  getCssclass: function(){
    return this._cssclass;
  },
  setCssclass: function( cssclass ){
    this._cssclass = cssclass;
  },
  getEcx: function(){
    return this._ecx;
  },
  setEcx: function( ecx ){
    this._ecx = ecx;
  },
  getEcy: function(){
    return this._ecy;
  },
  setEcy: function( ecy ){
    this._ecy = ecy;
  },
  getErx: function(){
    return this._erx;
  },
  setErx: function( erx ){
    this._erx = erx;
  },
  getEry: function(){
    return this._ery;
  },
  setEry: function( ery ){
    this._ery = ery;
  },
 // getValues: function() {
 //   return this._values;
 // },

 // setValues: function( values ) {
 //   this._values = values;
 //   this._parent._svgMap._scheduleUpdate();
 // },

  //getColor: function() {

  //  return "#003399";
    //return this._color;
 // },

 // setColor: function( color ) {
 //   var hex = function( value ) { return ( value < 16 ? "0" : "" ) + value.toString( 16 ); };
 //   this._color = "#" + hex( color[0] ) + hex( color[1] ) + hex( color[2] );
 //   this._parent._svgMap._scheduleUpdate();
 // },

 // getText: function() {
 //   return this._text;
 // },

 // setText: function( text ) {
  //  this._text = text;
  //  this._parent._svgMap._scheduleUpdate();
  //},

  id: function() {
    return this._rwtId;
  },
  destroy: function() {
    this._parent.removeItem( this );
  }
};
