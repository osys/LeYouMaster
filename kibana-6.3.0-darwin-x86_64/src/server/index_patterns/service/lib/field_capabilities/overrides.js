'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.mergeOverrides = mergeOverrides;

var _lodash = require('lodash');

const OVERRIDES = {
  _source: { type: '_source' },
  _index: { type: 'string' },
  _type: { type: 'string' },
  _id: { type: 'string' },
  _timestamp: {
    type: 'date',
    searchable: true,
    aggregatable: true
  },
  _score: {
    type: 'number',
    searchable: false,
    aggregatable: false
  }
};

/**
 *  Merge overrides for specific metaFields
 *
 *  @param  {FieldInfo} field
 *  @return {FieldInfo}
 */
function mergeOverrides(field) {
  if (OVERRIDES.hasOwnProperty(field.name)) {
    return (0, _lodash.merge)(field, OVERRIDES[field.name]);
  } else {
    return field;
  }
}