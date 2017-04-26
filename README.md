# Smart Foosbal IoT

![scheme](foosball.jpg)

Our Handsome team passionately loves foosball, mobile technology, and IoT. And it was a question of time when this project will be born. Smart Foosball IoT is inspired by [Smart Foosball Handsome project](https://github.com/handsomecode/smart-foosball), but has another approach and implementation that's why it can't be named "smart foosball v2".

An article with a video about the old project could be found [here](http://handsome.is/smart-foosball-is-keeping-score/).

The whole project consists of four parts which depend on each other:

* [Firebase](/README-firebase.md) is used as a backend to collect information about matches and players.
* [Android part](/AndroidApp/README-android.md) is for routing players and goal counting. Android UI is a simple and effective way to display information of all players and their statistic. Touch gestures allow choosing players conveniently for the current game.
* [Hardware part](/Arduino/README-arduino.md) is for fixing goals. This part is based on Arduino Uno-like board and has been implemented in C++.
* [Slack bot](/SlackBot/README-slackbot.md) is for looking for players and reporting results. Slack is a common way of communication in our team and it's really simple to use slack bot and application for such type of purposes.

Firebase and Android are required, hardware part and slack bot are optional.