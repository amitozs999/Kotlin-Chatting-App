package com.amitozsingh.chatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.ChatRoom
import com.amitozsingh.chatapp.utils.User
import com.amitozsingh.chatapp.utils.encodeEmail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_room.view.*
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



class ChatroomAdapter(
    private val mActivity: MessagesActivity, var mListener: ChatRoomListener,
    var mCurrentUserEmail:String
) : RecyclerView.Adapter<ChatroomAdapter.ViewHolder>()  {


    private val mChatRooms: ArrayList<ChatRoom>

    private val mInflater: LayoutInflater


    init {
        mInflater = mActivity.layoutInflater
        mChatRooms = ArrayList()
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


    fun setmChatRoom(chatrooms: List<ChatRoom>) {
        mChatRooms.clear()
        mChatRooms.addAll(chatrooms)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(R.layout.list_room,parent,false)
        return ViewHolder(userView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )

        //var user=mMesages[position]





        holder.bindItems(mChatRooms[position],mCurrentUserEmail,mListener)
    }
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindItems(chatRoom: ChatRoom, currentUserEmail:String,mListener: ChatRoomListener){


//            Picasso.with(context)
//                .load(chatRoom.getFriendPicture())
//                .into(mUserPicture)


            val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
            userDatabase.child(encodeEmail(chatRoom.friendEmail)).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)

                    if(user!!.userPicture != null) {
                        try {


                            Picasso.get().load(user.userPicture).fit().into(itemView.list_chat_room_userPicture)
                        } catch (e: IllegalArgumentException) {

                        }
                    }
                }

            })

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
            itemView.list_chat_room_userName.setText(chatRoom.friendName)



            var lastMessageSent = AESDecryptionMethod(chatRoom.lastMessage!!)

            if (lastMessageSent.length > 40) {
                lastMessageSent = lastMessageSent.substring(0, 40) + " ..."
            }

            if (!chatRoom.sentLastMessage) {
                lastMessageSent = lastMessageSent+ " (Draft)"
            }



            if (!chatRoom.lastMessageRead) {
                itemView.list_chat_room_newMessageIndicator.setVisibility(View.VISIBLE)
            } else {
                itemView.list_chat_room_newMessageIndicator.setVisibility(View.GONE)
            }


if(chatRoom.messageType=="textMessage") {

    if (chatRoom.lastMessageSenderEmail.equals(currentUserEmail)) {
        itemView.list_chat_room_lastMessage.setText("Me: $lastMessageSent")
    } else {
        itemView.list_chat_room_lastMessage.setText(lastMessageSent)
    }

}
            else {

    if (chatRoom.lastMessageSenderEmail.equals(currentUserEmail)) {
        itemView.list_chat_room_lastMessage.setText("Me: Image")
    } else {
        itemView.list_chat_room_lastMessage.setText("Image")
    }
}


            itemView.setOnClickListener {
                mListener.OnChatRoomClicked(chatRoom)
            }



        }
    }

    override fun getItemCount(): Int {
        return mChatRooms.size
    }


    interface ChatRoomListener {
        fun OnChatRoomClicked(chatRoom: ChatRoom)
    }
}
