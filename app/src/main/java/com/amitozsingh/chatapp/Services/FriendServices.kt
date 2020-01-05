package com.amitozsingh.chatapp.Services

import android.content.Context
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.amitozsingh.chatapp.FindFriendsAdapter
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment
import com.amitozsingh.chatapp.utils.User


class FriendServices {

    public var myFriendServices: FriendServices? = null
    var messagesActivity = MessagesActivity()
    var baseActivity: BaseActivity? = null


    fun getInstance(): FriendServices {
        if (myFriendServices == null) {
            myFriendServices = FriendServices()
        }
        return myFriendServices as FriendServices
    }

    fun getFriendRequestsSent(
        adapter: FindFriendsAdapter,fragment:SearchFriendsFragment
    ): ValueEventListener {
        val userHashMap=HashMap<String,User>()
       // var userHashMap: HashMap<String, User>? = null

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userHashMap.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    userHashMap.put(user?.email!!, user!!)
                }

                adapter.setmFriendRequestSentMap(userHashMap)
                fragment.setmFriendRequestsSentMap(userHashMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }
}
