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
