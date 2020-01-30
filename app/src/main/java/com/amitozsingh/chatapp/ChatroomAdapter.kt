package com.amitozsingh.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.ChattingActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.Message
import kotlinx.android.synthetic.main.list_messages.view.*
import com.amitozsingh.chatapp.utils.ChatRoom

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_room.view.*








class ChatroomAdapter(
    private val mActivity: MessagesActivity,var mListener:ChatRoomListener,
    var mCurrentUserEmail:String
) : RecyclerView.Adapter<ChatroomAdapter.ViewHolder>()  {


    private val mChatRooms: ArrayList<ChatRoom>

    private val mInflater: LayoutInflater


    init {
        mInflater = mActivity.layoutInflater
        mChatRooms = ArrayList()


    }


    fun setmChatRoom(chatrooms: List<ChatRoom>) {
        mChatRooms.clear()
        mChatRooms.addAll(chatrooms)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.list_room,parent,false)
        return  ViewHolder(userView)

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

            itemView.list_chat_room_userName.setText(chatRoom.friendName)

            var lastMessageSent = chatRoom.lastMessage

            if (lastMessageSent!!.length > 40) {
                lastMessageSent = lastMessageSent.substring(0, 40) + " ..."
            }

            if (!chatRoom.sentLastMessage) {
                lastMessageSent = lastMessageSent + " (Draft)"
            }

            if (chatRoom.lastMessageSenderEmail.equals(currentUserEmail)) {
                lastMessageSent = "Me: $lastMessageSent"
            }

            if (!chatRoom.lastMessageRead) {
                itemView.list_chat_room_newMessageIndicator.setVisibility(View.VISIBLE)
            } else {
                itemView.list_chat_room_newMessageIndicator.setVisibility(View.GONE)
            }



            itemView.list_chat_room_lastMessage.setText(lastMessageSent)


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
