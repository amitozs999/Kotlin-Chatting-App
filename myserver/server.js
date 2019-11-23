var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var firebasecredential=require(__dirname +'/private/mykey.json');



admin.initializeApp({
  credential: admin.credential.cert(firebasecredential),
  databaseURL: "https://node-chat-app-b19a4.firebaseio.com"
});

io.on('connection', function(socket) {
    var i=socket.id
    console.log(i+"has connected")
    
    socket.on('disconnect', function() {
   
      console.log("has disconnected")
   
     
    });
   
  });

  

http.listen(port, function(err){
    if (err){
        console.log(`Error in running the server: ${err}`);
    }

    console.log(`Server is running on port: ${port}`);
});