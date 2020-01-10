package com.amitozsingh.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.User
import kotlinx.android.synthetic.main.friend_request_list_layout.view.*

class UserFriendsAdapter(
    private val mActivity: MessagesActivity,
    private val mListener: UserListener
) : RecyclerView.Adapter<UserFriendsAdapter.ViewHolder>()  {


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
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.user_friend_list_layout,parent,false)
        return  ViewHolder(userView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )

            var user=mUsers[position]




        holder.bindItems(mUsers[position])
    }
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindItems(user: User){


            //Picasso.get().load(user.userPicture).into(itemView.userdp)

            itemView.friend_request_userName.text=user.userName


        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    interface UserListener {
        fun OnUserClicked(user: User)
    }
}
