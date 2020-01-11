package com.amitozsingh.chatapp.Fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amitozsingh.chatapp.Activities.RegisterActivity

import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.LOCAL_HOST

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Services.AccountServices

import org.json.JSONObject

import io.socket.emitter.Emitter

import android.content.Context


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {


     var mActivity: BaseActivity? = null
   var mAccountServices: AccountServices? = null
    lateinit var msocket: Socket
    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msocket= IO.socket(LOCAL_HOST)
        msocket.connect()
        msocket.on("token",tokenListener())

        mAccountServices=AccountServices().getInstance()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


       signin_button.setOnClickListener{
           Log.i("AMITOZ","i m here 1")
            mCompositeSubscription?.add(mAccountServices!!.sendLoginInfo(login_Email,login_Password,msocket,mActivity))
       }
        signup_button.setOnClickListener{
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


     fun tokenListener(): Emitter.Listener {
        return object : Emitter.Listener {
            override fun call(vararg args: Any) {
                val jsonObject = args[0] as JSONObject
                mCompositeSubscription?.add(
                   mAccountServices
                        ?.getAuthToken(jsonObject, mActivity, mSharedPreferences)
                )
            }
        }
//
//         return Emitter.Listener { args ->
//             val data = args[0] as JSONObject
//             mCompositeSubscription!!.add(mAccountServices?.getAuthToken(data,mActivity!!,mSharedPreferences))
//         }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        msocket.disconnect()
    }
}
