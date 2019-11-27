package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

import com.amitozsingh.chatapp.SearchAdapter
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.User


/**
 * A simple [Fragment] subclass.
 */
class FriendRequestsFragment : Fragment() {




    companion object {
        fun newInstance(): FriendRequestsFragment {
            return FriendRequestsFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_requests, container, false)
    }


}
