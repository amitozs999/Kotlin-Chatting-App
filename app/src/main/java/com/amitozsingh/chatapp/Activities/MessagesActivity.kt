package com.amitozsingh.chatapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.amitozsingh.chatapp.Fragments.FriendsFragment
import com.amitozsingh.chatapp.Fragments.LoginFragment
import com.amitozsingh.chatapp.Fragments.MessagesFragment
import com.amitozsingh.chatapp.Fragments.ProfileFragment
import com.amitozsingh.chatapp.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_messages)
        if (savedInstanceState == null) {
            val fragment = MessagesFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragcont, fragment)
                .commit()
        }
       bottom_nav_main.setOnNavigationItemSelectedListener(this)
    }

}


