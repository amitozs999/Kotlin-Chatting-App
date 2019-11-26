package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R

/**
 * A simple [Fragment] subclass.
 */
class SearchFriendsFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFriendsFragment{
            return SearchFriendsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_friends, container, false)
    }


}
