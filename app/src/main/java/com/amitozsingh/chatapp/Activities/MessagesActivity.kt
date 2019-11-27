package com.amitozsingh.chatapp.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.FriendsFragment
import com.amitozsingh.chatapp.Fragments.LoginFragment
import com.amitozsingh.chatapp.Fragments.MessagesFragment
import com.amitozsingh.chatapp.Fragments.ProfileFragment
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.USER_EMAIL
import com.amitozsingh.chatapp.utils.USER_INFO_PREFERENCE
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private var mAuth: FirebaseAuth? = null
    private var mListener: FirebaseAuth.AuthStateListener? = null
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
                  fragment = FriendsFragment()
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

        val sharedPreferences = getSharedPreferences(
            USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        //initialy set email null so that it will come back to base activity if user not signed in
        val userEmail = sharedPreferences.getString(USER_EMAIL, "")

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
        if (savedInstanceState == null) {
            val fragment = MessagesFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                .commit()
        }
       bottom_nav_main.setOnNavigationItemSelectedListener(this)
    }
    override fun onStart() {
        super.onStart()

            mAuth?.addAuthStateListener(mListener!!)

    }

    override fun onDestroy() {
        super.onDestroy()

            mAuth?.removeAuthStateListener(mListener!!)

    }
}


