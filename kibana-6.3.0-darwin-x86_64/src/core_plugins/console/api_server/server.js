"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.resolveApi = resolveApi;
let _ = require("lodash");

function resolveApi(sense_version, apis, reply) {
  let result = {};
  _.each(apis, function (name) {
    {
      // for now we ignore sense_version. might add it in the api name later
      let api = require('./' + name);
      result[name] = api.asJson();
    }
  });

  return reply(result).type("application/json");
}