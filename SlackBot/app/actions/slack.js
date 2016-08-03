'use strict';

var Botkit = require('botkit');
var Slack = require('slack-node');

var appConfig = require(__app + 'config');
var slackConfig = appConfig('slack');
var settingsConfig = appConfig('settings');

var utils = require(__app + 'helpers/utils');

var firebase = require(__app + 'services/firebase');

var slackBot = Botkit.slackbot({
    debug: true
});

slackBot
    .spawn({
        token: slackConfig.token
    })
    .startRTM(function (err) {
        if (err) {
            throw new Error(err);
        }
    });


var slack = new Slack(slackConfig.token);

// slack.api('chat.postMessage', {
//     text: 'hello from nodejs',
//     channel: '#general'
// }, function (err, response) {
//     console.log(response);
// });

module.exports = {
    playerListFromFirebase: [],

    playerListInCurrentGame: [],

    timerCleaning: 0,

    getUserData: function (userId) {
        return new Promise(function (resolve, reject) {
            slack.api('users.info', {
                user: userId
            }, function (error, response) {
                if (error) {
                    return reject(error);
                }

                if (response.statusCode !== 200) {
                    return reject(response);
                }

                resolve(response);
            });
        });
    },

    clearCurrentGame: function (bot, message) {
        bot.reply(message, 'Sorry guys, no one more want to play :sad: ' + utils.generatePlayersString(this.playerListInCurrentGame));

        this.playerListInCurrentGame = [];
    },

    initGameStartListener: function () {
        var self = this;

        slackBot.hears(['foosball', 'play', 'game'], ['direct_message', 'direct_mention', 'mention'], function (bot, message) {
            console.log('User slack ID ' + message.user);

            self.getUserData(message.user)
                .then(function (response) {
                    console.log('response ' + response); // Show the HTML for the Modulus homepage.
                    console.log('Real name' + response.user.profile.real_name_normalized);
                })
                .catch(function (error) {
                    console.error(error);
                });

            if (utils.isInFirebasePlayerList(message.user, self.playersList)) {
                if (!self.playerListInCurrentGame.length) {
                    self.timerCleaning = setTimeout(function () {
                        self.clearCurrentGame(bot, message);
                    }, 1 * 60 * 1000); // 5*60*1000 5 минут
                }

                if (!utils.isInCurrentPlayerList(message.user, self.playerListInCurrentGame)) {
                    self.playerListInCurrentGame.push({
                        playerSlackId: message.user
                    });
                    console.log(self.playerListInCurrentGame);
                    if (self.playerListInCurrentGame.length >= settingsConfig.maxPlayers) {
                        bot.reply(message, 'Hey guys ' + utils.generatePlayersString(self.playerListInCurrentGame) + '. You are next.');

                        self.clearCurrentGame(bot, message);
                    } else {
                        bot.reply(message, '<!here> Who want to play foosball with ' + utils.generatePlayersString(self.playerListInCurrentGame) + '.');
                    }
                } else {
                    bot.reply(message, '<@' + message.user + '> You are already in list.');
                }
            } else {
                bot.reply(message, '<@' + message.user + '> You are not registered in Handsome foosball team.');
            }
        });
    },

    init: function () {
        var self = this;

        firebase.getPlayers()
            .then(function (players) {
                self.playersList = players;

                self.initGameStartListener();
            });
    }
};
