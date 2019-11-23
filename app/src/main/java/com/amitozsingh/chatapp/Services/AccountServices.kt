package com.amitozsingh.chatapp.Services

import android.util.Log


import org.json.JSONException

import org.json.JSONObject

import android.widget.EditText

import io.socket.client.Socket
import android.util.Patterns
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers


class AccountServices {

    private var myAccountService: AccountServices? = null

    private val EMPTY_PASSWORD = 1
    private val EMPTY_EMAIL = 2
    private val EMPTY_USERNAME = 3
    private val PASSWORD_SHORT = 4
    private val EMAIL_BAD_FORMAT = 5


    private val SERVER_SUCCESS = 6
    private val SERVER_FAILURE = 7

    private val NO_ERRORS = 8

    fun getInstance(): AccountServices {
        if (myAccountService == null) {
            myAccountService = AccountServices()
        }
        return myAccountService as AccountServices
    }


    fun sendRegistrationInfo(
        userNameEt: EditText, userEmailEt: EditText,
        userPasswordEt: EditText, socket: Socket
    ): Subscription? {



        val userDetails = arrayListOf<String>()
        userDetails.add(userNameEt.text.toString())
        userDetails.add(userEmailEt.text.toString())
        userDetails.add(userPasswordEt.text.toString())

        val userDetailsObservable = Observable.just(userDetails)

        return userDetailsObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {


               override fun call(strings: List<String>): Int? {
                    val userName = strings[0]
                    val userEmail = strings[1]
                    val userPassword = strings[2]

                    if (userName.isEmpty()) {
                        return EMPTY_USERNAME
                    } else if (userEmail.isEmpty()) {
                        return EMPTY_EMAIL
                    } else if (userPassword.isEmpty()) {
                        return EMPTY_PASSWORD
                    } else if (userPassword.length < 6) {
                        return PASSWORD_SHORT
                    } else if (!isEmailValid(userEmail)) {
                        return EMAIL_BAD_FORMAT
                    } else {
                        val sendData = JSONObject()
                        try {
                            sendData.put("email", userEmail)
                            sendData.put("userName", userName)
                            sendData.put("password", userPassword)
                            socket.emit("userData", sendData)
                            return SERVER_SUCCESS
                        } catch (e: JSONException) {
                                Log.i(
                                AccountServices::class.java!!.getSimpleName(),
                                e.message
                            )
                            return SERVER_FAILURE
                        }

                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }


                override fun onNext(t: Int) {

                    if (t == EMPTY_EMAIL) {
                        userEmailEt.error = "Email  Can't Be Empty"
                    } else if (t == EMAIL_BAD_FORMAT) {
                        userEmailEt.error = "check your email format"
                    } else if (t == EMPTY_PASSWORD) {
                        userPasswordEt.error = "Password Can't Be null"
                    } else if (t == PASSWORD_SHORT) {
                        userPasswordEt.error = "Password must be at least 6 characters "
                    } else if (t == EMPTY_USERNAME) {
                        userNameEt.error = "Username can't be empty"
                    }
                }




                override fun onError(e: Throwable) {

                }


            })
    }
    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}