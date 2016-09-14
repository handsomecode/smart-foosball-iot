'use strict';

global.__base = __dirname + '/';
global.__app = __base + 'app/';

var slack = require(__app + 'actions/slack');
var schedule = require(__app + 'actions/schedule');
var firebase = require(__app + 'services/firebase');

slack.init();
schedule.weeklyStatsReport();
