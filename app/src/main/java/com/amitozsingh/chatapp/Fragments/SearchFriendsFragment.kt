package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.FriendServices
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.Adapters.FindFriendsAdapter
import com.amitozsingh.chatapp.utils.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search_friends.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.socket.client.IO
import io.socket.client.Socket
import rx.subjects.PublishSubject

import android.text.Editable
import android.text.TextWatcher


/**
 * A simple [Fragment] subclass.
 */
class SearchFriendsFragment : BaseFragment(), FindFriendsAdapter.UserListener {




    private var mUsersReference: DatabaseReference? = null
    private var mUsersListener: ValueEventListener? = null


    private var mGetAllFriendRequestsSentReference: DatabaseReference? = null
    private var mGetAllFriendRequestsSentListener: ValueEventListener? = null

    private var mGetAllFriendRequestsRecievedReference: DatabaseReference? = null
    private var mGetAllFriendRequestsRecievedListener: ValueEventListener? = null

     private var mGetAllCurrentUsersFriendsReference: DatabaseReference? = null
    private var mGetAllCurrentUsersFriendsListener: ValueEventListener? = null


     var mUserEmailString: String?=null
    private var mAdapter: FindFriendsAdapter? = null


    lateinit var mAllUsers: ArrayList<User>

    private var mFriendServices: FriendServices? = null

    lateinit var msocket:Socket

    var mFriendRequestsSentMap: HashMap<String, User>? = null
    var mFriendRequestsRecievedMap: HashMap<String, User>? = null

    private var mSearchBarString: PublishSubject<String>? = null

    companion object {
        fun newInstance(): SearchFriendsFragment{
            return SearchFriendsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        msocket= IO.socket(LOCAL_HOST)
        msocket.connect()



        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL,null)
        Log.i("AMITOZZ",mUserEmailString.toString())

        mFriendServices = FriendServices().getInstance()
        mFriendRequestsSentMap = HashMap()
        mFriendRequestsRecievedMap= HashMap()
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
        mAdapter =
            FindFriendsAdapter(activity as MessagesActivity, this)

        mUsersListener = getAllUsers(mAdapter!!,mUserEmailString)

        mUsersReference= FirebaseDatabase.getInstance().reference.child(FIREBASE_USERS);
        Log.i("AMITOZ1",mUsersReference.toString())
        Log.i("AMITOZ1",mUsersListener.toString())

        mUsersReference!!.addListenerForSingleValueEvent(mUsersListener!!)





        mGetAllFriendRequestsSentReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_SENT)
            .child(encodeEmail(mUserEmailString))

        mGetAllFriendRequestsSentListener = FriendServices().getFriendRequestsSent(mAdapter!!,this)

        mGetAllFriendRequestsSentReference!!.addValueEventListener(mGetAllFriendRequestsSentListener!!)


        mGetAllFriendRequestsRecievedReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED)
            .child(encodeEmail(mUserEmailString))

        mGetAllFriendRequestsRecievedListener = FriendServices().getFriendRequestsRecieved(mAdapter!!)

        mGetAllFriendRequestsRecievedReference!!.addValueEventListener(mGetAllFriendRequestsRecievedListener!!)

        mGetAllCurrentUsersFriendsReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_USER_FRIENDS).child(encodeEmail(mUserEmailString))

        mGetAllCurrentUsersFriendsListener = FriendServices().getAllCurrentUsersFriendMap(mAdapter!!)
        mGetAllCurrentUsersFriendsReference!!.addValueEventListener(mGetAllCurrentUsersFriendsListener!!)



        mRview.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        mRview.setAdapter(mAdapter)


        searchedtxt.addTextChangedListener(
            object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {


                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //  CommandAdapter(this@basicShortcutActivity,list).filter.filter(p0)
                    mAdapter!!.filter.filter(p0)

                }

            }

        )
//
//        mCompositeSubscription!!.add(createSearchBarSubscription())
//        listenToSearchBar()
    }
//
//    private fun createSearchBarSubscription(): Subscription {
//        mSearchBarString = PublishSubject.create()
//
////        return mSearchBarString
////                .debounce(1000, TimeUnit.MILLISECONDS)
////                .subscribeOn(Schedulers.io())
////                .map(new Func1<String, List<User>>() {
////                    @Override
////                    public List<User> call(String searchString) {
////                        return mLiveFriendsService.getMatchingUsers(mAllUsers,searchString);
////                    }
////                })
//        return mSearchBarString!!
//            .debounce(1000, TimeUnit.MILLISECONDS)
//            .subscribeOn(Schedulers.io())
//            .map(object : Func1<String, List<User>> {
//
//
//            override fun call(strings: String): List<User> {
//                Log.d("ERROR2",strings)
//
//return FriendServices().getMatchingUsers(mAllUsers,strings)
//
//
//            }
//
//        }).observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<List<User>> {
//               override fun onCompleted() {
//
//                }
//
//               override fun onError(e: Throwable) {
//
//                }
//
//               override fun onNext(users: List<User>) {
//
//                    if (users.isEmpty()) {
//                        noresulttv.setVisibility(View.VISIBLE)
//                        mRview.setVisibility(View.GONE)
//                    } else {
//                        noresulttv.setVisibility(View.GONE)
//                        mRview.setVisibility(View.VISIBLE)
//                    }
//
//                    mAdapter!!.setmUsers(users)
//                }
//            })
//    }
//
//    private fun listenToSearchBar() {
//
//        searchedtxt.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Log.d("ERROR",s.toString())
//                mSearchBarString!!.onNext(s.toString())
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//            }
//        })
//    }
    fun getAllUsers(adapter: FindFriendsAdapter, currentUsersEmail: String?): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
var i=0
                for (snapshot in dataSnapshot.children) {
                    i++
                    val user = snapshot.getValue(User::class.java)
Log.i("AMITOZ12",i.toString())
                    if (!user!!.email.equals(currentUsersEmail)&&user.hasLoggedIn
                    ) {
                        mAllUsers.add(user);


                    }

                }
                adapter.setmUsers(mAllUsers)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Can't Load Users", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun setmFriendRequestsSentMap(friendRequestsSentMap: HashMap<String, User>) {
        mFriendRequestsSentMap?.clear()
        mFriendRequestsSentMap?.putAll(friendRequestsSentMap)
    }
    override fun onDestroyView() {
        super.onDestroyView()

        if (mUsersListener !=null){
            mUsersReference?.removeEventListener(mUsersListener!!);
        }
        if (mGetAllFriendRequestsSentListener!=null){
            mGetAllFriendRequestsSentReference?.removeEventListener(
                mGetAllFriendRequestsSentListener!!
            )
        }
        if(mGetAllFriendRequestsRecievedListener!=null){
            mGetAllFriendRequestsRecievedReference?.removeEventListener(
                mGetAllFriendRequestsRecievedListener!!
            )
        }
    }


    override fun OnUserClicked(user: User) {

        if (isIncludedInMap(mFriendRequestsSentMap,user)){
            mGetAllFriendRequestsSentReference?.child(encodeEmail(user.email))
                ?.removeValue();

            mCompositeSubscription?.add(FriendServices().sendorremoverequests(msocket,mUserEmailString!!,
                user.email!!,"1"))




        } else {

            mGetAllFriendRequestsSentReference!!.child(encodeEmail(user.email))
                .setValue(user)

            mCompositeSubscription?.add(FriendServices().sendorremoverequests(msocket,mUserEmailString!!,
                user.email!!,"0"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        msocket.disconnect()
    }
}
