package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.amitozsingh.chatapp.R


import rx.subscriptions.CompositeSubscription
import android.content.SharedPreferences

import android.content.Context
import com.amitozsingh.chatapp.utils.USER_INFO_PREFERENCE


/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment : Fragment() {

    protected var mCompositeSubscription: CompositeSubscription? = null

    protected var mSharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeSubscription = CompositeSubscription()

        mSharedPreferences = getActivity()!!.getSharedPreferences(USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE);
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeSubscription?.clear()
    }



}
