'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
const FILEBEAT_CLOUD_INSTRUCTIONS = exports.FILEBEAT_CLOUD_INSTRUCTIONS = {
  CONFIG: {
    OSX: {
      title: 'Edit the configuration',
      textPre: 'Modify `filebeat.yml` to set the connection information for Elastic Cloud:',
      commands: ['cloud.id: "{config.cloud.id}"', 'cloud.auth: "elastic:<password>"'],
      textPost: 'Where `<password>` is the password of the `elastic` user.'
    },
    DEB: {
      title: 'Edit the configuration',
      textPre: 'Modify `/etc/filebeat/filebeat.yml` to set the connection information for Elastic Cloud:',
      commands: ['cloud.id: "{config.cloud.id}"', 'cloud.auth: "elastic:<password>"'],
      textPost: 'Where `<password>` is the password of the `elastic` user.'
    },
    RPM: {
      title: 'Edit the configuration',
      textPre: 'Modify `/etc/filebeat/filebeat.yml` to set the connection information for Elastic Cloud:',
      commands: ['cloud.id: "{config.cloud.id}"', 'cloud.auth: "elastic:<password>"'],
      textPost: 'Where `<password>` is the password of the `elastic` user.'
    },
    WINDOWS: {
      title: 'Edit the configuration',
      textPre: 'Modify `C:\\Program Files\\Filebeat\\filebeat.yml` to set the connection information for Elastic Cloud:',
      commands: ['cloud.id: "{config.cloud.id}"', 'cloud.auth: "elastic:<password>"'],
      textPost: 'Where `<password>` is the password of the `elastic` user.'
    }
  }
};