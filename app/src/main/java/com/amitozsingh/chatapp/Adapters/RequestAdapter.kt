package com.amitozsingh.chatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_request_list_layout.view.*
import kotlinx.android.synthetic.main.friend_request_list_layout.view.userdp


class RequestAdapter(
    private val mActivity: MessagesActivity,
    private val mListener: OnOptionListener
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>()  {


    private val mUsers: ArrayList<User>
    private val mInflater: LayoutInflater


    init {
        mInflater = mActivity.layoutInflater
        mUsers = ArrayList()


    }


    fun setmUsers(users: List<User>) {
        mUsers.clear()
        mUsers.addAll(users)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.friend_request_list_layout,parent,false)
        return ViewHolder(userView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )
        holder.itemView.acceptRequest.setOnClickListener {
            var user=mUsers[position]


            mListener.OnOptionClicked(user,"0")

        }
        holder.itemView.rejectRequest.setOnClickListener {
            var user=mUsers[position]


            mListener.OnOptionClicked(user,"1")
        }

        holder.bindItems(mUsers[position])
    }
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindItems(user: User){


            Picasso.get().load(user.userPicture).fit().into(itemView.userdp)
            //Picasso.get().load(user.userPicture).into(itemView.userdp)

            itemView.friend_request_userName.text=user.userName


        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    interface OnOptionListener {
        fun OnOptionClicked(user: User, result: String)
    }
}
