package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.ChattingFragment
import com.amitozsingh.chatapp.R
import android.content.Intent

import android.content.Context
import android.util.Log
import com.amitozsingh.chatapp.Fragments.FriendslistFragment
import com.amitozsingh.chatapp.utils.LOCAL_HOST
import com.amitozsingh.chatapp.utils.USER_EMAIL
import com.amitozsingh.chatapp.utils.USER_INFO_PREFERENCE
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


class ChattingActivity : BaseActivity() {

    private var mSocket: Socket? = null

    override fun createFragment(): Fragment {

        val friendDetails = intent.getStringArrayListExtra("EXTRA_FRIENDS_DETAILS")

        return ChattingFragment.newInstance(friendDetails!!)
    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        var intent=Intent(this,MessagesActivity::class.java)
//        startActivity(intent)
//
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSocket = IO.socket(LOCAL_HOST)


        mSocket!!.connect()
    }
//
//    fun updatestatus(status:String){
//
//        val sharedPreferences = getSharedPreferences(
//            USER_INFO_PREFERENCE,
//            Context.MODE_PRIVATE
//        )
//        val userEmail = sharedPreferences.getString(USER_EMAIL, "")
//        val sendData = JSONObject()
//
//        sendData.put("email",userEmail )
//        sendData.put("status", status)
//
//
//        mSocket!!.emit("updatestatusnew", sendData)
//        Log.i("ii",userEmail.toString())
//        Log.i("ii",status)
//        Log.i("ii",sendData.toString())
//
//
//
//
//
//
//
//    }
//
//
//
//    override fun onStart() {
//        super.onStart()
//
//        updatestatus("online")
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        updatestatus("offline")
//    }



    override fun onDestroy() {
        super.onDestroy()

    }






}






