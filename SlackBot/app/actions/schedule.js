var schedule = require('node-schedule');
var slackbot = require(__app + 'actions/slack');

module.exports = {
    weeklyStatsReport: function () {
        schedule.scheduleJob('0 12 * * 5', function () {
            slackbot.weeklyStatsReport();
        });
    }
};