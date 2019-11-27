package com.amitozsingh.chatapp.Services

import android.content.Context
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity


class FriendServices {

    private var myFriendServices: FriendServices? = null
    var messagesActivity = MessagesActivity()
    var baseActivity: BaseActivity? = null


    fun getInstance(): FriendServices {
        if (myFriendServices == null) {
            myFriendServices = FriendServices()
        }
        return myFriendServices as FriendServices
    }
}
