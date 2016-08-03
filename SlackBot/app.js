'use strict';

global.__base = __dirname + '/';
global.__app = __base + 'app/';

var slack = require(__app + 'actions/slack');

slack.init();
