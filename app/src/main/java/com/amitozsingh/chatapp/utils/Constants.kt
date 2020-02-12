package com.amitozsingh.chatapp.utils
//https://kotlin-chat-server.herokuapp.com/



val LOCAL_HOST = "http://192.168.0.103:3000"
val USER_INFO_PREFERENCE = "USER_INFO_PREFERENCE"

val USER_EMAIL = "USER_EMAIL"
val USER_NAME = "USER_NAME"
val USER_PICTURE = "USER_PICTURE"

val FIREBASE_USERS = "users"

val FIRE_BASE_PATH_FRIEND_REQUEST_SENT = "friendRequestsSent"
val FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED = "friendRequestRecieved"
val FIRE_BASE_PATH_USER_FRIENDS = "userFriends"
val FIRE_BASE_PATH_USER_TOKEN = "userToken"
val FIRE_BASE_PATH_USER_MESSAGES = "userMessages"
val FIRE_BASE_PATH_USER_NEW_MESSAGES = "newUserMessages"
val FIRE_BASE_PATH_USER_CHAT_ROOMS = "userChatRooms"
val FIRE_BASE_PATH_USER_MESSAGECOUNT = "messagecount"

fun encodeEmail(email: String?): String {
    return email!!.replace(".", ",")
}
fun isIncludedInMap(userHashMap: HashMap<String, User>?, user: User): Boolean {
    return userHashMap != null && userHashMap.size !== 0 &&
            userHashMap.containsKey(user.email)
}

