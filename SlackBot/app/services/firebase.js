'use strict';

var Firebase = require('firebase');

var moment = require('moment');

var firebaseConfig = require(__app + 'config')('firebase');

Firebase.initializeApp(firebaseConfig);

var firebaseDatabase = Firebase.database();

var games;

function getNewGamesFrom(dateFrom) {
    return new Promise(function (resolve) {
        firebaseDatabase.ref('/games/')
            .orderByChild('dateStart')
            .startAt(dateFrom)
            .on('value', function (snapshot) {
                //console.log(JSON.stringify(snapshot.val(), "", 4));
                resolve(snapshot.val());
            });
    });
}

module.exports = {
    getPlayers: function () {
        return new Promise(function (resolve) {
            firebaseDatabase.ref('/players/').on('value', function (snapshot) {
                resolve(snapshot.val());
            });
        });
    },

    getTodaysGames: function () {
        return getNewGamesFrom(moment().startOf('day').format('YYYY-MM-DD HH:mm:ss'));
    },

    getThisWeekGames: function () {
        return getNewGamesFrom(moment().startOf('isoWeek').format('YYYY-MM-DD HH:mm:ss'));
    },

    getThisMonthGames: function () {
        return getNewGamesFrom(moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'));
    },

    getThisYearGames: function () {
        return getNewGamesFrom(moment().startOf('year').format('YYYY-MM-DD HH:mm:ss'));
    },

    getNewGamesListener: function (handler) {
        var self = this;
        new Promise(function (resolve) {
            firebaseDatabase.ref('/games/').on('value', function (snapshot) {
                resolve(snapshot.val());
            });
        }).then(function (games) {
            self.games = games;
            console.log("initialised games list");
            firebaseDatabase.ref('/games').on('child_added', function (snapshot) {
                if (games[snapshot.key] === undefined) {
                    games[snapshot.key] = snapshot.val();
                    // console.log("NEW GAME !!! " + JSON.stringify(snapshot.val(),"",4));
                    handler(snapshot.val());
                }
                // else {
                //     console.log("OLD GAME " + snapshot.key);
                // }
            })
        });
    }
};
