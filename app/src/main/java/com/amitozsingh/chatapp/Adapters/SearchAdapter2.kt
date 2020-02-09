package com.amitozsingh.chatapp.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.R
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.User
import com.amitozsingh.chatapp.utils.isIncludedInMap
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_list.view.*
import kotlinx.android.synthetic.main.user_list.view.userdp
import java.lang.IllegalArgumentException


class FindFriendsAdapter(
    private val mActivity: MessagesActivity,
    private val mListener: UserListener
) : RecyclerView.Adapter<FindFriendsAdapter.ViewHolder>() , Filterable {




    private val mUsers: ArrayList<User>
    private val mInflater: LayoutInflater

    private var mFriendRequestSentMap: HashMap<String, User>? = null
    private var mFriendRequestRecievedMap: HashMap<String, User>? = null
    private var mCurrentUserFriendsMap: HashMap<String, User>? = null


    init {
        mInflater = mActivity.layoutInflater
        mUsers = ArrayList()
        mFriendRequestSentMap = HashMap()
        mFriendRequestRecievedMap= HashMap()
        mCurrentUserFriendsMap=HashMap()

    }


    fun setmUsers(users: List<User>) {
        mUsers.clear()
        mUsers.addAll(users)
        notifyDataSetChanged()
    }
    fun setmFriendRequestSentMap(friendRequestSentMap: HashMap<String, User>) {
        mFriendRequestSentMap!!.clear()
        mFriendRequestSentMap!!.putAll(friendRequestSentMap)
        notifyDataSetChanged()
    }

    fun setmFriendRequestRecievedMap(friendRequestRecievedMap: HashMap<String, User>) {
        mFriendRequestRecievedMap!!.clear()
        mFriendRequestRecievedMap!!.putAll(friendRequestRecievedMap)
        notifyDataSetChanged()
    }
    fun setmUserFriendMap(currentUserfriendmap: HashMap<String, User>) {
        mCurrentUserFriendsMap!!.clear()
        mCurrentUserFriendsMap!!.putAll(currentUserfriendmap)
        notifyDataSetChanged()
    }


    var filteredlist=mUsers
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults? {


                var key=constraint.toString()
                if(key.isEmpty()){



                    filteredlist=mUsers
                }
                else{
                    var newfiltered=ArrayList<User>()
                    for(i in mUsers)  //filteredlist
                    {
                        if(i.email!!.toLowerCase().startsWith(key.toLowerCase()))
                            newfiltered.add(i)
                    }

                    filteredlist=newfiltered
//                    if(filteredlist.isEmpty()){
//                        FriendServices().getMatchingUsers(filteredlist)
//
//                    }
                }
                var filterResults=FilterResults()
                filterResults.values=filteredlist
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {

                filteredlist=results.values  as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.user_list,parent,false)
return ViewHolder(userView)
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
        holder.itemView.reqsentbutton.setOnClickListener {
            var user=mUsers[position]
            mListener.OnUserClicked(user)
        }
        holder.itemView.reqsendbutton.setOnClickListener {
            var user=mUsers[position]
            mListener.OnUserClicked(user)
        }
        holder.bindItems(filteredlist[position],mFriendRequestSentMap,mFriendRequestRecievedMap!!,mCurrentUserFriendsMap!!)
    }
    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        fun bindItems(user: User,friendRequestSentMap:HashMap<String,User>?,friendRequestRecievedMap: HashMap<String, User>,UserFriendMap: HashMap<String, User>){



try {

    Picasso.get().load(user.userPicture).fit().into(itemView.userdp)
}
catch (e:IllegalArgumentException){

}


            itemView.username.text=user.userName

            if (isIncludedInMap(friendRequestSentMap,user)){
                //senttv.setVisibility(View.VISIBLE);
                itemView.senttv.text="Friend Request Sent"

                itemView.reqsendbutton.setVisibility(View.GONE)
                itemView.reqsentbutton.setVisibility(View.VISIBLE)
        //        itemView.reqsentbutton.setImageResource(R.drawable.btn_minus);
                //mAddFriend.setVisibility(View.VISIBLE);

            }else if(isIncludedInMap(friendRequestRecievedMap,user)){
                itemView.senttv.text="User sent you request"

                itemView.reqsendbutton.setVisibility(View.GONE)
                itemView.reqsentbutton.setVisibility(View.GONE)

            }else if(isIncludedInMap(UserFriendMap,user)){
                //mUserStatus.setVisibility(View.VISIBLE);
                itemView.senttv.text="You are Friends"
                //mAddFriend.setVisibility(View.GONE);
                itemView.reqsendbutton.setVisibility(View.GONE)
                itemView.reqsentbutton.setVisibility(View.GONE)
            }
            else{
                itemView.reqsentbutton.setVisibility(View.VISIBLE)
                itemView.senttv.setText("Add Friend")
                itemView.reqsendbutton.setVisibility(View.VISIBLE)
                itemView.reqsentbutton.setVisibility(View.GONE)
         //       itemView.reqsentbutton.setImageResource()

            }
        }
    }

    override fun getItemCount(): Int {
        return filteredlist.size
    }

    interface UserListener {
        fun OnUserClicked(user: User)
    }
}
