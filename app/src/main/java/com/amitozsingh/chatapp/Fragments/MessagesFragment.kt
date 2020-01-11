package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amitozsingh.chatapp.Activities.BaseActivity

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.utils.LOCAL_HOST
import io.socket.client.IO
import io.socket.client.Socket

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : BaseFragment() {

    companion object {
        fun newInstance(): MessagesFragment{
            return MessagesFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}
