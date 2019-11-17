package com.amitozsingh.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.socket.client.IO
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    lateinit var msocket:Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            msocket=IO.socket(LOCAL_HOST)
           msocket.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
   msocket.disconnect()}
}
