package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

import android.widget.Toast
import com.amitozsingh.chatapp.Activities.MessagesActivity


import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED
import com.amitozsingh.chatapp.utils.LOCAL_HOST
import com.amitozsingh.chatapp.utils.USER_EMAIL
import com.amitozsingh.chatapp.utils.encodeEmail
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_messages.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {

    private var mLiveFriendsService: FriendServices? = null

    private var mAllFriendRequestsReference: DatabaseReference? = null
    private var mAllFriendRequestsListener: ValueEventListener? = null


    private var mUserEmailString: String? = null

   // private val mActivity: BaseFragmentActivity? = null
    private var mSocket: Socket? = null
    var bott: BottomNavigationView?=null

    companion object {
        fun newInstance(): ProfileFragment{
            return ProfileFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            mSocket = IO.socket(LOCAL_HOST)


        mSocket!!.connect()
        mLiveFriendsService = FriendServices().getInstance()
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bott = MessagesActivity().findViewById(R.id.bottom_nav_main)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//       bottom_nav_main.selectTabWithId(R.id.tab_profile);
//        setUpBottomBar(mBottomBar,3);

        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED).child(encodeEmail(mUserEmailString));
        mAllFriendRequestsListener = mLiveFriendsService?.getFriendRequestBottom(bott!!,R.id.itemprofile);
        mAllFriendRequestsReference!!.addValueEventListener(mAllFriendRequestsListener!!);

    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mAllFriendRequestsListener!=null){
            mAllFriendRequestsReference!!.removeEventListener(mAllFriendRequestsListener!!)
        }
    }
}
