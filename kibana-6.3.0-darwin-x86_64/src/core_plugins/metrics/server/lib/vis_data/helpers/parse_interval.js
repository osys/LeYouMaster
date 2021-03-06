'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.parseInterval = parseInterval;

var _lodash = require('lodash');

var _lodash2 = _interopRequireDefault(_lodash);

var _moment = require('moment');

var _moment2 = _interopRequireDefault(_moment);

var _datemath = require('@kbn/datemath');

var _datemath2 = _interopRequireDefault(_datemath);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Assume interval is in the form (value)(unit), such as "1h"
const INTERVAL_STRING_RE = new RegExp('^([0-9\\.]*)\\s*(' + _datemath2.default.units.join('|') + ')$');

function parseInterval(interval) {
  const matches = String(interval).trim().match(INTERVAL_STRING_RE);

  if (!matches) return null;

  try {
    const value = parseFloat(matches[1]) || 1;
    const unit = matches[2];

    const duration = _moment2.default.duration(value, unit);

    // There is an error with moment, where if you have a fractional interval between 0 and 1, then when you add that
    // interval to an existing moment object, it will remain unchanged, which causes problems in the ordered_x_keys
    // code. To counteract this, we find the first unit that doesn't result in a value between 0 and 1.
    // For example, if you have '0.5d', then when calculating the x-axis series, we take the start date and begin
    // adding 0.5 days until we hit the end date. However, since there is a bug in moment, when you add 0.5 days to
    // the start date, you get the same exact date (instead of being ahead by 12 hours). So instead of returning
    // a duration corresponding to 0.5 hours, we return a duration corresponding to 12 hours.
    const selectedUnit = _lodash2.default.find(_datemath2.default.units, unit => {
      return Math.abs(duration.as(unit)) >= 1;
    });

    return _moment2.default.duration(duration.as(selectedUnit), selectedUnit);
  } catch (e) {
    return null;
  }
}