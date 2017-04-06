# Smart Foosbal IoT

Our Handsome team passionately loves foosball, mobile technology and IoT. And it was a question of time when this project will be born. Smart Foosball IoT is was inspired by [Smart Foosbal Handsome project](https://github.com/handsomecode/smart-foosball), but has another aproach and implementation thats why we can't be named "smart foosball v2".

Video about features of old project could be found [here](TODO link to youtube).

Whole project consist of three parts who depend each other:

* [Firebase](/README-firebase.md) is used as backend to collect information about matches and players.
* [Android part](/AndroidApp/README-android.md) is for routing players and goal counting. Android UI is simple and effective way to display information of all players and their statistic. Touch gestures allow to choose players conveniently for the current game.
* [Hardware part](/Arduino/README-arduino.md) is for fixing goals. This part is based on Arduino Uno-like board and has been implemented on C++.
* [Slack bot](/SlackBot/README-slackbot.md) is for looking for players and reporting results. Slack is common way of comminucation in our team and it's really simply to use slack bot and application for such type of purposes.

Firebase and Android is required, hardware part and slack bot is optional.
