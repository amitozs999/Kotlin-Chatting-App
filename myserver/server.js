var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

io.on('connection', function(socket) {
    var i=socket.id
    console.log(i+"has connected")
 
   
  });

http.listen(port, function(err){
    if (err){
        console.log(`Error in running the server: ${err}`);
    }

    console.log(`Server is running on port: ${port}`);
});