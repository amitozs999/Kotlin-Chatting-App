package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chatting.*

import com.amitozsingh.chatapp.utils.USER_EMAIL




/**
 * A simple [Fragment] subclass.
 */
class ChattingFragment : BaseFragment() {


    val FRIEND_DETAILS_EXTRA = "FRIEND_DETAILS_EXTRA"
    companion object {
        fun newInstance(friendDetails: ArrayList<String>):ChattingFragment {
            val arguments = Bundle()
            arguments.putStringArrayList("FRIEND_DETAILS_EXTRA", friendDetails)
            val chattingFragment = ChattingFragment()
            chattingFragment.setArguments(arguments)

            return chattingFragment
        }
    }

    private var mFriendEmailString: String? = null
    private var mFriendPictureString: String? = null
    private var mFriendNameString: String? = null
    private var mUserEmailString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val friendDetails = arguments!!.getStringArrayList(FRIEND_DETAILS_EXTRA)
        mFriendEmailString = friendDetails!![0]
       // mFriendPictureString = friendDetails[1]
        mFriendNameString = friendDetails[1]
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Picasso.get()
//            .load(mFriendPictureString)
//            .into(mFriendPicture);

        fragment_messages_friendName.setText(mFriendNameString);

    }

    override fun onDestroy() {
        super.onDestroy()
        //FriendslistFragment.newInstance()
    }



}
