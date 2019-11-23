var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var userAccountrequests=(io)=>{

    io.on('connection', function(socket) {
        var i=socket.id
        console.log(i+"has connected")
        
        socket.on('disconnect', function() {
       
          console.log("has disconnected")
       
         
        });

        registerUser(socket,io);
       
      });


};
function registerUser(socket,io){
    socket.on('userData',(data)=>{
      admin.auth().createUser({
        email:data.email,
        displayName:data.userName,
        password:data.password
      })
      .then((userRecord)=>{
        console.log('User was registered successfully');
        var db = admin.database();
        var ref = db.ref('users');
        var userRef = ref.child(encodeEmail(data.email));
        var date = {
          data:admin.database.ServerValue.TIMESTAMP
        };
  
        userRef.set({
          email:data.email,
          userName:data.userName,
          userPicture:'',
          dateJoined:date,
          hasLoggedIn:false
        });
  
        Object.keys(io.sockets.sockets).forEach((id)=>{
          if (id == socket.id) {
            var message = {
              text:'Success'
            }
            io.to(id).emit('message',{message});
          }
        });
  
  
      }).catch((error)=>{
        Object.keys(io.sockets.sockets).forEach((id)=>{
          console.log(error.message);
          if (id == socket.id) {
            var message = {
              text:error.message
            }
            io.to(id).emit('message',{message});
          }
        });
      });
    });
  }
  function encodeEmail(email){
    return email.replace('.',',');
  }
  
module.exports={
    userAccountrequests
};