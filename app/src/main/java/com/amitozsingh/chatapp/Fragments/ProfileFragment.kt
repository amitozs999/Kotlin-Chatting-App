package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R

import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.Services.FriendServices

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import android.content.Intent
import android.net.Uri

import android.provider.MediaStore
import java.io.File.separator

import android.system.Os.mkdir
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import android.app.Activity.RESULT_OK
import android.os.StrictMode

import com.amitozsingh.chatapp.Services.AccountServices
import com.amitozsingh.chatapp.utils.*
import io.socket.client.Socket

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.socket.client.IO
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.IllegalArgumentException as IllegalArgumentException1


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }


    private var mLiveFriendsService: FriendServices? = null

    private var mAllFriendRequestsReference: DatabaseReference? = null
    private var mAllFriendRequestsListener: ValueEventListener? = null

    private var mUsersNewMessagesReference: DatabaseReference? = null
    private var mUsersNewMessagesListener: ValueEventListener? = null

    private var userref: FirebaseDatabase? = null

    private var mUserEmailString: String? = null

    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_PICTURE = 101


    private var mTempUri: Uri? = null

    private var mPermission: Permissions? = null

    private var mActivity: MessagesActivity? = null
    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//
//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
        super.onCreate(savedInstanceState)
        mSocket= IO.socket(LOCAL_HOST)
        mSocket!!.connect()
        mLiveFriendsService = FriendServices().getInstance();
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "");
        mPermission = Permissions(activity as MessagesActivity)

        userref=FirebaseDatabase.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        Picasso.get()
//           .load()
//         .into(fragment_profile_userPicture)

        val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
        userDatabase.child(encodeEmail(mUserEmailString)).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                //nameed.setText(user!!.userName)

                if (user!!.age !=null)
                {

                fragment_profile_userAge.setText(user!!.age)
                }
                if(user.bio!=null) {


                    fragment_profile_userBio.setText(user.bio)
                }

                if(user!!.userPicture != null) {
                    try {


                        Picasso.get().load(user.userPicture).into(fragment_profile_userPicture)
                    } catch (e: IllegalArgumentException) {

                    }
                }
            }

        })





        //Picasso.get().load(mSharedPreferences?.getString(USER_PICTURE,"")).resize(200,250).into(fragment_profile_userPicture)

        fragment_profile_userEmail.setText(mUserEmailString);
       nameed.setText(mSharedPreferences!!.getString(USER_NAME, ""))


        fragment_profile_signOut.setOnClickListener {
            mSharedPreferences!!.edit().putString(USER_PICTURE,"").apply();
            mSharedPreferences!!.edit().putString(USER_NAME,"").apply();
            mSharedPreferences!!.edit().putString(USER_EMAIL,"").apply();
            FirebaseAuth.getInstance().signOut();
           mActivity!!.finish()
        }

        fragment_profile_update.setOnClickListener {

            updateDetails()

        }
        fragment_profile_camera_Picture.setOnClickListener {

            if (mPermission != null) {
                if (!mPermission!!.checkPermissionForCamera()) {
                    mPermission!!.requestPermissionForCamera()
                } else if (!mPermission!!.checkPermissionForWriteExternalStorage()) {
                    mPermission!!.requestPermissionForWriteExternalStorage()
                } else if (!mPermission!!.checkPermissionForReadExternalStorage()) {
                    mPermission!!.requestPermissionForReadExternalStorage()
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    //mTempUri = Uri.fromFile(getOutputFile())
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    mTempUri = context?.let { it1 ->
                        getOutputFile()?.let { it2 ->
                            FileProvider.getUriForFile(
                                it1,
                                context!!.getApplicationContext().packageName + ".provider",
                                it2
                            )
                        }
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri)
                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
                }
            }

        }

        fragment_profile_image_Picture.setOnClickListener {

                        if (!mPermission!!.checkPermissionForWriteExternalStorage()) {
                mPermission!!.requestPermissionForWriteExternalStorage()
            } else if (!mPermission!!.checkPermissionForReadExternalStorage()) {
                mPermission!!.requestPermissionForReadExternalStorage()
            } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Choose Image With"),
                REQUEST_CODE_PICTURE
            )
             }


        }


    }

    fun updateDetails(){


        val bio=fragment_profile_userBio.text.toString()
        val age=fragment_profile_userAge.text.toString()

        val userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(encodeEmail(mUserEmailString))

        userDatabase.child("bio").setValue(bio)
        userDatabase.child("age").setValue(age)
    }

    private fun getOutputFile(): File? {
        val mesdiaStorageDir = File(
            getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), "ChatApp"
        )

        if (!mesdiaStorageDir.exists()) {
            if (!mesdiaStorageDir.mkdir()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(
            mesdiaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg"
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICTURE) {
            val selectedImageUri = data!!.data


            val filePath = FirebaseStorage.getInstance().reference
                .child("usersProfilePics").child(encodeEmail(mUserEmailString))
//            var bitmap: Bitmap? = null
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(mActivity!!.contentResolver, selectedImageUri)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val baos = ByteArrayOutputStream()
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, baos)
//            val data = baos.toByteArray()
//
//            val uploadTask = filePath.putBytes(data)
//            uploadTask.addOnFailureListener { e -> e.printStackTrace() }
//            uploadTask.addOnSuccessListener { taskSnapshot ->
//                filePath.downloadUrl
//                    .addOnSuccessListener { uri ->
//
//                        Log.i("zz3",uri.toString())
//
//                        mSharedPreferences!!.edit().putString(
//                            USER_PICTURE, uri.toString()
//
//                        ).apply()
//                        updateImageUri(uri.toString(),mUserEmailString!!)
//                    }
//                    .addOnFailureListener { e -> e.printStackTrace() }
//            }






            mCompositeSubscription!!.add(
                AccountServices().getInstance()
                    .changeProfilePhoto(
                        filePath, selectedImageUri!!, mActivity!!,
                        mUserEmailString!!, fragment_profile_userPicture, mSharedPreferences!!, mSocket!!,userref!!
                    )
            )

        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            val selectedImageUri = mTempUri



          val filePath = FirebaseStorage.getInstance().reference
                .child("usersProfilePics").child(encodeEmail(mUserEmailString))


//            var bitmap: Bitmap? = null
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(mActivity!!.contentResolver, selectedImageUri)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val baos = ByteArrayOutputStream()
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, baos)
//            val data = baos.toByteArray()
//
//            val uploadTask = filePath.putBytes(data)
//            uploadTask.addOnFailureListener { e -> e.printStackTrace() }
//            uploadTask.addOnSuccessListener { taskSnapshot ->
//                filePath.downloadUrl
//                    .addOnSuccessListener { uri ->
//                        // x=uri
//                        Log.i("zz3",uri.toString())
//
//                        mSharedPreferences!!.edit().putString(
//                            USER_PICTURE, uri.toString()
//
//                        ).apply()
//                        updateImageUri(uri.toString(),mUserEmailString!!)
//                    }
//                    .addOnFailureListener { e -> e.printStackTrace() }
//            }



            mCompositeSubscription!!.add(
                AccountServices().getInstance()
                    .changeProfilePhoto(
                        filePath, selectedImageUri!!, mActivity!!,
                        mUserEmailString!!, fragment_profile_userPicture, mSharedPreferences!!, mSocket!!,userref!!
                    )
            )



        }
    }

    fun updateImageUri(uri: String,email:String) {
        Log.i("uri",uri)
        Log.i("email",email)
        userref!!.reference.child("users").child(encodeEmail(email)).child("userPicture").setValue(uri)

        //userdatabase = getInstance().getReference("users").child(encodeEmail(email)).child("userPicture").setValue(uri)

//mAllFriendRequestsReference!!.child("users").child(encodeEmail(email)).child("userPicture").setValue(uri)

        populateImage(uri)
    }
    fun populateImage(uri: String){
        Picasso.get().load(uri).resize(200,250).into(fragment_profile_userPicture)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MessagesActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket!!.disconnect()
    }
}

