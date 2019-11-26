package com.amitozsingh.chatapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amitozsingh.chatapp.Fragments.FriendRequestsFragment
import com.amitozsingh.chatapp.Fragments.FriendslistFragment
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment


class myPagerAdapter(fm:FragmentManager) :FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment

        when (position) {
            0 -> fragment = FriendslistFragment.newInstance()
            1 -> fragment = FriendRequestsFragment.newInstance()
            2 -> fragment = SearchFriendsFragment.newInstance()

            else -> return null
        }
        return fragment

    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title: CharSequence

        when (position) {
            0 -> title = "Friends"
            1 -> title = "Requests"
            2 -> title = "Find Friends"
            else -> return null
        }

        return title

    }
}