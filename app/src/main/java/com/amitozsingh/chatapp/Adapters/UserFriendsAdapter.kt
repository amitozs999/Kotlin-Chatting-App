package com.amitozsingh.chatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.User
import com.amitozsingh.chatapp.utils.encodeEmail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
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
        val userView=li.inflate(R.layout.user_friend_list_layout,parent,false)
        return ViewHolder(userView)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )

            var user=mUsers[position]





        holder.bindItems(mListener,mUsers[position])
    }
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindItems(mListener: UserListener, user: User){


            val userDatabase = FirebaseDatabase.getInstance().reference.child("users")
            userDatabase.child(encodeEmail(user.email)).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)

                    if(user!!.userPicture != null) {
                        try {


                            Picasso.get().load(user.userPicture).fit().into(itemView.userdp)
                        } catch (e: IllegalArgumentException) {

                        }
                    }
                }

            })



            //Picasso.get().load(user.userPicture).fit().into(itemView.userdp)

            itemView.friend_request_userName.text=user.userName

            itemView.setOnClickListener {
                mListener.OnUserClicked(user)
            }



        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    interface UserListener {
        fun OnUserClicked(user: User)
    }
}
