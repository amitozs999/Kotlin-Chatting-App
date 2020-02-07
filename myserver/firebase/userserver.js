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
        updateProfilePicture(socket,io);
        registerUser(socket,io);
        logUserIn(socket,io);
        userstatus(socket,io);
        userstatusnew(socket,io)
       
      });


};


function logUserIn(socket,io){
    socket.on('userInfo',(data)=>{
      admin.auth().getUserByEmail(data.email)         //admin.auth().getUserByEmail(email)
                                                      //.then(function(userRecord) {
                                                      // See the UserRecord reference doc for the contents of userRecord.
                                                     //console.log('Successfully fetched user data:', userRecord.toJSON());
                                                     //})
                                                     //.catch(function(error) {
                                                     //console.log('Error fetching user data:', error);
                                                     //});
      .then((userRecord)=>{
  
        var db = admin.database();
        var ref = db.ref('users');
        var userRef = ref.child(encodeEmail(data.email));
  
        userRef.once('value',(snapshot) =>{
          var addtotoken = {
            email:data.email
          };
  
          admin.auth().createCustomToken(userRecord.uid,addtotoken)
          .then((customToken) =>{
  
            Object.keys(io.sockets.sockets).forEach((id)=>{
              if (id == socket.id) {
                var token = {
                  authToken:customToken,
                  email:data.email,
                  photo:snapshot.val().userPicture,
                  displayName:snapshot.val().userName
                }
  
               
                userRef.child('hasLoggedIn').set(true); 
  
                io.to(id).emit('token',{token});
              }
            });
  
          }).catch((error)=>{
            console.log(error.message);
  
            Object.keys(io.sockets.sockets).forEach((id)=>{
              if (id == socket.id) {
                var token = {
                  authToken:error.message,
                  email:'error',
                  photo:'error',
                  displayName:'error'
                }
                io.to(id).emit('token',{token});
              }
            });
          });
        });
      });
    });
  }
  
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
  
        //used to send response to android app that our user us registerd succesfuly
        Object.keys(io.sockets.sockets).forEach((id)=>{
          if (id == socket.id) {
            var message = {
              text:'Success'
            }
            io.to(id).emit('responseMessage',{message});   //obj->responseMessage->message->text=success
          }
        });
  
  
      }).catch((error)=>{
        Object.keys(io.sockets.sockets).forEach((id)=>{
          console.log(error.message);
          if (id == socket.id) {
            var message = {
              text:error.message
            }
            io.to(id).emit('responseMessage',{message});   
          }
        });
      });
    });
  }

  function updateProfilePicture(socket,io){
    socket.on('userUpdatedPicture',(data) =>{
      console.log(data.email);
      console.log(data.picUrl);
      var db = admin.database();
      var ref = db.ref(`users`);
      var userRef = ref.child(encodeEmail(data.email)).child('userPicture');
      userRef.set(data.picUrl);
  
      // var userFriendRef = db.ref('userFriends').child(encodeEmail(data.email));
      // userFriendRef.orderByChild("email").on("child_added",(snapshot)=>{
      //   var friendRef = db.ref('userFriends').child(encodeEmail(snapshot.val().email))
      //   .child(encodeEmail(data.email)).child('userPicture');
      //   friendRef.set(data.picUrl);
      // });

      
     });
  
  }

  function userstatus(socket,io){
    socket.on('updatestatus',(data) =>{
   
      var db = admin.database();
      var ref = db.ref(`users`);
      var userRef = ref.child(encodeEmail(data.email)).child('userStatus');
      userRef.set(data.status);
      
     });
  }
  function userstatusnew(socket,io){
    socket.on('updatestatusnew',(data) =>{
   
      var db = admin.database();
      var ref = db.ref(`users`);
      var userRef = ref.child(encodeEmail(data.email)).child('status');
      userRef.set(data.status);
      
     });
  }
  

  function encodeEmail(email){
    return email.replace('.',',');
  }
  
module.exports={
    userAccountrequests
};