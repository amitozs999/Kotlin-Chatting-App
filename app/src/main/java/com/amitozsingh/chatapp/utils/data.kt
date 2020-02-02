package com.amitozsingh.chatapp.utils


data class User(

    val email: String? = "",
    val hasLoggedIn: Boolean=false,
    val userName: String? = "",
    val userPicture: String? = ""



)


data class Message(
    var messageId:String="",
var messageText:String="",
    var messageType:String="",
var messageSenderEmail:String="",
var messageSenderPicture:String=""



)


data class ChatRoom (
     var friendPicture: String? = "",
     var friendName: String? = "",
   var friendEmail: String? = "",
     var lastMessage: String? = "",
     var lastMessageSenderEmail: String? = "",
     var lastMessageRead: Boolean = false,
    var sentLastMessage: Boolean = false
)

