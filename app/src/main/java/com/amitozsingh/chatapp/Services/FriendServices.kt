package com.amitozsingh.chatapp.Services

import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Adapters.*
import com.amitozsingh.chatapp.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase


class FriendServices {

    public var myFriendServices: FriendServices? = null
    var messagesActivity = MessagesActivity()
    var baseActivity: BaseActivity? = null

    private val SERVER_SUCCESS = 6
    private val SERVER_FAILURE = 7
    fun getInstance(): FriendServices {
        if (myFriendServices == null) {
            myFriendServices = FriendServices()
        }
        return myFriendServices as FriendServices
    }

    fun getFriendRequestsSent(
        adapter: FindFriendsAdapter, fragment:SearchFriendsFragment
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

    fun getFriendRequestsRecieved(
        adapter: FindFriendsAdapter
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

                adapter.setmFriendRequestRecievedMap(userHashMap)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    fun approveDeclineFriendRequest(
        socket: Socket,
        userEmail: String,
        friendEmail: String,
        requestCode: String
    ): Subscription {
        val details = arrayListOf<String>()
        details.add(userEmail)
        details.add(friendEmail)
        details.add(requestCode)

        val listObservable = Observable.just(details)

        return listObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {


                override fun call(strings: List<String>): Int? {


                    val sendData = JSONObject()
                    try {
                        sendData.put("userEmail", strings[0])
                        sendData.put("friendEmail", strings[1])
                        sendData.put("requestCode", strings[2])
                        socket.emit("friendRequestResponse", sendData)
                        return SERVER_SUCCESS
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        return SERVER_FAILURE
                    }

                }

            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(integer: Int?) {

                }
            })
    }


    fun sendorremoverequests(
       socket: Socket,
       useremail:String,friendEmail:String,requestcode:String
    ): Subscription? {



        val userDetails = arrayListOf<String>()
        userDetails.add(useremail)
        userDetails.add(friendEmail)
        userDetails.add(requestcode)

        val listObservable = Observable.just(userDetails)

        return listObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {


                override fun call(strings: List<String>): Int? {
                    val userEmail = strings[0]
                    val email = strings[1]
                    val requestCode = strings[2]


                        val sendData = JSONObject()
                        try {
                            sendData.put("userEmail", userEmail)
                            sendData.put("email", email)
                            sendData.put("requestCode", requestCode)
                            socket.emit("friendRequest", sendData)
                            return SERVER_SUCCESS
                        } catch (e: JSONException) {
//                            Log.i(
//                                AccountServices::class.java!!.getSimpleName(),
//                                e.message
//                            )
                            return SERVER_FAILURE
                        }

                    }

            })
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }


                override fun onNext(t: Int) {

//                    if (t == EMPTY_EMAIL) {
//                        userEmailEt.error = "Email  Can't Be Empty"
//                    } else if (t == EMAIL_BAD_FORMAT) {
//                        userEmailEt.error = "check your email format"
//                    }
                }




                override fun onError(e: Throwable) {

                }


            })
    }

    fun getAllFriendRequests(
        adapter: RequestAdapter, recyclerView: RecyclerView,

        textView: TextView
    ): ValueEventListener {

        val users = ArrayList<User>()

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    users.add(user!!)
                }

                if (users.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    adapter.setmUsers(users)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    fun getAllCurrentUsersFriendMap(adapter: FindFriendsAdapter): ValueEventListener {
        val userHashMap = HashMap<String,User>()
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userHashMap.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    userHashMap.put(user?.email!!, user)
                }

                adapter.setmUserFriendMap(userHashMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }
    fun getAllFriends(
        recyclerView: RecyclerView,
        adapter: UserFriendsAdapter,
        textView: TextView
    ): ValueEventListener {
        val users = ArrayList<User>()
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    users.add(user!!)
                }

                if (users.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    adapter.setmUsers(users)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    fun getFriendRequestBottom(bottomBar: BottomNavigationView, tagId: Int): ValueEventListener {
        val users = ArrayList<User>()
               val badge = bottomBar?.getOrCreateBadge(tagId)


        badge?.setVisible(true)
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    users.add(user!!)
                }

                if (!users.isEmpty()) {

                    badge?.number=users.size

                    badge?.setVisible(true)

                } else {
                    badge?.setVisible(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }



    fun getMessagesBottom(bottomBar: BottomNavigationView, tagId: Int): ValueEventListener {
        val messages = ArrayList<Message>()
        val badge = bottomBar?.getOrCreateBadge(tagId)


        badge?.setVisible(true)

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.clear()

                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    messages.add(message!!)
                }

                if (!messages.isEmpty()) {
                    badge?.number=messages.size

                    badge?.setVisible(true)
                } else {

                    badge?.number=messages.size

                    badge?.setVisible(false)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }





    fun sendMessage(
        socket: Socket,
        messageSenderEmail: String,
        messageSenderPicture: String,
        messageText: String,
        friendEmail: String,
        messageSenderName: String,
        type:String,
        finaltime:String
    ): Subscription {
        val details = ArrayList<String>()
        details.add(messageSenderEmail)
        details.add(messageSenderPicture)
        details.add(messageText)
        details.add(friendEmail)
        details.add(messageSenderName)
        details.add(type)
        details.add(finaltime)
        val listObservable = Observable.just(details)

        return listObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {


                override fun call(strings: List<String>): Int? {

                    val sendData = JSONObject()

                    try {
                        sendData.put("senderEmail", strings[0])
                        sendData.put("senderPicture", strings[1])
                        sendData.put("messageText", strings[2])
                        sendData.put("friendEmail", strings[3])
                        sendData.put("senderName", strings[4])
                        sendData.put("type",strings[5])
                        sendData.put("finaltime",strings[6])
                        socket.emit("details", sendData)
                        return SERVER_SUCCESS
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        return SERVER_FAILURE
                    }

                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(integer: Int?) {

                }
            })
    }

    fun getAllMessages(
        recyclerView: RecyclerView, textView: TextView, imageView: ImageView,
        adapter: MessagesAdapter, userEmail: String
    ): ValueEventListener {
        val messages = ArrayList<Message>()

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.clear()
                val newMessagesReference = FirebaseDatabase.getInstance().getReference()
                    .child(FIRE_BASE_PATH_USER_NEW_MESSAGES)
                    .child(encodeEmail(userEmail))

                for (snapshot in dataSnapshot.children) {
                    val message = snapshot.getValue(Message::class.java)
                    newMessagesReference.child(message?.messageId!!).removeValue()
                    messages.add(message!!)
                }

                if (messages.isEmpty()) {
                    imageView.setVisibility(View.VISIBLE)
                    textView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    imageView.setVisibility(View.GONE)
                    textView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setmUsers(messages)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }


    fun getAllChatRooms(
        recyclerView: RecyclerView, textView: TextView,
        adapter: ChatroomAdapter
    ): ValueEventListener {
        val chatRooms = ArrayList<ChatRoom>()
        return object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatRooms.clear()
                for (snapshot in dataSnapshot.children) {
                    val chatRoom = snapshot.getValue(ChatRoom::class.java)
                    chatRooms.add(chatRoom!!)
                }
                if (chatRooms.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    adapter.setmChatRoom(chatRooms)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    fun getMatchingUsers(users: List<User>): List<User> {



        return users
    }



}
