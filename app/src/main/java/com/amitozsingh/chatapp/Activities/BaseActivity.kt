package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment




//used for implementing in another clases
abstract class BaseActivity : AppCompatActivity() {

    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.amitozsingh.chatapp.R.layout.activity_base)

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
}



