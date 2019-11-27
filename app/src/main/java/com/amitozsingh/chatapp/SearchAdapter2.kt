package com.amitozsingh.chatapp

import androidx.recyclerview.widget.RecyclerView
import android.R
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.User
import kotlinx.android.synthetic.main.user_list.view.*


class FindFriendsAdapter(
    private val mActivity: MessagesActivity,
    private val mListener: UserListener
) : RecyclerView.Adapter<FindFriendsAdapter.ViewHolder>() {


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
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.user_list,parent,false)
return  ViewHolder(userView)
      //  val findFriendsViewHolder = FindFriendsViewHolder(userView)
//        findFriendsViewHolder.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View) {
//                val user = findFriendsViewHolder.itemView.tag as User
//                mListener.OnUserClicked(user)
//            }
//        })
     //   return findFriendsViewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )
        holder.bindItems(mUsers[position])
    }
    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        fun bindItems(user: User){
            itemView.username.text=user.userName
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    interface UserListener {
        fun OnUserClicked(user: User)
    }
}
