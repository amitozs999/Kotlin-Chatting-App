package com.amitozsingh.chatapp.Fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amitozsingh.chatapp.R
import kotlinx.android.synthetic.main.fragment_chatting.*

import com.amitozsingh.chatapp.utils.USER_EMAIL




/**
 * A simple [Fragment] subclass.
 */


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.ChattingActivity
import com.amitozsingh.chatapp.Adapters.MessagesAdapter

import com.amitozsingh.chatapp.Services.FriendServices
import com.amitozsingh.chatapp.utils.*
import io.socket.client.IO
import io.socket.client.Socket
import rx.subjects.PublishSubject
import com.amitozsingh.chatapp.utils.ChatRoom
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.Subscription
import rx.Observer
import java.util.concurrent.TimeUnit
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.FileProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_messages.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ChattingFragment : BaseFragment() {


    val FRIEND_DETAILS_EXTRA = "FRIEND_DETAILS_EXTRA"
    companion object {
        fun newInstance(friendDetails: ArrayList<String>):ChattingFragment {
            val arguments = Bundle()
            arguments.putStringArrayList("FRIEND_DETAILS_EXTRA", friendDetails)
            val chattingFragment = ChattingFragment()
            chattingFragment.setArguments(arguments)

            return chattingFragment
        }
    }

    private val REQUEST_CODE_CAMERA = 100
    private val REQUEST_CODE_PICTURE = 101

    private var mPermission: PermissionsChatting? = null

    private var mFriendEmailString: String? = null
    private var mFriendPictureString: String? = null
    private var mFriendNameString: String? = null
    private var mUserEmailString: String? = null

    private var mGetAllMessagesReference: DatabaseReference? = null
    private var mGetAllMessagesListener: ValueEventListener? = null


    private var mUserChatRoomReference: DatabaseReference? = null
    private var mUserChatRoomListener: ValueEventListener? = null


    private var mMessageSubject: PublishSubject<String>? = null


    private var stringMessage: String? = null
    private var encryptionKey = byteArrayOf(56, 15, 78, 6, 85, 67, 7, -23, 78, 4, 6, 70, 65, -15, 19, 53)
    private var cipher: Cipher? = null
    private  var decipher: Cipher? = null
    private var secretKeySpec: SecretKeySpec? = null



    var mActivity: ChattingActivity?=null
    private var mAdapter: MessagesAdapter? = null

    private var mSocket: Socket? = null
    private var mLiveFriendsService: FriendServices? = null

    private var mTempUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {


//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
        super.onCreate(savedInstanceState)
        mSocket= IO.socket(LOCAL_HOST)
        mSocket!!.connect()
        mLiveFriendsService = FriendServices().getInstance();

        val friendDetails = arguments!!.getStringArrayList(FRIEND_DETAILS_EXTRA)
        mFriendEmailString = friendDetails!![0]
        // mFriendPictureString = friendDetails[1]
        mFriendNameString = friendDetails[1]
        mUserEmailString = mSharedPreferences!!.getString(USER_EMAIL, "")

        mPermission = PermissionsChatting(activity as ChattingActivity)

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace();
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace();
        }

        secretKeySpec = SecretKeySpec(encryptionKey, "AES")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Picasso.get()
//            .load(mFriendPictureString)
//            .into(mFriendPicture);





        sendArrow.setOnClickListener {

//            val current = LocalDateTime.now()
//            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
//            val formatted = current.format(formatter)



            setmSendMessage("textMessage","")
        }


        sendPic.setOnClickListener {

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

        sendCamera.setOnClickListener {
            if (mPermission != null) {
                if (!mPermission!!.checkPermissionForCamera()) {
                    mPermission!!.requestPermissionForCamera()
                } else if (!mPermission!!.checkPermissionForWriteExternalStorage()) {
                    mPermission!!.requestPermissionForWriteExternalStorage()
                } else if (!mPermission!!.checkPermissionForReadExternalStorage()) {
                    mPermission!!.requestPermissionForReadExternalStorage()
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                   // mTempUri = Uri.fromFile(getOutputFile())
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

        backbtn.setOnClickListener {

            Log.i("ll","ll")
            activity!!.finish();

        }


        Log.i("vv","kk")

        val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
        userDatabase.child(encodeEmail(mFriendEmailString)).addValueEventListener(object :
            ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                Log.i("vv",user!!.status.toString())
                if(statustv!=null) {
                    statustv.text = user.status.toString()

                    Log.i("vv", mUserEmailString.toString())
                }
            }

        })





        fragment_messages_friendName.setText(mFriendNameString)

        friendname.setText(mFriendNameString)

        mAdapter= MessagesAdapter(
            activity as ChattingActivity,
            mUserEmailString!!
        )



        mUserChatRoomReference = FirebaseDatabase.getInstance().getReference()
            .child(FIRE_BASE_PATH_USER_CHAT_ROOMS).child(encodeEmail(mUserEmailString))
            .child(encodeEmail(mFriendEmailString));


        mUserChatRoomListener = getCurrentChatRoomListener();

        mUserChatRoomReference?.addValueEventListener(mUserChatRoomListener!!);

        mGetAllMessagesReference = FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_PATH_USER_MESSAGES)
            .child(encodeEmail(mUserEmailString)).child(encodeEmail(mFriendEmailString));


        mGetAllMessagesListener = mLiveFriendsService?.getAllMessages(fragment_messages_recyclerView,fragment_messages_friendName,fragment_messages_friendPicture,mAdapter!!,mUserEmailString!!);

        mGetAllMessagesReference!!.addValueEventListener(mGetAllMessagesListener!!)

       var linearlayout=LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        linearlayout.stackFromEnd=true
        fragment_messages_recyclerView.layoutManager= linearlayout
//
//        fragment_messages_recyclerView.setHasFixedSize(true)
//        fragment_messages_recyclerView.setItemViewCacheSize(200)
//        fragment_messages_recyclerView.setDrawingCacheEnabled(true)
        fragment_messages_recyclerView.recycledViewPool.setMaxRecycledViews(0, 0);


        fragment_messages_recyclerView.setAdapter(mAdapter)








        mCompositeSubscription!!.add(createChatRoomSubscription())
        fragment_messages_recyclerView.scrollToPosition(mAdapter!!.itemCount-1);


        messageBoxListener();

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

        if(data!=null) {

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICTURE) {
                val selectedImageUri = data!!.data


                val filePath = FirebaseStorage.getInstance().reference
                    .child("MessagePics").child(encodeEmail(mUserEmailString))
                    .child(selectedImageUri?.lastPathSegment!!);
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        mActivity!!.contentResolver,
                        selectedImageUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                val data = baos.toByteArray()

                val uploadTask = filePath.putBytes(data)
                uploadTask.addOnFailureListener { e -> e.printStackTrace() }
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    filePath.downloadUrl
                        .addOnSuccessListener { uri ->

                            Log.i("zz5", uri.toString())

                            mSharedPreferences!!.edit().putString(
                                USER_PICTURE, uri.toString()

                            ).apply()
                            //PicUrl = uri.toString()
                            setmSendMessage("picMessage", uri.toString())
                            // updateImageUri(uri.toString(),mUserEmailString!!)
                        }
                        .addOnFailureListener { e -> e.printStackTrace() }
                }
//                Log.i("zz6", PicUrl)

//            mCompositeSubscription!!.add(
//                AccountServices().getInstance()
//                    .changeProfilePhoto(
//                        filePath, selectedImageUri!!, mActivity!!,
//                        mUserEmailString!!, fragment_profile_userPicture, mSharedPreferences!!, mSocket!!,userref!!
//                    )
//            )

            }

        }

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
                val selectedImageUri = mTempUri


                val filePath = FirebaseStorage.getInstance().reference
                    .child("MessagePics").child(encodeEmail(mUserEmailString)).child(selectedImageUri?.lastPathSegment!!);
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        mActivity!!.contentResolver,
                        selectedImageUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                val data = baos.toByteArray()

                val uploadTask = filePath.putBytes(data)
                uploadTask.addOnFailureListener { e -> e.printStackTrace() }
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    filePath.downloadUrl
                        .addOnSuccessListener { uri ->

                            Log.i("zz5", uri.toString())

                            mSharedPreferences!!.edit().putString(
                                USER_PICTURE, uri.toString()

                            ).apply()
                            //PicUrl = uri.toString()
                            setmSendMessage("picMessage",uri.toString())
                            // updateImageUri(uri.toString(),mUserEmailString!!)
                        }
                        .addOnFailureListener { e -> e.printStackTrace() }
                }


            }

    }







    fun setmSendMessage(type:String,uri:String) {
//        val filePath = FirebaseStorage.getInstance().reference
//            .child("usersProfilePics").child(encodeEmail(mUserEmailString))


        var calender=Calendar.getInstance()
        var currentdate=SimpleDateFormat("MMM dd,yyyy")
        var saveddate=currentdate.format(calender.time)


        var currenttime=SimpleDateFormat("hh:mm a")

        var savedtime=currenttime.format(calender.time)
        var b=savedtime.toUpperCase()


        var finaltime="$b-$saveddate"
        Log.i("zz", "$b-$saveddate")
        Log.i("zz", finaltime)



        val filepath = FirebaseDatabase.getInstance().getReference()
            .child(FIREBASE_USERS)
            .child(encodeEmail(mUserEmailString)).child("userPicture")


        if (fragment_messages_messageBox.getText().toString().equals("")&&type=="textMessage") {
            Toast.makeText(activity, "Message Can't Be Blank", Toast.LENGTH_SHORT).show()


        } else {

            var chatRoom=ChatRoom()
            if(type=="textMessage") {
                chatRoom=ChatRoom(
                    mFriendPictureString,
                    mFriendNameString,
                    mFriendEmailString,
                    AESEncryptionMethod(fragment_messages_messageBox.text.toString()),
                    mUserEmailString,
                    true,
                    true,"textMessage"
                )
            }else{
                chatRoom=ChatRoom(
                    mFriendPictureString,
                    mFriendNameString,
                    mFriendEmailString,
                    AESEncryptionMethod(uri),
                    mUserEmailString,
                    true,
                    true,"PicMessage")

            }

            mUserChatRoomReference!!.setValue(chatRoom)

            val newMessageRefernce = mGetAllMessagesReference?.push()
          Log.i("zz7", uri)
            var message=Message()
            if(type=="textMessage"){
                message = Message(
                    newMessageRefernce?.key!!,
                    AESEncryptionMethod(fragment_messages_messageBox.getText().toString()),type,
                    mUserEmailString!!,
                    mSharedPreferences?.getString(USER_PICTURE, "")!!,finaltime
                )

                Log.i("xx",fragment_messages_messageBox.text.toString())

            }
            if(type=="picMessage"){

                message = Message(
                    newMessageRefernce?.key!!,
                   AESEncryptionMethod(uri),type,
                    mUserEmailString!!,
                    mSharedPreferences?.getString(USER_PICTURE, "")!!,finaltime
                )

                Log.i("xx2",uri)
            }


            newMessageRefernce!!.setValue(message)

            if(type=="textMessage"){

                mCompositeSubscription!!.add(
                    mLiveFriendsService?.sendMessage(
                        mSocket!!,
                        mUserEmailString!!,
                        mSharedPreferences?.getString(USER_PICTURE, "")!!,
                        AESEncryptionMethod(fragment_messages_messageBox.getText().toString()),
                        mFriendEmailString!!,
                        mSharedPreferences?.getString(USER_NAME, "")!!,
                        "textMessage",finaltime
                    )
                )
            }

            if(type=="picMessage"){
                mCompositeSubscription!!.add(
                    mLiveFriendsService?.sendMessage(
                        mSocket!!,
                        mUserEmailString!!,
                        mSharedPreferences?.getString(USER_PICTURE, "")!!,
                        AESEncryptionMethod(uri),
                        mFriendEmailString!!,
                        mSharedPreferences?.getString(USER_NAME, "")!!,
                        "PicMessage",finaltime
                    )
                )
            }



            fragment_messages_recyclerView.scrollToPosition(mAdapter!!.itemCount-1);

            fragment_messages_messageBox.setText("");



        }
    }
    private fun createChatRoomSubscription(): Subscription {
        mMessageSubject = PublishSubject.create()
        return mMessageSubject!!
            .debounce(200, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNext(message: String) {
                    if (!message.isEmpty()) {
                        val chatRoom = ChatRoom(
                            mFriendPictureString, mFriendNameString,
                            mFriendEmailString, message, mUserEmailString, true, false
                        )

                        mUserChatRoomReference!!.setValue(chatRoom)

                    }
                }
            })
    }


    private fun messageBoxListener() {
        fragment_messages_messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mMessageSubject!!.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun getCurrentChatRoomListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatRoom = dataSnapshot.getValue(ChatRoom::class.java)
                if (chatRoom != null) {
                    mUserChatRoomReference
                        ?.child("lastMessageRead")
                        ?.setValue(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    private fun AESEncryptionMethod(string: String): String {

        val stringByte = string.toByteArray()
        var encryptedByte = ByteArray(stringByte.size)

        try {
            cipher?.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            encryptedByte = cipher!!.doFinal(stringByte)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }

        var returnString: String? = null
        val charset: Charset = Charsets.ISO_8859_1

        try {
            returnString = String(encryptedByte, charset)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return returnString!!
    }




    fun updatestatus(status:String){
//
//        val sharedPreferences = getSharedPreferences(
//            USER_INFO_PREFERENCE,
//            Context.MODE_PRIVATE
//        )
//        val userEmail = sharedPreferences.getString(USER_EMAIL, "")
        val sendData = JSONObject()

        sendData.put("email",mUserEmailString )
        sendData.put("status", status)


        mSocket!!.emit("updatestatusnew", sendData)
        Log.i("ii",mUserEmailString!!)
        Log.i("ii",status)
        Log.i("ii",sendData.toString())







    }



    override fun onStart() {
        super.onStart()

        updatestatus("online")

    }

    override fun onStop() {
        super.onStop()

        updatestatus("offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        //FriendslistFragment.newInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()


        if (mGetAllMessagesListener != null) {
            mGetAllMessagesReference?.removeEventListener(mGetAllMessagesListener!!)
        }
        if (mUserChatRoomListener!=null){
            mUserChatRoomReference?.removeEventListener(mUserChatRoomListener!!)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as ChattingActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }


}
