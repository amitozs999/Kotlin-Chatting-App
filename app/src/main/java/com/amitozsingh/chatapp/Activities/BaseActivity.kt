package com.amitozsingh.chatapp.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.amitozsingh.chatapp.utils.USER_INFO_PREFERENCE
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import androidx.annotation.NonNull
import com.amitozsingh.chatapp.utils.USER_EMAIL


//used for implementing in another clases
abstract class BaseActivity : AppCompatActivity() {

    abstract fun createFragment(): Fragment


    private var mAuth: FirebaseAuth? = null
    private var mListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.amitozsingh.chatapp.R.layout.activity_base)

        val sharedPreferences = getSharedPreferences(
            USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        //initialy set email null so that it will come back to base activity if user not signed in
        val userEmail = sharedPreferences.getString(USER_EMAIL, "")

        mAuth = FirebaseAuth.getInstance()

        if (!(this is LoginActivity || this is RegisterActivity)) {
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
        }


        val fragmentManager = supportFragmentManager
        var fragment =
            fragmentManager.findFragmentById(com.amitozsingh.chatapp.R.id.basefragcontainer)

        if (fragment == null) {
            fragment = createFragment()
            fragmentManager.beginTransaction()
                .add(com.amitozsingh.chatapp.R.id.basefragcontainer, fragment)
                .commit()
        }
    }


    override fun onStart() {
        super.onStart()
        if (!(this is LoginActivity || this is RegisterActivity)) {
            mAuth?.addAuthStateListener(mListener!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (!(this is LoginActivity || this is RegisterActivity)) {
            mAuth?.removeAuthStateListener(mListener!!)
        }
    }
}



