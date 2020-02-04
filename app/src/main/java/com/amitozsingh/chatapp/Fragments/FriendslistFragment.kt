package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.amitozsingh.chatapp.R

import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.Adapters.UserFriendsAdapter
import com.amitozsingh.chatapp.utils.*
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_friendslist.*
import com.amitozsingh.chatapp.Activities.MessagesActivity
import android.content.Intent

import android.util.Log
import com.amitozsingh.chatapp.Activities.ChattingActivity


/**
 * A simple [Fragment] subclass.
 */
class FriendslistFragment : BaseFragment(), UserFriendsAdapter.UserListener {


    var mActivity:MessagesActivity?=null




    private var mFriendServices: FriendServices? = null
    private var mUserEmailString: String? = null

    private var mAdapter: UserFriendsAdapter? = null

    private var mGetAllCurrenUsersFriendsReference: DatabaseReference? = null
    private var mGetAllCurrentUsersFriendsListener: ValueEventListener? = null
    private var userdatabase:DatabaseReference?=null





    companion object {
        fun newInstance(): FriendslistFragment {
            return FriendslistFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ERROR","KK1")

        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL,null)
        mFriendServices = FriendServices().getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friendslist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter=
            UserFriendsAdapter(activity as MessagesActivity, this)




        mGetAllCurrenUsersFriendsReference = FirebaseDatabase.getInstance().reference
            .child(FIRE_BASE_PATH_USER_FRIENDS)
            .child(encodeEmail(mUserEmailString))

        mGetAllCurrentUsersFriendsListener =
            mFriendServices?.getAllFriends(mUserFriendsRview,mAdapter!!,userfriends_message)

        mGetAllCurrenUsersFriendsReference!!.addValueEventListener(mGetAllCurrentUsersFriendsListener!!)
        mUserFriendsRview.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        mUserFriendsRview.setAdapter(mAdapter)

    }
    override fun OnUserClicked(user: User) {

        Log.d("ERROR","KK2")
        val friendDetails = ArrayList<String>()
        friendDetails.add(user.email!!)
        //friendDetails.add(user.userPicture!!)
        friendDetails.add(user.userName!!)
       // val intent = ChattingActivity.newInstance(mActivity.applicationContext ,friendDetails)
        val intent = Intent(activity, ChattingActivity::class.java)
        intent.putStringArrayListExtra("EXTRA_FRIENDS_DETAILS", friendDetails)
        startActivity(intent)


//        activity!!.overridePendingTransition(
//            android.R.anim.fade_in,
//            android.R.anim.fade_out
//        )

    }
}
