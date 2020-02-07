var express = require('express');

var app = express();
var http=require('http').Server(app)
var port = 3000;

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var FCM = require('fcm-push');
var serverKey ='AAAA42Q9OkY:APA91bEWYPsVzg82e031BH8MpL8__CvdSO07rT_ve5tEhs7y1ApKXBzJ3jcnj5obPxuCRNC00MpO0XE9j3B_Fw2oF0nWM-AL-Y7sfen1owouOzlHrjnDfe_fSKrOYF75rag-PT4eXctv';
var fcm = new FCM(serverKey);

var userFriendsrequests=(io)=>{

    io.on('connection', function(socket) {
        var i=socket.id
        console.log(i+"has connected to friend services")
        
        socket.on('disconnect', function() {
       
          console.log("has disconnected")
       
         
        });
        sendMessage(socket,io);
        approveOrDeclineFrienqRequest(socket,io);
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
        var userRef = ref.child(encodeEmail(userEmail));
  
        userRef.once('value',(snapshot)=>{
          friendRef.set({
            email:snapshot.val().email,
            userName:snapshot.val().userName,
            userPicture:' ',
            dateJoined:date,
            hasLoggedIn:snapshot.val().hasLoggedIn
          });
        });
  
      
        var tokenRef = db.ref('userToken');
        var friendToken = tokenRef.child(encodeEmail(friendEmail));
  
        friendToken.once("value",(snapshot)=>{
          var message = {
            to:snapshot.val().token,
            data:{
              title:'my Chat',
              body:`Friend Request from ${userEmail}`
            },
          };
  
          fcm.send(message)
          .then((response)=>{

            console.log('Message sent!');

          }).catch((err)=>{

            console.log(err);

          });
        });
         
        
  
      } else{
        friendRef.remove();
      }
  
    });

  }
  // sendData.put("userEmail", strings[0])
  // sendData.put("friendEmail", strings[1])
  // sendData.put("requestCode", strings[2])
  // socket.emit("friendRequestResponse", sendData)
  
  function approveOrDeclineFrienqRequest(socket,io){
    socket.on('friendRequestResponse',(data)=>{

      var friendEmail = data.friendEmail;
      var userEmail = data.userEmail;
      var requestCode = data.requestCode;
      var date = {
        data:admin.database.ServerValue.TIMESTAMP
      };

          var db = admin.database();
          var friendRequestRef = db.ref('friendRequestsSent').child(encodeEmail(friendEmail))
          .child(encodeEmail(userEmail));
          friendRequestRef.remove();                     //yaha sent wala reference delete hoga 
  
  
          if (requestCode ==0) {
            var db = admin.database();
            var ref = db.ref('users');
            var userRef = ref.child(encodeEmail(userEmail));
                                                                          //if approved create new user friends reference
            var userFriendsRef = db.ref('userFriends');                        
            var friendFriendRef = userFriendsRef.child(encodeEmail(friendEmail))
            .child(encodeEmail(userEmail));
  
            userRef.once('value',(snapshot)=>{
              friendFriendRef.set({
                email:snapshot.val().email,
                userName:snapshot.val().userName,
                userPicture:snapshot.val().userPicture,
                dateJoined:date,
                hasLoggedIn:snapshot.val().hasLoggedIn
              });
            });
          }
    });
  }
  
  function sendMessage(socket,io){
    socket.on('details',(data)=>{
      var db = admin.database();
      var friendMessageRef = db.ref('userMessages').child(encodeEmail(data.friendEmail))
      .child(encodeEmail(data.senderEmail)).push();
  
  
      var newfriendMessagesRef = db.ref('newUserMessages').child(encodeEmail(data.friendEmail))
      .child(friendMessageRef.key);

      var chatRoomRef = db.ref('userChatRooms').child(encodeEmail(data.friendEmail))
      .child(encodeEmail(data.senderEmail));
  
      //Console.log(data.messageType);
      //Console.log(data.messageText);

        var message={
        messageId: friendMessageRef.key,
        messageText: data.messageText,
        messageSenderEmail: data.senderEmail,
        messageSenderPicture: data.senderPicture,
        messageType:data.type,
        messageTime:data.finaltime
      };
  
     
      var chatRoom = {
        friendPicture: data.senderPicture,
        friendName:data.senderName,
        friendEmail: data.senderEmail,
        lastMessage: data.messageText,
        lastMessageSenderEmail: data.senderEmail,
        lastMessageRead:false,
        sentLastMessage:true,
        messageType:data.type
      };

      friendMessageRef.set(message);
      newfriendMessagesRef.set(message);

      chatRoomRef.set(chatRoom);

  
  
    });
  }

  function encodeEmail(email){
    return email.replace('.',',');
  }

  module.exports={
    userFriendsrequests
};