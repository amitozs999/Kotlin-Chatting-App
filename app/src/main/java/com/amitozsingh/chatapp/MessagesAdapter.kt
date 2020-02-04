package com.amitozsingh.chatapp

import android.content.Context
import android.util.Log
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








Log.i("aa","1")


            if (!currentUserEmail.equals(message.messageSenderEmail)){


                Log.i("aa","2")
                if(message.messageType=="textMessage"){


                    Log.i("aa","3")

                    itemView.list_messages_userPicture.visibility=View.GONE      //user gone
                    itemView.list_messages_UserText.visibility=View.GONE

                    itemView.list_messages_messagePicUser.visibility=View.GONE     //pic gone
                    itemView.list_messages_messagePicfriend.visibility=View.GONE

                    itemView.list_messages_friendPicture.visibility=View.VISIBLE    //friend visible
                    itemView.list_messages_friendText.visibility=View.VISIBLE

                    // Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_friendPicture)

                    itemView.list_messages_friendText.setText(message.messageText)


                   }

                        else {

                    Log.i("aa", "4")

                    itemView.list_messages_userPicture.visibility = View.GONE      //user gone
                    itemView.list_messages_UserText.visibility = View.GONE

                    itemView.list_messages_messagePicUser.visibility = View.GONE    //pic gone(user)

                    itemView.list_messages_friendText.visibility = View.GONE         //text gone(friend)

                    itemView.list_messages_friendPicture.visibility = View.VISIBLE   //friend visible
                    itemView.list_messages_messagePicfriend.visibility=View.VISIBLE

                    Picasso.get().load(message.messageText)
                        .into(itemView.list_messages_messagePicfriend)


                          }




            } else{

                Log.i("aa","5")
                if(message.messageType=="textMessage"){


                    Log.i("aa","6")
                    itemView.list_messages_userPicture.visibility=View.VISIBLE   //user visible
                    itemView.list_messages_UserText.visibility=View.VISIBLE

                    itemView.list_messages_messagePicUser.visibility=View.GONE    //Pic gone
                    itemView.list_messages_messagePicfriend.visibility=View.GONE

                    itemView.list_messages_friendPicture.visibility=View.GONE    //frieend gone
                    itemView.list_messages_friendText.visibility=View.GONE

                    //Picasso.get().load(message.messageSenderPicture).into(itemView.list_messages_userPicture)

                    itemView.list_messages_UserText.setText(message.messageText)
                }

              else{

                    Log.i("aa","7")
                    itemView.list_messages_userPicture.visibility=View.VISIBLE     //user visible


                    itemView.list_messages_UserText.visibility=View.GONE          //text gone (user)


                    itemView.list_messages_messagePicfriend.visibility=View.GONE    //pic gone(friend)

                    itemView.list_messages_friendPicture.visibility=View.GONE
                    itemView.list_messages_friendText.visibility=View.GONE             //friend gonne

                    itemView.list_messages_messagePicUser.visibility=View.VISIBLE

                    Picasso.get().load(message.messageText).placeholder(R.drawable.ic_launcher_background).into(itemView.list_messages_messagePicUser)
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
