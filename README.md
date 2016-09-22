# Smart Foosbal IoT

Smart Foosball IoT is project about automatisation of foosball table. With help of that you are able to automatically calculate score while playing, save statistics about games and find games in Slack.

It consist from following parts:
- Arduino based devices
- Android application
- Slackbot based (node.js)
- Firebase (database and storage)

You have to set permissions to write for you android application to follow directories in Firebase database: /players and /games. It is necessary to manualy create players like that:
```
players : {
    "-KRSZAE9wjacS4BUTw0M" : { //uniq playerID
        "nick": "Vasya"
        "slackID": "U04TQG0DD" //user slackID for your team
    }
    "-KRS_YxauyWzz361O8Fj" : { //uniq playerID
        "nick": "Petya"
        "slackID": "U04DQK0MD" //user slackID for your team
    }
}
```
Also you may save avatars for players in Firebase datastorage in /avatars/ It must be named as nick_in_lower_case and be jpg files. 
 
For deploying android application you have to add file **data** (named "data" without extention) to /AndroidApp/app/src/main/res/raw with structure like this (one line - one string):
```
firebase_login
firebase_password
yandex API
```

For deploying SlackBot you have to add config.json file to slackbot/app/config with structure like that:
```
{
  "settings": {
    "maxPlayers": 4
  },
  "slack": {
    "token": "", //bot token you can get after adding bot to you slack with help of Slack API
    "clientId": "", //clientId for slack app
    "redirectUri": "", //server url
    "clientSecret": "" //clientsecret for slack app
  },
  "firebase": {
    "apiKey": "",
    "authDomain": "",
    "databaseURL": "",
    "storageBucket": ""
  }
}
```
Also it is necessary to have ssl-cert for you server, path to certificates can be changed in /SlackBot/app/services/https.js in 7 and 8 lines:
```
var privateKey  = fs.readFileSync('path_to_private_part');
var certificate = fs.readFileSync('path_to_public_part');
```
After slackbot run you have to go to https://servername/login for add application to you Slack.

Bot have two main mention commands:
@foosbot play - for looking for players with help of interactive messages with buttons
@foosbot stats - for showing this week statistic