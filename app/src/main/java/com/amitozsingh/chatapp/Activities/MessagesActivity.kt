package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.LoginFragment
import com.amitozsingh.chatapp.Fragments.MessagesFragment
import com.amitozsingh.chatapp.R

class MessagesActivity : BaseActivity() {


        override fun createFragment(): Fragment {
            return MessagesFragment.newInstance()
        }



}
