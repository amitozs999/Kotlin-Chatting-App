var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var userFriendsrequests=(io)=>{

    io.on('connection', function(socket) {
        var i=socket.id
        console.log(i+"has connected to friend services")
        
        socket.on('disconnect', function() {
       
          console.log("has disconnected")
       
         
        });

        sendOrDeleteFriendRequest(socket,io);
       
      });


};

function sendOrDeleteFriendRequest(socket,io){
    socket.on('friendRequest',(data)=>{
      var friendEmail = data.email;
      var userEmail = data.userEmail;
      var requestCode = data.requestCode;
      var date = {
        data:admin.database.ServerValue.TIMESTAMP
      };
  
  
      var db = admin.database();
      var friendRef = db.ref('friendRequestRecieved').child(encodeEmail(friendEmail))
      .child(encodeEmail(userEmail));
  
      if (requestCode ==0) {
        var db = admin.database();
        var ref = db.ref('users');
        var userRef = ref.child(encodeEmail(data.userEmail));
  
        userRef.once('value',(snapshot)=>{
          friendRef.set({
            email:snapshot.val().email,
            userName:snapshot.val().userName,
            userPicture:' ',
            dateJoined:date,
            hasLoggedIn:snapshot.val().hasLoggedIn
          });
        });
  
      
  
         
        
  
      } else{
        friendRef.remove();
      }
  
    });
  }
  function encodeEmail(email){
    return email.replace('.',',');
  }

  module.exports={
    userFriendsrequests
};