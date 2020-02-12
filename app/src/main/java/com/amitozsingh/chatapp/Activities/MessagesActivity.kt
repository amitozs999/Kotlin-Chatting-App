package com.amitozsingh.chatapp.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.FriendsFragment
import com.amitozsingh.chatapp.Fragments.LoginFragment
import com.amitozsingh.chatapp.Fragments.MessagesFragment
import com.amitozsingh.chatapp.Fragments.ProfileFragment
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.fragment_messages.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import com.google.firebase.iid.InstanceIdResult
import com.google.android.gms.tasks.OnSuccessListener

import android.util.Log

var messageToken:String=""
class MessagesActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {



    private var mAuth: FirebaseAuth? = null
    private var mListener: FirebaseAuth.AuthStateListener? = null

    private var mLiveFriendsService: FriendServices? = null

    private var mAllFriendRequestsReference: DatabaseReference? = null
    private var mAllFriendRequestsListener: ValueEventListener? = null

    private var mUsersNewMessagesReference: DatabaseReference? = null
    private var mUsersNewMessagesListener: ValueEventListener? = null


    private var mUserEmailString: String? = null

    // private val mActivity: BaseFragmentActivity? = null
    private var mSocket: Socket? = null

    var bott: BottomNavigationView?=null
   // private var navPosition: BottomNavigationView =findViewById(R.id.bottom_nav)
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            var fragment: Fragment? =null

            when (item.itemId) {
                R.id.itemprofile -> {
                    fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                        .commit()
                    return true
                }
                R.id.itemfriends -> {
                    val bundle = Bundle()
                    bundle.putString("key", "1") // Put anything what you want

                    fragment = FriendsFragment()
                    fragment!!.setArguments(bundle)

                    supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                        .commit()
                    return true
                }
                R.id.itemmessages -> {



                    fragment = MessagesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                        .commit()
                    return true
                }
            }
            return false
        }



//    override fun createFragment(): Fragment {
//            return MessagesFragment.newInstance()
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSocket = IO.socket(LOCAL_HOST)


        mSocket!!.connect()
        mLiveFriendsService = FriendServices().getInstance()


       // val messageToken = FirebaseInstanceId.getInstance().token


        val sharedPreferences = getSharedPreferences(
            USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        //initialy set email null so that it will come back to base activity if user not signed in
        val userEmail = sharedPreferences.getString(USER_EMAIL, "")

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this
        ) { instanceIdResult ->
            messageToken = instanceIdResult.token
            Log.e("newToken", messageToken)

            if (messageToken != null && !userEmail.equals("")) {
                val tokenReference = FirebaseDatabase.getInstance().reference
                    .child(FIRE_BASE_PATH_USER_TOKEN).child(encodeEmail(userEmail))
                tokenReference.child("token").setValue(messageToken)

            }
        }
        Log.e("newToken1", messageToken)







        mAuth = FirebaseAuth.getInstance()


            mListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser

                if (user == null) {
                    val intent = Intent(application, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else if (userEmail == "") {
                    FirebaseAuth.getInstance().signOut()
                    finish()
                }
            }

        setContentView(R.layout.activity_messages)

        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED).child(encodeEmaill(userEmail))
        mAllFriendRequestsListener = mLiveFriendsService?.getFriendRequestBottom(bottom_nav_main,R.id.itemfriends);
        mAllFriendRequestsReference!!.addValueEventListener(mAllFriendRequestsListener!!)





        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_USER_NEW_MESSAGES).child(encodeEmail(userEmail));
        mUsersNewMessagesListener = mLiveFriendsService?.getMessagesBottom(bottom_nav_main,R.id.itemmessages);

        mUsersNewMessagesReference!!.addValueEventListener(mUsersNewMessagesListener!!);

        bottom_nav_main.setOnNavigationItemSelectedListener(this)


//        val badge = bottom_nav_main?.getOrCreateBadge(R.id.itemfriends)
//        badge?.number=5
//
//        badge?.setVisible(true)
        if (savedInstanceState == null) {
            val fragment = MessagesFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                .commit()
        }



    }


    fun updatestatus(status:String){

        val sharedPreferences = getSharedPreferences(
            USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val userEmail = sharedPreferences.getString(USER_EMAIL, "")
        val sendData = JSONObject()

        sendData.put("email",userEmail )
        sendData.put("status", status)


       // mSocket!!.emit("updatestatus", sendData)






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

    fun encodeEmaill(email: String?): String {
        return email!!.replace(".", ",")
    }
    override fun onStart() {
        super.onStart()

            mAuth?.addAuthStateListener(mListener!!)
        //updatestatus("online")

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {

                override fun onClick(arg0: DialogInterface, arg1: Int) {
                    onSuperBackPressed()
                }
            }).create().show()
    }
    fun onSuperBackPressed() {
        super.onBackPressed()
    }
    override fun onDestroy() {
        super.onDestroy()

            mAuth?.removeAuthStateListener(mListener!!)

    }
}


