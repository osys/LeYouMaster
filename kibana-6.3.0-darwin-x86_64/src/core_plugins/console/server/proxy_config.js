'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ProxyConfig = undefined;

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _lodash = require('lodash');

var _url = require('url');

var _https = require('https');

var _fs = require('fs');

var _wildcard_matcher = require('./wildcard_matcher');

class ProxyConfig {
  constructor(config) {
    config = _extends({}, config);

    // -----
    // read "match" info
    // -----
    const rawMatches = _extends({}, config.match);
    this.id = (0, _url.format)({
      protocol: rawMatches.protocol,
      hostname: rawMatches.host,
      port: rawMatches.port,
      pathname: rawMatches.path
    }) || '*';

    this.matchers = {
      protocol: new _wildcard_matcher.WildcardMatcher(rawMatches.protocol),
      host: new _wildcard_matcher.WildcardMatcher(rawMatches.host),
      port: new _wildcard_matcher.WildcardMatcher(rawMatches.port),
      path: new _wildcard_matcher.WildcardMatcher(rawMatches.path, '/')
    };

    // -----
    // read config vars
    // -----
    this.timeout = config.timeout;
    this.sslAgent = this._makeSslAgent(config);
  }

  _makeSslAgent(config) {
    const ssl = config.ssl || {};
    this.verifySsl = ssl.verify;

    const sslAgentOpts = {
      ca: ssl.ca && ssl.ca.map(ca => (0, _fs.readFileSync)(ca)),
      cert: ssl.cert && (0, _fs.readFileSync)(ssl.cert),
      key: ssl.key && (0, _fs.readFileSync)(ssl.key)
    };

    if ((0, _lodash.values)(sslAgentOpts).filter(Boolean).length) {
      sslAgentOpts.rejectUnauthorized = this.verifySsl == null ? true : this.verifySsl;
      return new _https.Agent(sslAgentOpts);
    }
  }

  getForParsedUri({ protocol, hostname, port, pathname }) {
    let match = this.matchers.protocol.match(protocol.slice(0, -1));
    match = match && this.matchers.host.match(hostname);
    match = match && this.matchers.port.match(port);
    match = match && this.matchers.path.match(pathname);

    if (!match) return {};
    return {
      timeout: this.timeout,
      rejectUnauthorized: this.sslAgent ? undefined : this.verifySsl,
      agent: protocol === 'https:' ? this.sslAgent : undefined
    };
  }
}
exports.ProxyConfig = ProxyConfig;