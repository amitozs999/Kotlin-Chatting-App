package com.amitozsingh.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.ChattingActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.Message

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_messages.view.*


class MessagesAdapter(
    private val mActivity: ChattingActivity,
    var mCurrentUserEmail:String
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>()  {


    private val mMesages: ArrayList<Message>
    private val mInflater: LayoutInflater


    init {
        mInflater = mActivity.layoutInflater
        mMesages = ArrayList()


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
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.list_messages,parent,false)
        return  ViewHolder(userView)

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

            if (!currentUserEmail.equals(message.messageSenderEmail)){
                itemView.list_messages_userPicture.visibility=View.GONE
                itemView.list_messages_UserText.visibility=View.GONE
                itemView.list_messages_friendPicture.visibility=View.VISIBLE
                itemView.list_messages_friendText.visibility=View.VISIBLE

               // Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_friendPicture)

                itemView.list_messages_friendText.setText(message.messageText)
            } else{
                itemView.list_messages_userPicture.visibility=View.VISIBLE
                itemView.list_messages_UserText.visibility=View.VISIBLE
                itemView.list_messages_friendPicture.visibility=View.GONE
                itemView.list_messages_friendText.visibility=View.GONE

                //Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_userPicture)

                itemView.list_messages_UserText.setText(message.messageText)
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
