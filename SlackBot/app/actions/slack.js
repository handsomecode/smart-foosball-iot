'use strict';

var Botkit = require('botkit');
var Slack = require('slack-node');

var appConfig = require(__app + 'config');
var slackConfig = appConfig('slack');
var settingsConfig = appConfig('settings');

var utils = require(__app + 'helpers/utils');

var firebase = require(__app + 'services/firebase');

var appHttps = require(__app + 'services/https').getApp();

// just a simple way to make sure we don't
// connect to the RTM twice for the same team
var _bots = {};

var slackBot = Botkit.slackbot({
    interactive_replies: true,
    debug: true
}).configureSlackApp(
    {
        clientId: slackConfig.clientId,
        clientSecret: slackConfig.clientSecret,
        redirectUri: slackConfig.redirectUri,
        scopes: ['bot', 'users:read']
    }
);

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

                // if (response.statusCode !== 200) {
                //     return reject(response);
                // }

                resolve(response);
            });
        });
    },

    constructEphemeralMessage: function (text) {
        return {
            "text": text,
            "response_type": "ephemeral",
            "replace_original": false,
            "delete_original": false
        };
    },

    initInteractiveFoosballButtons: function () {
        var self = this;

        //simple message
        var defaultmessage = {
            "text": "<!here> Who wants some foosball?",
            "attachments": [
                {
                    "text": "Team A",
                    "fallback": "You are unable to choose a game",
                    "callback_id": "teamA",
                    "color": "#AA0000",
                    "attachment_type": "default",
                    "actions": [
                        {
                            "name": "player1",
                            "text": "Player 1",
                            "type": "button",
                            "value": ""
                        },
                        {
                            "name": "player2",
                            "text": "Player 2",
                            "type": "button",
                            "value": ""
                        }
                    ]
                },
                {
                    "text": "Team B",
                    "fallback": "You are unable to choose a game",
                    "callback_id": "teamB",
                    "color": "#0000AA",
                    "attachment_type": "default",
                    "actions": [
                        {
                            "name": "player1",
                            "text": "Player 1",
                            "type": "button",
                            "value": ""
                        },
                        {
                            "name": "player2",
                            "text": "Player 2",
                            "type": "button",
                            "value": ""
                        }
                    ]
                }
            ]
        };

        slackBot.createHomepageEndpoint(appHttps);

        slackBot.createWebhookEndpoints(appHttps);

        slackBot.createOauthEndpoints(appHttps, function (err, req, res) {
            if (err) {
                res.status(500).send('ERROR: ' + err);
            } else {
                res.send('Success!');
            }
        });

        // slackBot
        //     .spawn({
        //         token: 'xoxb-68389405730-3to2vTFhezVHYokL4uZXB7hl'
        //     })
        //     .startRTM(function (err) {
        //         if (err) {
        //             throw new Error(err);
        //         }
        //     });

        function trackBot(bot) {
            _bots[bot.config.token] = bot;
        }

        slackBot.on('create_bot', function (bot, config) {

            console.log('bot config = ' + JSON.stringify(bot.config, null, 4));

            if (_bots[bot.config.token]) {
                // already online! do nothing.
            } else {
                bot.startRTM(function (err) {

                    if (!err) {
                        trackBot(bot);
                    }

                    bot.startPrivateConversation({user: config.createdBy}, function (err, convo) {
                        if (err) {
                            console.log(err);
                        } else {
                            convo.say('I am a bot that has just joined your team');
                            convo.say('You must now /invite me to a channel so that I can be of use!');
                        }
                    });

                });
            }

        });

        slackBot.hears(['game', 'play'], ['direct_mention'], function (bot, message) {
            bot.reply(message, defaultmessage);
        });

        // receive an interactive message, and reply with a message that will replace the original
        slackBot.on('interactive_message_callback', function (bot, message) {
                console.log("request info about " + message.user);
                // if (utils.isInFirebasePlayerList(message.user, self.playersList)) {
                //TODO mb it will be effective to create function to calculate team inex and player index
                var team;
                if (message.callback_id === "teamA") {
                    team = 0;
                } else {
                    team = 1;
                }
                var player;
                if (message.actions[0].name === "player1") {
                    player = 0;
                } else {
                    player = 1;
                }
                if (!utils.isInCurrentPlayerActionList(message.user, message.original_message)) {
                    if (message.actions[0].value === "") {
                        self.getUserData(message.user).then(function (response) {
                            console.log("success\n" + JSON.stringify(response, null, 4));
                            var user_info = response;
                            var new_message = message.original_message;
                            console.log("++++");
                            console.log(message);
                            console.log("----");
                            // self.playerListInCurrentGame[team * 2 + player] = {};
                            // self.playerListInCurrentGame[team * 2 + player].playerSlackId = message.user;
                            new_message.attachments[team].actions[player].text =
                                user_info.user.profile.real_name_normalized;
                            new_message.attachments[team].actions[player].value = message.user;
                            new_message.attachments[team].actions[player].style = "primary";
                            console.log(new_message.attachments[team].actions[player]);
                            bot.replyInteractive(message, new_message);
                            bot.replyInteractive(message, self.constructEphemeralMessage("You will take place"));
                            var playercount = 0;
                            for (var j = 0; j < 2; j++) {
                                for (var i = 0; i < 2; i++) {
                                    if (message.original_message.attachments[j].actions[i].value !== "") {
                                        playercount++;
                                    }
                                }
                            }
                            if (playercount >= 4) {
                                bot.reply(message, 'Hey guys '
                                    + utils.generatePlayersStringFromActionMessage(message.user,
                                        message.original_message)
                                    + '. You are next.');
                                self.playerListInCurrentGame = [];
                            }
                        }).catch(function (err) {
                            console.log("error\n" + JSON.stringify(err, null, 4));
                        });
                    } else {
                        bot.replyInteractive(message,
                            self.constructEphemeralMessage("Sorry place has been already taken."));
                    }
                } else {
                    if (message.actions[0].value === message.user) {
                        var new_message = message.original_message;
                        new_message.attachments[team].actions[player] =
                            defaultmessage.attachments[team].actions[player];
                        bot.replyInteractive(message, new_message);
                        bot.replyInteractive(message,
                            self.constructEphemeralMessage("Slot has been released."));
                    } else {
                        bot.replyInteractive(message,
                            self.constructEphemeralMessage("You are already in list."));
                    }
                }
                // } else {
                //     bot.replyInteractive(message, self.constructEphemeralMessage("You are not registered in Handsome foosball team."));
                // }
            }
        );
    },


    init: function () {
        var self = this;

        firebase.getPlayers()
            .then(function (players) {
                self.playersList = players;

                self.initInteractiveFoosballButtons();
            });
    }
};
