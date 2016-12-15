/*******************************************************************************
 *
 ******************************************************************************/
/*
 * An array of items with additional add() and remove() methods.
 */
 if (!window.d3svgdevicepanel) {
   window.d3svgdevicepanel = {};
 }
d3svgdevicepanel.ItemList = function() {
  this.add = function( item ) {
    this.push( item );
  };
  this.remove = function( item ) {
    var index;
    while( ( index = this.indexOf( item ) ) !== -1 ) {
      this.splice( index, 1 );
    }
  };
};
d3svgdevicepanel.ItemList.prototype = Array.prototype;
