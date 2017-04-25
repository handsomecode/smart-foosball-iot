'use strict';

var fs = require('fs');
var https = require('https');
var bodyParser = require('body-parser');

var sslConfig = require(__app + 'config')('ssl');

var privateKey  = fs.readFileSync(sslConfig.privateKey);
var certificate = fs.readFileSync(sslConfig.certificate);

var credentials = {key: privateKey, cert: certificate};
var express = require('express');
var app = express();

//parsing
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); //for parsing url encoded
app.use(function (req, res, next) {
    console.log("--------------req");
    console.log(req.body);
    console.log("++++++++++++++req");
    next();
});

// view engine ejs
app.set('view engine', 'ejs');

var httpsServer = https.createServer(credentials, app);

httpsServer.listen(443);

module.exports = {
    getHttpsServer : function () {
        return httpsServer;
    },
    getApp : function () {
        return app;
    }
}