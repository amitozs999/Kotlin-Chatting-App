package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.amitozsingh.chatapp.Activities.MessagesActivity

import com.amitozsingh.chatapp.R


import com.amitozsingh.chatapp.myPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_friends.*
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

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
class FriendsFragment : BaseFragment() {

//    private val mFriendsService: FriendServices? = null
//    private val mRequestsReference: DatabaseReference? = null
//    private val mRequestsListener: ValueEventListener? = null
//
//    private val mUserEmailString: String? = null
    var mViewPager: ViewPager? =null


    private var mLiveFriendsService: FriendServices? = null

    private var mAllFriendRequestsReference: DatabaseReference? = null
    private var mAllFriendRequestsListener: ValueEventListener? = null


    private var mUserEmailString: String? = null

    // private val mActivity: BaseFragmentActivity? = null
    private var mSocket: Socket? = null

    var bott: BottomNavigationView?=null

    companion object {
        fun newInstance(): FriendsFragment {

            return FriendsFragment()
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
 val rootView=inflater.inflate(R.layout.fragment_friends, container, false)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED).child(encodeEmail(mUserEmailString));
        mAllFriendRequestsListener = mLiveFriendsService?.getFriendRequestBottom(bott!!,R.id.itemfriends);
        mAllFriendRequestsReference!!.addValueEventListener(mAllFriendRequestsListener!!);

        friendsviewpager.currentItem = 0

        friendsviewpager!!.adapter = myPagerAdapter(childFragmentManager)

        navigationTabs.setupWithViewPager(friendsviewpager)

        friendsviewpager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(navigationTabs))

        navigationTabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                friendsviewpager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mAllFriendRequestsListener!=null){
            mAllFriendRequestsReference!!.removeEventListener(mAllFriendRequestsListener!!)
        }
    }


}
