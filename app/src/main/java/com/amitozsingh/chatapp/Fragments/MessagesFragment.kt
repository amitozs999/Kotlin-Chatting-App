package com.amitozsingh.chatapp.Fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.utils.LOCAL_HOST
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_messages.*
import com.google.android.material.badge.BadgeDrawable




/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : BaseFragment() {

    //var mActivity: MessagesActivity? = null

    companion object {
        fun newInstance(): MessagesFragment{
            return MessagesFragment()
        }
    }

    //var bott: BottomNavigationView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        bott = mActivity?.findViewById(R.id.bottom_nav_main)
//
//       // bott?.getOrCreateBadge(R.id.itemfriends)?.number = 5
//
//        val badge = bott?.getOrCreateBadge(R.id.itemfriends)
//        badge?.number=5
//
//        badge?.setVisible(true)







    }

}
