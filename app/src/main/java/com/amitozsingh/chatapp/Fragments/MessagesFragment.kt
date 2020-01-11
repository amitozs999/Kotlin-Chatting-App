package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amitozsingh.chatapp.Activities.BaseActivity

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED
import com.amitozsingh.chatapp.utils.LOCAL_HOST
import com.amitozsingh.chatapp.utils.USER_EMAIL
import com.amitozsingh.chatapp.utils.encodeEmail
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_messages.*

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : BaseFragment() {

    private var mLiveFriendsService: FriendServices? = null

    private var mAllFriendRequestsReference: DatabaseReference? = null
    private var mAllFriendRequestsListener: ValueEventListener? = null


    private var mUserEmailString: String? = null

    // private val mActivity: BaseFragmentActivity? = null
    private var mSocket: Socket? = null

    companion object {
        fun newInstance(): MessagesFragment{
            return MessagesFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSocket = IO.socket(LOCAL_HOST)


        mSocket!!.connect()
        mLiveFriendsService = FriendServices().getInstance()
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED).child(encodeEmail(mUserEmailString));
        mAllFriendRequestsListener = mLiveFriendsService?.getFriendRequestBottom(bottom_nav_main,R.id.itemmessages);
        mAllFriendRequestsReference!!.addValueEventListener(mAllFriendRequestsListener!!);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mAllFriendRequestsListener!=null){
            mAllFriendRequestsReference!!.removeEventListener(mAllFriendRequestsListener!!)
        }
    }
}
