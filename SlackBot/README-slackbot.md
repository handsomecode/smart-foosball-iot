# Slackbot for Smart foosball IoT

Slackbot is aimed to help you to find players for matches. It works with help of Slack interactive messages and doesn't flood a lot. So it's really easy way to start a game. Also, it reports games results and could show statistics on request. It is written on Node.js as a server application.
[Botkit](https://github.com/howdyai/botkit) is used as basis for bot to communicate with users. Match's results and statistics are based on the information from Firebase datastorage.

## Firebase 

You should go to the project setup in [firebase console](https://console.firebase.google.com) and click **Add Firebase to your web application**.
You should get information like this:

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

Please, save this config information, it'll be needed later.

## Slack

First of all, you need to [create slack application](https://api.slack.com/apps) and:

1. Give our application the following permission in **Features -> OAuth & Permissions**:
* Confirm user’s identity.
* Access user’s profile and team profile fields.
* View email addresses of people on this team.

2. And specify Redirect URL on the same page. Redirect URL should point to https://_yousite.com_/oauth

3. Go to **Bot users** and add Bot User, named, for example, _foosbot_ to application.

4. Install application to your team on **Intall App page**.

5. Then you should collect all necessary tokens and secrets for your bot:

* **Bot User OAuth Access Token** on **Install App** page
* **Client ID**, **Client Secret** and **Redirect URL** on **OAuth & Permissions** page

5. Enabling Interactive Messages will be possible **ONLY** after running your application on server (see server section). Once application on your server runs successfully, it'll create endpoint for slack interactive messages on path https://_yousite.com_/slack/receive. You should specify this URL as Request URL for your application on **Interactive Messages** page.

## Server

To deploy application to your server you need Node.Js 7.7.4 or higher and npm 4.4.1 or higher. Application could add all necessary node_modules with help of `npm install` command.
Slack demands to have SSL certificate on your server for using Interactive messages. You can get it, for example, from [Let's Encrypt provider](letsencrypt.org). Please, save public key and merged certificate somewhere on your server.

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

Then you will be able to start application, turn on Slack Interacvite Messages, invite bot to general channel and start to use it.

Bot has two main commands, mentioned above:

**@foosbot play** - for looking for players with help of interactive messages with buttons
**@foosbot stats** - for displaying week's statistics
