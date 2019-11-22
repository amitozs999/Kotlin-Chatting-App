package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.RegisterFragment
import com.amitozsingh.chatapp.R

class RegisterActivity : BaseActivity() {
    override fun createFragment(): Fragment {
        return RegisterFragment.newInstance()
    }


}
