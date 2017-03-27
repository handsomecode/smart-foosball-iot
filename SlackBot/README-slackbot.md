# Slackbot for Smart foosball IoT

Slackbot could help you to find players for matches. It works with help of Slack interactive messages and doesn't flood a lot. So it's realy easy way to find a game. Also, it report about game results and could show statistic on request. It wrote on Node.js as server application.
[Botkit](https://github.com/howdyai/botkit) is used as basis for bot for communicate with users. Match results and statistic are displayed based on information from Firebase datastorage.

##Firebase 

You need to create new Firebase application [here](https://console.firebase.google.com). And set access to read to all by setting ".read" to true:

```json
{
  "rules": {
    ".read": true,
    ".write": "auth != null"
  }
}
```

After that you should go to project setup and press **Add Firebase to your web application**.
You should get information like:

```javascript
<script src="https://www.gstatic.com/firebasejs/3.7.3/firebase.js"></script>
<script>
  // Initialize Firebase
  var config = {
    apiKey: "SOME_FIREBASE_API_KEY",
    authDomain: "SOME_FIREBASE_AUTH_DOMAIN",
    databaseURL: "https:/SOME_FIREBASE_PROJECT_NAME.firebaseio.com",
    storageBucket: "SOME_FIREBASE_PROJECT_NAME.appspot.com",
    messagingSenderId: "SOME_FIREBASE_MESSAGING_SENDER_ID"
  };
  firebase.initializeApp(config);
</script>
```

Please save this config information, it'll be It will be needed later.

##Slack

First of all you need to [create slack application](https://api.slack.com/apps) and:

1. Give to our application following permisson in **Features -> OAuth & Permissions**:
* Confirm user’s identity.
* Access user’s profile and team profile fields.
* View email addresses of people on this team.

2. And specify Redirect URL on same page. Redirect URL must point to https://_yousite.com_/oauth

3. Go to **Bot users** and Add Bot User named for example _foosbot_ to application.

4. Install application to your team on **Intall App page**.

5. After that you should collect all nessesary tokens and secrets for your bot:

* **Bot User OAuth Access Token** on **Install App** page
* **Cliend ID**, **Cliend Secret** and **Redirect URL** on **OAuth & Permissions** page

5. Enable Interactive Messages will be possible **ONLE** after running you application on server (see server section). After successfully runing application on your server, it will create endpoint for slack interactive messages on path https://_yousite.com_/slack/receive. You should specify this URL as Request URL for you application on **Interactive Messages** page.

##Server

For deploy application to your server you need Node.Js 7.7.4 or higher and npm 4.4.1 or higher. Application could add all necessary node_modules with help of `npm install` command.
Slack demands to have SSL certivicate on your server for using Interactive messages, you could get it for example from [Let's Encrypt provider](letsencrypt.org). Please public key and merged certivicate somewhere on your server.

After that you have to create file named **config.json** in SlackBot/app/config/ directory with config for Slackbot and Firebase

```json
{
  "settings": {
    "maxPlayers": 4
  },
  "slack": {
    "token": "$(Bot User OAuth Access Token)",
    "clientId": "$(Cliend ID)",
    "redirectUri": "$(Redirect URL)",
    "clientSecret": "$(Cliend Secret)"
  },
  "firebase": {
    "apiKey": "SOME_FIREBASE_API_KEY-quwPnMxLfag",
    "authDomain": "SOME_FIREBASE_AUTH_DOMAIN.firebaseapp.com",
    "databaseURL": "https://SOME_FIREBASE_PROJECT_NAME.firebaseio.com",
    "storageBucket": "SOME_FIREBASE_PROJECT_NAME.appspot.com"
  },
  "ssl": {
      "privateKey": "path/to/publicKey",
      "certificate": "path/to/certificate"
  }
}
```

Then you will be able to start application and turn on Slack Interacvite Messages, invite bot to general channel and start using.