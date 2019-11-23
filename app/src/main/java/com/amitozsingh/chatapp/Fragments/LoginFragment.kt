package com.amitozsingh.chatapp.Fragments


import android.content.Intent
import android.os.Bundle
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


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {


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
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signup_button.setOnClickListener{
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        msocket.disconnect()
    }
}
