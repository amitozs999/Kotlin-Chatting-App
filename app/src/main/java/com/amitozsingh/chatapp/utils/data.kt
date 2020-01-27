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
var messageSenderEmail:String="",
var messageSenderPicture:String=""



)