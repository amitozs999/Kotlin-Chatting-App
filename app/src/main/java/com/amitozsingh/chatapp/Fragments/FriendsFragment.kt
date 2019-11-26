package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.amitozsingh.chatapp.R


import com.amitozsingh.chatapp.myPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_friends.*







/**
 * A simple [Fragment] subclass.
 */
class FriendsFragment : BaseFragment() {


var mViewPager: ViewPager? =null

    companion object {
        fun newInstance(): FriendsFragment {

            return FriendsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

 val rootView=inflater.inflate(R.layout.fragment_friends, container, false)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        friendsviewpager.currentItem = 0

        friendsviewpager!!.adapter = myPagerAdapter(childFragmentManager)

        navigationTabs.setupWithViewPager(friendsviewpager)

        friendsviewpager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(navigationTabs))

        navigationTabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                friendsviewpager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }



}
