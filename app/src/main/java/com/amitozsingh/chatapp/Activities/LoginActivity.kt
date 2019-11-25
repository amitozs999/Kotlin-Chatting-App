package com.amitozsingh.chatapp.Activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.LoginFragment

class LoginActivity : BaseActivity() {
    override fun createFragment(): Fragment {
        return LoginFragment.newInstance()
    }
//    lateinit var msocket:Socket
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//            msocket=IO.socket(LOCAL_HOST)
//           msocket.connect()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//   msocket.disconnect()}
}
