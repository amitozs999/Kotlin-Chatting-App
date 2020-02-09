package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.amitozsingh.chatapp.R


import com.amitozsingh.chatapp.Adapters.myPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_friends.*


import android.util.Log


/**
 * A simple [Fragment] subclass.
 */
class FriendsFragment : BaseFragment() {

//    private val mFriendsService: FriendServices? = null
//    private val mRequestsReference: DatabaseReference? = null
//    private val mRequestsListener: ValueEventListener? = null
//
//    private val mUserEmailString: String? = null
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

        Log.i("mm","jj")
        val bundle = this.arguments

            val code = bundle!!.getString("key")
            Log.i("mm",code)





        friendsviewpager!!.adapter =
            myPagerAdapter(childFragmentManager)

        if(code=="0") {
            friendsviewpager.currentItem = 1
        }else{
            friendsviewpager.currentItem = 0
        }

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
    fun displayReceivedData(message: String) {

    }



}
