# Firebase for Smart Foosball IoT

Firebase database and storage are used in this project. Firebase project could be created in [firebase console](https://console.firebase.google.com).

You have to add a node with all players to the database manually.

Data scheme:
<pre><code>json
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

}</code></pre>

Players avatars should be stored in Firebase storage directory *avatars/* and named in lowercase with a jpg extension. (e.g. nick Player1 - avatar avatars/player1.jpg).

Games will be saved in the node named *games* by the android application.

Data scheme:
<pre><code>json
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

}</code></pre>

Also set access to read to all by setting ".read" to true:

```json
{
  "rules": {
    ".read": true,
    ".write": "auth != null"
  }
}
```

