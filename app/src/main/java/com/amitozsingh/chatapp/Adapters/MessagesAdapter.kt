package com.amitozsingh.chatapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.ChattingActivity
import com.amitozsingh.chatapp.Fragments.ChattingFragment
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.Message
import com.amitozsingh.chatapp.utils.User
import com.amitozsingh.chatapp.utils.encodeEmail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.list_messages.view.*
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec


private var stringMessage: String? = null
private var encryptionKey = byteArrayOf(56, 15, 78, 6, 85, 67, 7, -23, 78, 4, 6, 70, 65, -15, 19, 53)
private var cipher: Cipher? = null
private  var decipher: Cipher? = null
private var secretKeySpec: SecretKeySpec? = null

class MessagesAdapter(
    private val mActivity: ChattingActivity,
    var mCurrentUserEmail:String
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>()  {



    private val mMesages: ArrayList<Message>
    private val mInflater: LayoutInflater


    init {
        mInflater = mActivity.layoutInflater
        mMesages = ArrayList()
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


    fun setmUsers(messages: List<Message>) {
        mMesages.clear()
        mMesages.addAll(messages)
        notifyDataSetChanged()
    }

    fun getmMessages(): List<Message> {
        return mMesages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(R.layout.list_messages,parent,false)
        return ViewHolder(userView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )

        //var user=mMesages[position]





        holder.bindItems(mMesages[position],mCurrentUserEmail)
    }
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindItems(message: Message,currentUserEmail:String){





            fun AESDecryptionMethod(string: String): String {
                val EncryptedByte = string.toByteArray(charset("ISO-8859-1"))
                var decryptedString = string

                val decryption: ByteArray

                try {
                    decipher?.init(Cipher.DECRYPT_MODE, secretKeySpec)
                    decryption = decipher!!.doFinal(EncryptedByte)
                    decryptedString = String(decryption)
                } catch (e: InvalidKeyException) {
                    e.printStackTrace()
                } catch (e: BadPaddingException) {
                    e.printStackTrace()
                } catch (e: IllegalBlockSizeException) {
                    e.printStackTrace()
                }

                return decryptedString
            }






Log.i("azi",message.finaltime)





            if (!currentUserEmail.equals(message.messageSenderEmail)){






                itemView.mylayout.visibility=View.GONE
                itemView.friendlayout.visibility=View.VISIBLE

                itemView.list_messages_time_friend.text=message.finaltime
                itemView.list_messages_time_my.visibility=View.GONE


                val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
                userDatabase.child(encodeEmail(message.messageSenderEmail)).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val user = p0.getValue(User::class.java)

                        if(user!!.userPicture != null) {
                            try {


                         //       Picasso.get().load(user.userPicture).fit().into(itemView.list_messages_friendPicture)
                            } catch (e: IllegalArgumentException) {

                            }
                        }
                    }

                })





                Log.i("aa","2")
                if(message.messageType=="textMessage"){


                    itemView.friendcardviewimage.visibility=View.GONE



                    Log.i("aa","3")

//                    itemView.list_messages_userPicture.visibility=View.GONE      //user gone
                    itemView.list_messages_UserText.visibility=View.GONE

                    itemView.list_messages_messagePicUser.visibility=View.GONE     //pic gone
                    itemView.list_messages_messagePicfriend.visibility=View.GONE

//                    itemView.list_messages_friendPicture.visibility=View.VISIBLE    //friend visible

                    itemView.list_messages_friendText.visibility=View.VISIBLE

                    // Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_friendPicture)

                    itemView.list_messages_friendText.setText(AESDecryptionMethod(message.messageText))

                    //Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_friendPicture)

                   }

                        else {


                    itemView.friendcardviewimage.visibility=View.VISIBLE

                    Log.i("aa", "4")

        //            itemView.list_messages_userPicture.visibility = View.GONE      //user gone
                    itemView.list_messages_UserText.visibility = View.GONE

                    itemView.list_messages_messagePicUser.visibility = View.GONE    //pic gone(user)

                    itemView.list_messages_friendText.visibility = View.GONE         //text gone(friend)

          //          itemView.list_messages_friendPicture.visibility = View.VISIBLE   //friend visible
                    itemView.list_messages_messagePicfriend.visibility=View.VISIBLE

                    Picasso.get().load(AESDecryptionMethod(message.messageText)).transform(RoundedCornersTransformation(10,10)).placeholder(R.drawable.ic_photo_library).resize(450,450)
                        .into(itemView.list_messages_messagePicfriend)


                    //Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_friendPicture)


                          }




            } else{





                itemView.mylayout.visibility=View.VISIBLE
                itemView.friendlayout.visibility=View.GONE


                itemView.list_messages_time_my.text=message.finaltime

                itemView.list_messages_time_friend.visibility=View.GONE



                val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
                userDatabase.child(encodeEmail(currentUserEmail)).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val user = p0.getValue(User::class.java)

                        if(user!!.userPicture != null) {
                            try {


                             //   Picasso.get().load(user.userPicture).fit().into(itemView.list_messages_userPicture)
                            } catch (e: IllegalArgumentException) {

                            }
                        }
                    }

                })



                Log.i("aa","5")
                if(message.messageType=="textMessage"){

                    itemView.mycardviewimage.visibility=View.GONE

                    Log.i("aa","6")
           //         itemView.list_messages_userPicture.visibility=View.VISIBLE   //user visible
                    itemView.list_messages_UserText.visibility=View.VISIBLE

                    itemView.list_messages_messagePicUser.visibility=View.GONE    //Pic gone
                    itemView.list_messages_messagePicfriend.visibility=View.GONE

             //       itemView.list_messages_friendPicture.visibility=View.GONE    //frieend gone
                    itemView.list_messages_friendText.visibility=View.GONE

                    //Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_userPicture)

                    itemView.list_messages_UserText.setText(AESDecryptionMethod(message.messageText))

                   // Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_userPicture)
                }

              else{

                    itemView.mycardviewimage.visibility=View.VISIBLE

                    Log.i("aa","7")
               //     itemView.list_messages_userPicture.visibility=View.VISIBLE     //user visible


                    itemView.list_messages_UserText.visibility=View.GONE          //text gone (user)


                    itemView.list_messages_messagePicfriend.visibility=View.GONE    //pic gone(friend)

                 //   itemView.list_messages_friendPicture.visibility=View.GONE
                    itemView.list_messages_friendText.visibility=View.GONE             //friend gonne

                    itemView.list_messages_messagePicUser.visibility=View.VISIBLE

                    Picasso.get().load(AESDecryptionMethod(message.messageText)).transform(RoundedCornersTransformation(10,10)).placeholder(R.drawable.ic_photo_library).resize(450,450).into(itemView.list_messages_messagePicUser)

                //    Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_userPicture)
                }



            }


//            itemView.setOnClickListener {
//                mListener.OnUserClicked(user)
//            }



        }
    }

    override fun getItemCount(): Int {
        return mMesages.size
    }

//    interface UserListener {
//        fun OnUserClicked(user: User)
//    }
}
