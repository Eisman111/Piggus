/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module unless amdModuleId is set
    define(["jquery"], function (a0) {
      return (factory(a0));
    });
  } else if (typeof exports === 'object') {
    // Node. Does not work with strict CommonJS, but
    // only CommonJS-like environments that support module.exports,
    // like Node.
    module.exports = factory(require("jquery"));
  } else {
    factory(jQuery);
  }
}(this, function (jQuery) {

(function ($) {
  $.fn.selectpicker.defaults = {
    noneSelectedText: 'Tidak ada yang dipilih',
    noneResultsText: 'Tidak ada yang cocok {0}',
    countSelectedText: '{0} terpilih',
    maxOptionsText: ['Mencapai batas (maksimum {n})', 'Mencapai batas grup (maksimum {n})'],
    selectAllText: 'Pilih Semua',
    deselectAllText: 'Hapus Semua',
    multipleSeparator: ', '
  };
})(jQuery);


}));
