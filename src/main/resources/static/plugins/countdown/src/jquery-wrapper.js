/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

var Countdown = require('./countdown.js');
var NAME = 'countdown';
var DATA_ATTR = 'date';

jQuery.fn.countdown = function(options) {
  return $.each(this, function(i, el) {
    var $el = $(el);
    if (!$el.data(NAME)) {
      // allow setting the date via the data-date attribute
      if ($el.data(DATA_ATTR)) {
        options.date = $el.data(DATA_ATTR);
      }
      $el.data(NAME, new Countdown(el, options));
    }
  });
};

module.exports = Countdown;
