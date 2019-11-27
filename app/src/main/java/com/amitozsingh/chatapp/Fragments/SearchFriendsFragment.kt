package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.SearchAdapter
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.User
import java.util.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.utils.USER_EMAIL
import kotlin.collections.ArrayList

import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.FIREBASE_USERS
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search_friends.*





/**
 * A simple [Fragment] subclass.
 */
class SearchFriendsFragment : BaseFragment(),SearchAdapter.UserListener {
    override fun OnUserClicked(user: User) {

    }

    private var mUsersReference: DatabaseReference? = null
    private var mUsersListener: ValueEventListener? = null





    private var mUserEmailString: String? = null
    private var mAdapter: SearchAdapter? = null


    lateinit var mAllUsers: ArrayList<User>

    private var mFriendsService: FriendServices? = null

    companion object {
        fun newInstance(): SearchFriendsFragment{
            return SearchFriendsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL,"");
        Log.i("AMITOZZ",mUserEmailString!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_search_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



//
//        mUsersListener = getAllUsers(mUserEmailString)
//        mAdapter = SearchAdapter(activity as MessagesActivity, this,mAllUsers)

        mAllUsers = ArrayList()
        mAdapter = SearchAdapter(activity as MessagesActivity, this,mAllUsers)

        mUsersListener = getAllUsers(mAdapter!!,mUserEmailString)

        mUsersReference= FirebaseDatabase.getInstance().reference.child(FIREBASE_USERS);
        Log.i("AMITOZ1",mUsersReference.toString())
        Log.i("AMITOZ1",mUsersListener.toString())

        mUsersReference!!.addListenerForSingleValueEvent(mUsersListener!!)

        mRview.layoutManager= LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        mRview.setAdapter(mAdapter)
    }

    fun getAllUsers(adapter: SearchAdapter, currentUsersEmail: String?): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mAllUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (!user!!.email.equals(currentUsersEmail) && user.hasLoggedIn) {
                        mAllUsers.add(user)
                    }
                }
                adapter.setmUsers(mAllUsers)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Can't Load Users", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (mUsersListener !=null){
            mUsersReference?.removeEventListener(mUsersListener!!);
        }
    }


}
