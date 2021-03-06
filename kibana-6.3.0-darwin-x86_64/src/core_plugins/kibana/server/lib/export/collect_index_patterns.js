'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.collectIndexPatterns = collectIndexPatterns;
async function collectIndexPatterns(savedObjectsClient, panels) {
  const docs = panels.reduce((acc, panel) => {
    const { kibanaSavedObjectMeta, savedSearchId } = panel.attributes;

    if (kibanaSavedObjectMeta && kibanaSavedObjectMeta.searchSourceJSON && !savedSearchId) {
      let searchSource;
      try {
        searchSource = JSON.parse(kibanaSavedObjectMeta.searchSourceJSON);
      } catch (err) {
        return acc;
      }

      if (searchSource.index && !acc.find(s => s.id === searchSource.index)) {
        acc.push({ type: 'index-pattern', id: searchSource.index });
      }
    }
    return acc;
  }, []);

  if (docs.length === 0) return [];

  const { saved_objects: savedObjects } = await savedObjectsClient.bulkGet(docs);
  return savedObjects;
}