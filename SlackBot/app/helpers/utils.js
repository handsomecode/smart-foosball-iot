'use strict';

module.exports = {
    generatePlayersString: function (playerList) {
        return playerList.reduce(function (playerString, player, index) {
            return playerString + (index ? ', ' : '') + '<@' + player.playerSlackId + '>'
        }, '');
    },

    isInFirebasePlayerList: function (userId, playerList) {
        for (var playerKey in playerList) {
            if (playerList[playerKey].slackID === userId) {
                return true;
            }
        }

        return false;
    },

    isInCurrentPlayerList: function (userId, playerList) {
        return playerList.some(function (player) {
            return userId === player.playerSlackId;
        });
    },

    getGamesStatistic: function (gameList) {
        console.log("IN getGamesStatistic");
        var playerList = {};
        var playerFields = ['idPlayerA1', 'idPlayerA2', 'idPlayerB1', 'idPlayerB2'];
        var gamesCount = 0;
        var goalsCount = 0;
        var topThreeWinners = {};
        //var timeDuringGames = 0; to dangerous for displaying :)
        for (var game in gameList) {
            gamesCount++;
            goalsCount = goalsCount + gameList[game].scoreA + gameList[game].scoreB;
            playerFields.forEach(function (field, i, playerFields) {
                if (gameList[game][field] !== "") {
                    if (playerList[gameList[game][field]] === undefined) {
                        playerList[gameList[game][field]] = {
                            wins: 0,
                            losses: 0
                        };
                    }
                    if (gameList[game].scoreA > gameList[game].scoreB) {
                        if (i < 2) {
                            playerList[gameList[game][field]].wins++;
                        } else {
                            playerList[gameList[game][field]].losses++;
                        }
                    } else {
                        if (i < 2) {
                            playerList[gameList[game][field]].losses++;
                        } else {
                            playerList[gameList[game][field]].wins++;
                        }
                    }
                }
            });
        }

        var top = {
            one: {wins: -1},
            two: {wins: -1},
            three: {wins: -1}
        };

        //TODO refactor with massive and add checks for equal wins
        for (var player in playerList) {
            if (playerList[player].wins >= top.one.wins) {
                top.three = top.two;
                top.two = top.one;
                top.one = playerList[player];
                top.one.id = player;
            } else if (playerList[player].wins >= top.two.wins) {
                top.three = top.two;
                top.two = playerList[player];
                top.two.id = player;
            } else if (playerList[player].wins >= top.three.wins) {
                top.three = playerList[player];
                top.three.id = player;
            }
        }

        return {top: top, goals: goalsCount, games: gamesCount};
    },

    isInCurrentPlayerActionList: function (userId, original_message) {
        for (var j = 0; j < 2; j++) {
            for (var i = 0; i < 2; i++) {
                if (original_message.attachments[j].actions[i].value === userId) {
                    return true;
                }
            }
        }
        return false;
    },

    generatePlayersStringFromActionMessage: function (userId, original_message) {
        var playerString = "";
        for (var j = 0; j < 2; j++) {
            for (var i = 0; i < 2; i++) {
                if (original_message.attachments[j].actions[i].value !== "")
                    playerString = playerString + "<@" + original_message.attachments[j].actions[i].value + "> ";
            }
        }
        return playerString;
    }
};
