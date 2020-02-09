package com.amitozsingh.chatapp.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity

import com.amitozsingh.chatapp.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

import com.amitozsingh.chatapp.Activities.ChattingActivity
import com.amitozsingh.chatapp.Adapters.ChatroomAdapter
import com.amitozsingh.chatapp.Adapters.myPagerAdapter
import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.*
import com.google.firebase.database.FirebaseDatabase
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.fragment_messages.*
import org.json.JSONException
import org.json.JSONObject









/**
 * A simple [Fragment] subclass.
 */


class MessagesFragment : BaseFragment() , ChatroomAdapter.ChatRoomListener{
    override fun OnChatRoomClicked(chatRoom: ChatRoom) {


        val friendDetails = ArrayList<String>()
        friendDetails.add(chatRoom.friendEmail!!)
        //friendDetails.add(chatRoom.friendPicture!!)
        friendDetails.add(chatRoom.friendName!!)
        // val intent = ChattingActivity.newInstance(mActivity.applicationContext ,friendDetails)
        val intent = Intent(activity, ChattingActivity::class.java)
        intent.putStringArrayListExtra("EXTRA_FRIENDS_DETAILS", friendDetails)
        startActivity(intent)

    }

    //var mActivity: MessagesActivity? = null

    private var mLiveFriendsService: FriendServices? = null
    private var mSocket: Socket? = null
//
//    private var mAllFriendRequestsReference: DatabaseReference? = null
//    private var mAllFriendRequestsListener: ValueEventListener? = null

    private var mAdapter: ChatroomAdapter? = null


    private var mUserChatRoomReference: DatabaseReference? = null
    private var mUserChatRoomListener: ValueEventListener? = null

    private var mUserEmailString: String? = null

//    private var mUsersNewMessagesReference: DatabaseReference? = null
//    private var mUsersNewMessagesListener: ValueEventListener? = null

    companion object {
        fun newInstance(): MessagesFragment{
            return MessagesFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLiveFriendsService = FriendServices().getInstance();
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL,"");
//        mSocket = IO.socket(LOCAL_HOST)
//
//
//        mSocket!!.connect()
    }
    //var bott: BottomNavigationView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        bott = mActivity?.findViewById(R.id.bottom_nav_main)
//
//       // bott?.getOrCreateBadge(R.id.itemfriends)?.number = 5
//
//        val badge = bott?.getOrCreateBadge(R.id.itemfriends)
//        badge?.number=5
//
//        badge?.setVisible(true)

        mAdapter= ChatroomAdapter(
            activity as MessagesActivity,
            this,
            mUserEmailString!!
        )


        mUserChatRoomReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_USER_CHAT_ROOMS).child(encodeEmail(mUserEmailString));

        mUserChatRoomListener = mLiveFriendsService?.getAllChatRooms(fragment_inbox_recyclerView,fragment_inbox_message,mAdapter!!);

        mUserChatRoomReference?.addValueEventListener(mUserChatRoomListener!!)
        var fragment: Fragment? =null



        fragment_inbox_recyclerView.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        fragment_inbox_recyclerView.setAdapter(mAdapter)
fab.setOnClickListener {
//    friendsviewpager.currentItem = 1
//
//    friendsviewpager!!.adapter =
//        myPagerAdapter(childFragmentManager)
//
//    navigationTabs.setupWithViewPager(friendsviewpager)

    val bundle = Bundle()
    bundle.putString("key", "0") // Put anything what you want

    fragment = FriendsFragment()
    fragment!!.setArguments(bundle)

    val fragmentManager = activity!!.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.fragcont, fragment!!)
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()


}


    }

    fun updatestatus(status:String){

        val sendData = JSONObject()

            sendData.put("email", mUserEmailString)
            sendData.put("status", status)


            mSocket!!.emit("updatestatus", sendData)






    }


//    override fun onStart() {
//        super.onStart()
//        updatestatus("online")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        updatestatus("offline")
//
//    }

    override fun onStop() {
        super.onStop()
       // getFragmentManager()!!.beginTransaction().remove(this).commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()


        if (mUserChatRoomListener != null) {
            mUserChatRoomReference?.removeEventListener(mUserChatRoomListener!!)
        }


    }

}
