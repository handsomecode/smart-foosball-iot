var Botkit = require("botkit");
var Firebase = require("firebase");
var Request = require("request");

if (!process.env.tokenSlack) {
    console.log('Error: Specify Slack token in environment');
    process.exit(1);
}

if (!process.env.tokenFirebase) {
    console.log('Error: Specify Firebase token in environment');
    process.exit(1);
}

var controller = Botkit.slackbot({
    debug: true
});

controller.spawn({
    token: process.env.tokenSlack
}).startRTM(function(err) {
    if (err) {
        throw new Error(err);
    }
});

var firebaseConfig = {
    apiKey: process.env.tokenFirebase,
    authDomain: "handsomefoosball.firebaseapp.com",
    databaseURL: "https://handsomefoosball.firebaseio.com",
    storageBucket: "handsomefoosball.appspot.com"
};

Firebase.initializeApp(firebaseConfig);
console.log("firebase initialized");

var playersList;

Firebase.database().ref('/players/').on("value", function(snapshot) {
    playersList = snapshot.val();
    console.log(playersList);
});

var listOfPlayers = "";
var maxPlayers = 2;
var currentGame = [];
var timerCleaning;

function ClearCurrentGame(bot, message) {
    currentGame.length = 0;
    bot.reply(message, "Sorry guys, no one more want to play :sad: " + listOfPlayers);
    listOfPlayers = "";
}

controller.hears(['foosball', 'play', 'game'],['direct_message','direct_mention','mention'], function(bot,message) {

    console.log("User slack ID " + message.user);
    Request.post('https://slack.com/api/users.info', {form:{'token':process.env.tokenSlack, 'user':message.user}},
        function (error, response, body) {
            if (!error && response.statusCode == 200) {
                console.log('response ' + body); // Show the HTML for the Modulus homepage.
                var jsonResposne = JSON.parse(body);
                console.log('Real name' + jsonResposne.user.profile.real_name_normalized);
        }});

    var isInList = false;
    var id = "";
    for(var p in playersList) {
        console.log(p, playersList[p]);
        if (message.user == playersList[p].slackID) {
            isInList = true;
            //nick = playersList[p].nick;
            id = p;
        }
    }
    if (isInList) {
        if (currentGame.length == 0) {
            timerCleaning = setTimeout(ClearCurrentGame, 1*60*1000, bot, message); //5*60*1000 5 минут
        }
        var isPlayerInCurrentGame = false;
        for (var i = 0; i < currentGame.length; i++) {
            if (currentGame[i].user == message.user) isPlayerInCurrentGame = true;
        }
        if (!isPlayerInCurrentGame) {
            var player = {};
            player.user = message.user;
            player.id = id;
            currentGame.push(player);
            if (!currentGame.length == 0) {
                listOfPlayers += ", ";
            }
            listOfPlayers += "<@";
            listOfPlayers += message.user;
            listOfPlayers += ">";


            if (currentGame.length == maxPlayers) {
                bot.reply(message, "Hey guys " + listOfPlayers + "You are next");
                currentGame.length = 0;
                clearTimeout(timerCleaning);
            } else {
                bot.reply(message, "<!here> Who want to play foosball with " + listOfPlayers);
            }
        } else {
            bot.reply(message, "<@" + message.user + "> You are already in list");
        }
    }
    else {
        bot.reply(message, "<@" + message.user + "> You are not registered in Handsome foosball team");
    }
});