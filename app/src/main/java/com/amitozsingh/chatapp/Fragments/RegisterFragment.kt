package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.utils.LOCAL_HOST

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_register.*
import rx.subscriptions.CompositeSubscription


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment() {

    lateinit var msocket: Socket

    var mAccountService:AccountServices?=null

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msocket= IO.socket(LOCAL_HOST)
        msocket.connect()

        mAccountService=AccountServices().getInstance()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_signup.setOnClickListener{
            //mAccountService.sendRegistrationInfo(register_userName,register_Email,register_Password,msocket)

            mCompositeSubscription?.add(mAccountService!!.sendRegistrationInfo(register_userName,register_Email,register_Password,msocket))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        msocket.disconnect()
    }


}

private fun CompositeSubscription?.add(sendRegistrationInfo: Unit) {

}
