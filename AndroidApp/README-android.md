# Android application for Smart foosball IoT

Android application helps to choose players for current match, display score, and start countdown before the beginning of the round. Also it has countdown activation by voice using code phrase "Let's go". Information about players are loaded from Firebase database, and then match results are saved to Firebase. So, this Firebase integration is necessary for our needs.

For adding Firebase to your application you have to go to [firebase console](https://console.firebase.google.com) and add Android application on Firebase project setup page. After that you will be able to download google-service.json file. This file should be placed into *AndroidApp/app* directory.

Also you have to active login in firebase by email/password and create new user for your application. It could be done in [firebase console](https://console.firebase.google.com) in Authentication section. Android application will try to find login and password in file AndroidApp/app/src/main/res/ra/data (without extention).
You should create file on this path and add two lines into the file: the first one with the login, and the second one with the password. After that your application will be able to get players information from firebase and push matсhes events there.

If you want to add countdown voice activation, you should get [Yandex API key](https://tech.yandex.com/speechkit/) and add this key on the third line in AndroidApp/app/src/main/res/ra/data file.
