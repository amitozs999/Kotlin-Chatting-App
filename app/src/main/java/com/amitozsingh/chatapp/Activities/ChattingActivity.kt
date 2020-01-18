package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.ChattingFragment
import com.amitozsingh.chatapp.R
import android.content.Intent

import android.content.Context
import com.amitozsingh.chatapp.Fragments.FriendslistFragment


class ChattingActivity : BaseActivity() {

    override fun createFragment(): Fragment {

        val friendDetails = intent.getStringArrayListExtra("EXTRA_FRIENDS_DETAILS")

        return ChattingFragment.newInstance(friendDetails!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent=Intent(this,MessagesActivity::class.java)
        startActivity(intent)


    }

    override fun onDestroy() {
        super.onDestroy()

    }






}






