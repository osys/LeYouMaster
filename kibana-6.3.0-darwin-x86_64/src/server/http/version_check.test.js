'use strict';

var _bluebird = require('bluebird');

var _path = require('path');

var _kbn_server = require('../../test_utils/kbn_server');

var kbnTestServer = _interopRequireWildcard(_kbn_server);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

const src = _path.resolve.bind(null, __dirname, '../../../src');

const versionHeader = 'kbn-version';
const version = require(src('../package.json')).version;

describe('version_check request filter', function () {
  function makeRequest(kbnServer, opts) {
    return (0, _bluebird.fromNode)(cb => {
      kbnTestServer.makeRequest(kbnServer, opts, resp => {
        cb(null, resp);
      });
    });
  }

  async function makeServer() {
    const kbnServer = kbnTestServer.createServer();

    await kbnServer.ready();

    kbnServer.server.route({
      path: '/version_check/test/route',
      method: 'GET',
      handler: function (req, reply) {
        reply(null, 'ok');
      }
    });

    return kbnServer;
  }

  let kbnServer;
  beforeEach(async () => kbnServer = await makeServer());
  afterEach(async () => await kbnServer.close());

  it('accepts requests with the correct version passed in the version header', async function () {
    const resp = await makeRequest(kbnServer, {
      url: '/version_check/test/route',
      method: 'GET',
      headers: {
        [versionHeader]: version
      }
    });

    expect(resp.statusCode).toBe(200);
    expect(resp.payload).toBe('ok');
  });

  it('rejects requests with an incorrect version passed in the version header', async function () {
    const resp = await makeRequest(kbnServer, {
      url: '/version_check/test/route',
      method: 'GET',
      headers: {
        [versionHeader]: `invalid:${version}`
      }
    });

    expect(resp.statusCode).toBe(400);
    expect(resp.headers).toHaveProperty(versionHeader, version);
    expect(resp.payload).toMatch(/"Browser client is out of date/);
  });

  it('accepts requests that do not include a version header', async function () {
    const resp = await makeRequest(kbnServer, {
      url: '/version_check/test/route',
      method: 'GET'
    });

    expect(resp.statusCode).toBe(200);
    expect(resp.payload).toBe('ok');
  });
});