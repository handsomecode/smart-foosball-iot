'use strict';

var Firebase = require('firebase');

var firebaseConfig = require(__app + 'config')('firebase');

Firebase.initializeApp(firebaseConfig);

var firebaseDatabase = Firebase.database();

var games;

var initialisationState = 0;

module.exports = {
    getPlayers: function () {
        return new Promise(function (resolve) {
            firebaseDatabase.ref('/players/').on('value', function (snapshot) {
                resolve(snapshot.val());
            });
        });
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
