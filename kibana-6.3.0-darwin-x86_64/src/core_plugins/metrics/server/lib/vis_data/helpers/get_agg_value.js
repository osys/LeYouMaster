'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _lodash = require('lodash');

var _extended_stats_types = require('./extended_stats_types');

var _extended_stats_types2 = _interopRequireDefault(_extended_stats_types);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const aggFns = {
  max: _lodash.max,
  min: _lodash.min,
  sum: _lodash.sum,
  avg: values => (0, _lodash.sum)(values) / values.length
};

exports.default = (row, metric) => {
  // Extended Stats
  if ((0, _lodash.includes)(_extended_stats_types2.default, metric.type)) {
    const isStdDeviation = /^std_deviation/.test(metric.type);
    const modeIsBounds = ~['upper', 'lower'].indexOf(metric.mode);
    if (isStdDeviation && modeIsBounds) {
      return (0, _lodash.get)(row, `${metric.id}.std_deviation_bounds.${metric.mode}`);
    }
    return (0, _lodash.get)(row, `${metric.id}.${metric.type}`);
  }

  // Percentiles
  if (metric.type === 'percentile') {
    let percentileKey = `${metric.percent}`;
    if (!/\./.test(`${metric.percent}`)) {
      percentileKey = `${metric.percent}.0`;
    }
    return row[metric.id].values[percentileKey];
  }

  if (metric.type === 'percentile_rank') {
    const percentileRankKey = `${metric.value}`;
    return row[metric.id] && row[metric.id].values && row[metric.id].values[percentileRankKey];
  }

  if (metric.type === 'top_hit') {
    if (row[metric.id].doc_count === 0) return null;
    const hits = (0, _lodash.get)(row, [metric.id, 'docs', 'hits', 'hits'], []);
    const values = hits.map(doc => {
      return (0, _lodash.get)(doc, `_source.${metric.field}`, 0);
    });
    const aggWith = metric.agg_with && aggFns[metric.agg_with] || aggFns.avg;
    return aggWith(values);
  }

  // Derivatives
  const normalizedValue = (0, _lodash.get)(row, `${metric.id}.normalized_value`, null);

  // Everything else
  const value = (0, _lodash.get)(row, `${metric.id}.value`, null);
  return normalizedValue || value;
};

module.exports = exports['default'];