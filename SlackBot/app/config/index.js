'use strict';

var jsonfile = require('jsonfile');

module.exports = function (field) {
    return jsonfile.readFileSync(__app + 'config/config.json')[field];
};
