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
import android.widget.Toast
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId

import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import java.io.IOException
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import butterknife.internal.Utils

import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import io.socket.client.IO
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import kotlin.math.log


class AccountServices {

    private var myAccountService: AccountServices? = null
    var messagesActivity=MessagesActivity()
    var baseActivity:BaseActivity?=null

    var mContext: Context? = null

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


    fun getAuthToken(
        data: JSONObject,
        activity: BaseActivity?,
        sharedPreferences: SharedPreferences?
    ): Subscription {
        val jsonObservable = Observable.just(data)

        return jsonObservable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map(object : Func1<JSONObject, List<String>> {
                override fun call(t: JSONObject?): List<String> {


                    val userDetails = arrayListOf<String>()

                    try {
                        val serverData = t!!.getJSONObject("token")
                        val token = serverData.get("authToken") as String
                        val email = serverData.get("email") as String
                        val photo = serverData.get("photo") as String
                        val userName = serverData.get("displayName") as String

                        userDetails.add(token)
                        userDetails.add(email)
                        userDetails.add(photo)
                        userDetails.add(userName)
                        return userDetails
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        return userDetails
                    }
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<String>> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(strings: List<String>) {
                    val token = strings[0]
                    val email = strings[1]
                    val photo = strings[2]
                    val userName = strings[3]

                    Log.i("AMITOZ",email)
                    Log.i("AMITOZ",token)
                    Log.i("AMITOZ",photo)
                    Log.i("AMITOZ",userName)

                    if (email != "error") {
                        FirebaseAuth.getInstance().signInWithCustomToken(token)
                            .addOnCompleteListener { task ->
                                if (!task.isSuccessful) {

                                } else {
                                    sharedPreferences!!.edit().putString(USER_EMAIL, email)
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString(USER_NAME, userName).apply()


                                   try {



                                     Log.i("AMITOZ",activity.toString())
                                       val intent = Intent(activity, messagesActivity::class.java)
                                       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                       if (activity != null) {
                                           activity.startActivity(intent)
                                       }

                                       activity?.finish()
                                   }catch (e:Exception)
                                   {
                                       e.printStackTrace()
                                   }

                                }
                            }
                    }
                }
            })
    }


     fun sendLoginInfo(
        userEmailEt: EditText,
        userPasswordEt: EditText,
        socket: Socket,
        activity: BaseActivity?
    ): Subscription? {
        Log.i("AMITOZ","i m here 2")
        val userDetails = arrayListOf<String>()
        userDetails.add(userEmailEt.text.toString())
        userDetails.add(userPasswordEt.text.toString())
        Log.i("AMITOZ","i m here")
        val userDetailsObservable = Observable.just<List<String>>(userDetails)

        return userDetailsObservable
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {
                override fun call(strings: List<String>): Int? {
                    val userEmail=strings[0]
                    val userPassword = strings[1]

                    Log.i("AMITOZ",userEmail)
                    Log.i("AMITOZ",userPassword)

                    if (userEmail.isEmpty()) {
                       return EMPTY_EMAIL
                    } else if (userPassword.isEmpty()) {
                       return EMPTY_PASSWORD
                    } else if (userPassword.length < 6) {
                       return PASSWORD_SHORT
                    } else if (!isEmailValid(userEmail)) {
                       return EMAIL_BAD_FORMAT
                    } else {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.i("AMITOZ","NI HOGYA")
                                  Log.i("AMITOZ",task.exception!!.message)


                                   // Toast.makeText(activity,task.exception!!.message,Toast.LENGTH_SHORT).show();

                                } else {
                                    Log.i("AMITOZ","HOGYA")
                                    val sendData = JSONObject()
                                    try {
                                        sendData.put("email", userEmail)
                                        socket.emit("userInfo", sendData)
                                        Log.i("AMITOZ","HOGYA DONE")


                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }

                                }
                            }





                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId()
                           // FirebaseInstanceId.getInstance().token

//                            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//
//
//                            }


                            FirebaseInstanceId.getInstance().instanceId
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (!task.isSuccessful) {

                                        return@OnCompleteListener
                                    }

                                    // Get new Instance ID token
                                    val token = task.result?.token
                                    Log.i("newToken0",token)

                                })

                        } catch (e: IOException) {

                            e.printStackTrace()

                        }

                       //FirebaseAuth.getInstance().signOut()
                        Log.i("AMITOZ","LEVEL 6")
                       return NO_ERRORS


                    }
                }
            })
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(integer: Int?) {



                    if (integer == EMPTY_EMAIL) {
                        userEmailEt.error = "Email Address Can't Be Empty"
                    } else if (integer == EMAIL_BAD_FORMAT) {
                        userEmailEt.error = "Please check your email format"
                    } else if (integer == EMPTY_PASSWORD) {
                        userPasswordEt.error = "Password Can't Be Blank"
                    } else if (integer == PASSWORD_SHORT) {
                        userPasswordEt.error = "Password must be at least 6 characters long"
                    }

                }
            })

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

    fun registerResponse(data: JSONObject, activity: BaseActivity): Subscription {
        val jsonObjectObservable = Observable.just(data)
        return jsonObjectObservable
            .subscribeOn(Schedulers.io())
            .map(object :Func1<JSONObject, String>{
                override fun call(t: JSONObject?): String {
                       val message: String

                try {
                    val json = t!!.getJSONObject("message")
                    message = json.get("text") as String
                    return message
                } catch (e: JSONException) {
                    return e.message!!
                }
                }

            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(Response: String) {
                    if (Response == "Success") {
                        activity.finish()
                        Toast.makeText(activity, "Registration Successful!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(activity, Response, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

//    fun changeProfilePhoto(
//
//        uri: Uri,
//        activity: MessagesActivity,
//        currentUserEmail: String,
//        imageView:ImageView,
//        sharedPreferences: SharedPreferences,
//        socket: Socket
//    ): Subscription {
//        val uriObservable = Observable.just(uri)
//
//        return uriObservable
//            .subscribeOn(Schedulers.io())
//            .map(object : Func1<Uri, String> {
//                override fun call(uri: Uri): String? {
//                    try {
//
//                        return uri.toString()
//
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        return null
//                    }
//
//                }
//            }).observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<String> {
//                override fun onCompleted() {
//
//                }
//
//                override fun onError(e: Throwable) {
//
//                }
//
//                override fun onNext(picUrl: String) {
//
////                    //Log.i("aa", storageReference.downloadUrl.toString())
////                    val uploadTask = storageReference.putBytes(bytes)
////                    var link:String?=null
////                    uploadTask.addOnSuccessListener { taskSnapshot ->
//
//                        val sendData = JSONObject()
//                          try {
//                            sendData.put("email", currentUserEmail)
//                            sendData.put("picUrl", picUrl)
//                            socket.emit("userUpdatedPicture", sendData)
//
//
//                             //node-chat-app-b19a4.appspot.com/usersProfilePics/babaji@gmail,com/IMG_20200201_192158.jpg
//
//                              // Log.i("aaaa",taskSnapshot.ge)
//                               //serDatabase.child("imageurl").setValue(uri)
//
//
//
//
//                            } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//
//                }
//            })
//    }


    fun changeProfilePhoto(
        filepath: StorageReference,
        uri: Uri,
        activity: MessagesActivity,
        currentUserEmail: String,
        imageView: ImageView,
        msharedPreferences: SharedPreferences,
        socket: Socket,userref:FirebaseDatabase
    ): Subscription {

        socket!!.connect()
        val uriObservable = Observable.just(uri)
        return uriObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<Uri, ByteArray> {
                override fun call(uri: Uri): ByteArray? {
                    try {


                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)


                        val byteArrayOutPutStream = ByteArrayOutputStream()

                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutPutStream)
                        val data = byteArrayOutPutStream.toByteArray()
                        return data
                    } catch (e: IOException) {

                        return null
                    }

                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ByteArray> {
                override fun onCompleted() {

                }

               override fun onError(e: Throwable) {

                }

                override fun onNext(data: ByteArray) {
                    val uploadTask = filepath.putBytes(data)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        val sendData = JSONObject()
//                        try {
//                            sendData.put("email", currentUserEmail)
//                            sendData.put("picUrl", taskSnapshot.getDownloadUrl().toString())
//                            socket.emit("userUpdatedPicture", sendData)
//                            sharedPreferences.edit().putString(
//                                USER_PICTURE,
//                                taskSnapshot.getDownloadUrl().toString()
//                            ).apply()
//                            Picasso.with(activity)
//                                .load(taskSnapshot.getDownloadUrl().toString())
//                                .into(imageView)
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }

                        filepath.downloadUrl
                            .addOnSuccessListener { uri ->

                                try {
                                    sendData.put("email", currentUserEmail)
                          sendData.put("picUrl", uri.toString())
                            socket.emit("userUpdatedPicture", sendData)

                                    Log.i("zz4",uri.toString())

                                    msharedPreferences.edit().putString(
                                        USER_PICTURE, uri.toString()

                                    ).apply()
                                   // userref.reference.child("users").child(encodeEmail(currentUserEmail)).child("userPicture").setValue(uri.toString())
                                    //updateImageUri(uri.toString(),mUserEmailString!!)
                                    Picasso.get().load(uri.toString()).fit().into(imageView)
                                }catch (e: JSONException) {
                           e.printStackTrace()
                        }



                                }

                    }
                }
            })
    }


    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }




}