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
        console.log(data.email);
        console.log(data.userName);
        console.log(data.password);
    })
}
module.exports={
    userAccountrequests
};