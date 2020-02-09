package com.amitozsingh.chatapp.Adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amitozsingh.chatapp.Fragments.FriendRequestsFragment
import com.amitozsingh.chatapp.Fragments.FriendslistFragment
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment


class myPagerAdapter(fm:FragmentManager) :FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        var fragment: Fragment?=null
        Log.i("mmmm",position.toString())
        when (position) {


            1 -> fragment = FriendslistFragment.newInstance()
            2 -> fragment = FriendRequestsFragment.newInstance()
            0 -> fragment = SearchFriendsFragment.newInstance()


        }
        return fragment!!

    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title: CharSequence

        when (position) {
            1 -> title = "Friends"
            2 -> title = "Requests"
            0 -> title = "Search"
            else -> return null
        }

        return title

    }
}