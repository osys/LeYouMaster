'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _alter = require('../lib/alter.js');

var _alter2 = _interopRequireDefault(_alter);

var _chainable = require('../lib/classes/chainable');

var _chainable2 = _interopRequireDefault(_chainable);

var _lib = require('../../common/lib');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = new _chainable2.default('legend', {
  args: [{
    name: 'inputSeries',
    types: ['seriesList']
  }, {
    name: 'position',
    types: ['string', 'boolean', 'null'],
    help: 'Corner to place the legend in: nw, ne, se, or sw. You can also pass false to disable the legend',
    suggestions: [{
      name: 'false',
      help: 'disable legend'
    }, {
      name: 'nw',
      help: 'place legend in north west corner'
    }, {
      name: 'ne',
      help: 'place legend in north east corner'
    }, {
      name: 'se',
      help: 'place legend in south east corner'
    }, {
      name: 'sw',
      help: 'place legend in south west corner'
    }]
  }, {
    name: 'columns',
    types: ['number', 'null'],
    help: 'Number of columns to divide the legend into'
  }, {
    name: 'showTime',
    types: ['boolean'],
    help: 'Show time value in legend when hovering over graph. Default: true'
  }, {
    name: 'timeFormat',
    types: ['string'],
    help: `moment.js format pattern. Default: ${_lib.DEFAULT_TIME_FORMAT}`
  }],
  help: 'Set the position and style of the legend on the plot',
  fn: function legendFn(args) {
    return (0, _alter2.default)(args, function (eachSeries, position, columns, showTime = true, timeFormat = _lib.DEFAULT_TIME_FORMAT) {
      eachSeries._global = eachSeries._global || {};
      eachSeries._global.legend = eachSeries._global.legend || {};
      eachSeries._global.legend.noColumns = columns;
      eachSeries._global.legend.showTime = showTime;
      eachSeries._global.legend.timeFormat = timeFormat;

      if (position === false) {
        eachSeries._global.legend.show = false;
        eachSeries._global.legend.showTime = false;
      } else {
        eachSeries._global.legend.position = position;
      }

      return eachSeries;
    });
  }
});
module.exports = exports['default'];