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
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_register.*
import rx.subscriptions.CompositeSubscription
import org.json.JSONObject

import com.amitozsingh.chatapp.Activities.BaseActivity

import android.content.Context


/**
 * A simple [Fragment] subclass.
 */

 //var status:String?=""
class RegisterFragment : BaseFragment() {

    lateinit var msocket: Socket

    var mAccountService:AccountServices?=null
    var mbaseActivity:BaseActivity?=null

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msocket= IO.socket(LOCAL_HOST)
        msocket.connect()

        msocket.on("responseMessage",Response())

        mAccountService=AccountServices().getInstance()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_signup.setOnClickListener{
            //mAccountService.sendRegistrationInfo(register_userName,register_Email,register_Password,msocket)

            mCompositeSubscription?.add(mAccountService!!.sendRegistrationInfo(register_userName,register_Email,register_Password,msocket))
        }


    }



     fun Response(): Emitter.Listener {
        return Emitter.Listener { args ->
            val data = args[0] as JSONObject
            mCompositeSubscription!!.add(mAccountService!!.registerResponse(data,mbaseActivity!!))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

//    override fun onStart() {
//        super.onStart()
//        status="online"
//
//
//    }

//    override fun onStop() {
//        super.onStop()
//        status="offine"
//    }

    override fun onDestroy() {
        super.onDestroy()
        msocket.disconnect()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mbaseActivity = context as BaseActivity
    }

    override fun onDetach() {
        super.onDetach()
        mbaseActivity = null
    }

}

private fun CompositeSubscription?.add(sendRegistrationInfo: Unit) {

}
