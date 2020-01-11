package com.amitozsingh.chatapp.Services

import android.content.Context
import android.util.Log
import android.widget.EditText
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.amitozsingh.chatapp.FindFriendsAdapter
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment
import com.amitozsingh.chatapp.utils.User
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.RequestAdapter
import com.amitozsingh.chatapp.UserFriendsAdapter


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

    fun getMatchingUsers(users: List<User>): List<User> {



        return users
    }



}
