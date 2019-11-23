package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.amitozsingh.chatapp.R


import rx.subscriptions.CompositeSubscription


/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment : Fragment() {

    protected var mCompositeSubscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeSubscription = CompositeSubscription()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeSubscription?.clear()
    }



}
