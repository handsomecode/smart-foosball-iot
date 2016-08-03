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
        playerList.some(function (player) {
            return userId === player.playerSlackId;
        });
    }
};
