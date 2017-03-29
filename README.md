# Smart Foosbal IoT

Our Handsome team passionately loves foosball, mobile technology and IoT. And it was a question of time when this project will be born. Smart Foosball IoT is was inspired by [Smart Foosbal Handsome project](https://github.com/handsomecode/smart-foosball), but has another aproach and implementation thats why we can't be named "smart foosball v2".

Video about features of old project could be found [here](TODO link to youtube).

Whole project consist of three parts who depend each other:

* [Firebase](/README-firebase.md) was used as backend for collect information about matches and players.
* [Android part](/AndroidApp/README-android.md) for routing players and goal counting. Android UI is simple and effective way to display information of all players, their statistic. Touch gestures allow choose players for current game with сonvenience.
* [Hardware part](/Arduino/README-arduino.md) for fixing goals. This part based on Arduino Uno-like board and was implemented on C++.
* [Slack bot](/SlackBot/README-slackbot.md) for looking for players and reporting results. Slack is common way of comminucation in team and it's realy easy to use slack bots and application for such type of purposes.

Firebase and Android is required, hardware part and slack bot is optional.