package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

import com.amitozsingh.chatapp.Services.FriendServices
import io.socket.client.IO
import io.socket.client.Socket
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.Adapters.RequestAdapter
import com.amitozsingh.chatapp.utils.*
import kotlinx.android.synthetic.main.fragment_friend_requests.*


/**
 * A simple [Fragment] subclass.
 */
class FriendRequestsFragment : BaseFragment(), RequestAdapter.OnOptionListener{


    private var mFriendServices: FriendServices? = null
    private var mAdapter: RequestAdapter? = null

    private var mGetAllUsersFriendRequestsReference: DatabaseReference? = null
    private var mGetAllUsersFriendRequestsListener: ValueEventListener? = null


    private var mUserEmailString: String? = null

    private var mSocket: Socket? = null
    override fun OnOptionClicked(user: User, result: String) {

        if (result.equals("0")) {
            val userFriendReference = FirebaseDatabase.getInstance().reference
                .child(FIRE_BASE_PATH_USER_FRIENDS)
                .child(encodeEmail(mUserEmailString))
                .child(encodeEmail(user.email))
            userFriendReference.setValue(user)
            mGetAllUsersFriendRequestsReference!!.child(encodeEmail(user.email))    //recieved wali yaha delete hogi //sent wali node server pe
                .removeValue()
            mCompositeSubscription!!.add(
                mFriendServices!!.approveDeclineFriendRequest(                     //after deleting create new user friends reference in node server
                    mSocket!!, mUserEmailString!!,
                    user.email!!, "0"
                )
            )
        } else {
            mGetAllUsersFriendRequestsReference!!.child(encodeEmail(user.email))
                .removeValue()
            mCompositeSubscription!!.add(
                mFriendServices!!.approveDeclineFriendRequest(
                    mSocket!!, mUserEmailString!!,
                    user.email!!, "1"
                ))
        }
    }





    companion object {
        fun newInstance(): FriendRequestsFragment {
            return FriendRequestsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSocket= IO.socket(LOCAL_HOST)
        mSocket!!.connect()



        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL,null)
        mFriendServices = FriendServices().getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_friend_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter=
            RequestAdapter(activity as MessagesActivity, this)




        mGetAllUsersFriendRequestsReference = FirebaseDatabase.getInstance().reference
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED)
            .child(encodeEmail(mUserEmailString))

        mGetAllUsersFriendRequestsListener =
            mFriendServices?.getAllFriendRequests(mAdapter!!, mrequestrview,request_message)

        mGetAllUsersFriendRequestsReference!!.addValueEventListener(mGetAllUsersFriendRequestsListener!!)
        mrequestrview.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        mrequestrview.setAdapter(mAdapter)



    }

    override fun onDestroyView() {
        super.onDestroyView()


        if (mGetAllUsersFriendRequestsListener != null) {
            mGetAllUsersFriendRequestsReference?.removeEventListener(
                mGetAllUsersFriendRequestsListener!!
            )
        }
    }
}
