package com.amitozsingh.chatapp.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.USER_EMAIL
import com.amitozsingh.chatapp.utils.USER_NAME
import com.amitozsingh.chatapp.utils.USER_PICTURE
import com.github.nkzawa.socketio.client.Socket
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
import com.amitozsingh.chatapp.utils.Permissions
import android.os.StrictMode








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

    private var mUserEmailString: String? = null

    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_PICTURE = 101


    private var mTempUri: Uri? = null

    private var mPermission: Permissions? = null

    private val mActivity: MessagesActivity? = null
    private val mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        super.onCreate(savedInstanceState)
        mLiveFriendsService = FriendServices().getInstance();
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "");
        mPermission = Permissions(activity as MessagesActivity)
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

        //Picasso.get()
        //   .load(mSharedPreferences?.getString(USER_PICTURE,""))
        // .into(fragment_profile_userPicture);
        fragment_profile_userEmail.setText(mUserEmailString);
        fragment_profile_userName.setText(mSharedPreferences!!.getString(USER_NAME, ""))


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
                    mTempUri = Uri.fromFile(getOutputFile())
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

    private fun getOutputFile(): File? {
        val mesdiaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
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


        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            val selectedImageUri = mTempUri




        }
    }

}

