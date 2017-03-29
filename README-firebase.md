# Firebase for Smart Foosball IoT

For this project Firebase database and storage are used. Firebase project could be created in [firebase console](https://console.firebase.google.com).

You have to add node with all players to database manualy.

Data scheme:
```json
"players" : {
    "player1UniqID" : {
      "nick" : "Player1",
      "slackID" : "U04TKQ3Q0"
    },
    "player2UniqID" : {
      "nick" : "Player2",
      "slackID" : "U055441B2"
    },

    ...

} ```

Players avatars should be stored in Firebase storage directory avatars/ and named nickname in lowercase with jpg extention. (nick Player1 - avatar avatars/player1.jpg).

Games will be saved in node named games by android application.

Data scheme:
```json
"games" : {
    "-KRSZAE9wjacS4BUTw0M" : {
      "dateEnd" : "2016-09-16 07:24:14",
      "dateStart" : "2016-09-16 07:02:07",
      "idPlayerA1" : "player1UniqID1",
      "idPlayerA2" : "player1UniqID2",
      "idPlayerB1" : "player1UniqID3",
      "idPlayerB2" : "player1UniqID4",
      "mode" : "2x2mb",
      "scoreA" : 52,
      "scoreB" : 48
    },
    "-KRS_YxauyWzz361O8Fj" : {
      "dateEnd" : "2016-09-19 10:00:28",
      "dateStart" : "2016-09-19 09:40:28",
      "idPlayerA1" : "player1UniqID2",
      "idPlayerA2" : "player1UniqID3",
      "idPlayerB1" : "player1UniqID1",
      "idPlayerB2" : "player1UniqID4",
      "mode" : "2x2mb",
      "scoreA" : 51,
      "scoreB" : 34
    },

    ...

}```