'use strict';

var Firebase = require('firebase');

var firebaseConfig = require(__app + 'config')('firebase');

Firebase.initializeApp(firebaseConfig);

module.exports = {
    getPlayers: function () {
        return new Promise(function (resolve) {
            Firebase.database().ref('/players/').on('value', function (snapshot) {
                resolve(snapshot.val());
            });
        });
    }
};
