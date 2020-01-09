var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var firebasecredential=require(__dirname +'/private/mykey.json');

var userrequests=require('./firebase/userserver')
var friendrequests=require('./firebase/friendserver')


userrequests.userAccountrequests(io);
friendrequests.userFriendsrequests(io);


admin.initializeApp({
  credential: admin.credential.cert(firebasecredential),
  databaseURL: "https://node-chat-app-b19a4.firebaseio.com"
});



  

http.listen(port, function(err){
    if (err){
        console.log(`Error in running the server: ${err}`);
    }

    console.log(`Server is running on port: ${port}`);
});