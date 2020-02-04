package com.amitozsingh.chatapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.R
import com.amitozsingh.chatapp.utils.User

class SearchAdapter(var mActivity:MessagesActivity, var mListener: UserListener, private var userlist: ArrayList<User>) : RecyclerView.Adapter<SearchAdapter.myviewHolder>() {

    fun addElement(chat: User) {
        userlist.add(chat)
        notifyDataSetChanged()
    }
    fun setmUsers(users: List<User>) { Log.i("AMITOZ1","AGYA1")
        userlist.clear()
      userlist.addAll(users)
        notifyDataSetChanged()
    }

    class myviewHolder( val view: View): RecyclerView.ViewHolder(view)



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): myviewHolder {
        Log.i("AMITOZ1","AGYA")
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=li.inflate(R.layout.user_list,parent,false)
        return myviewHolder(view)


    }

    override fun getItemCount(): Int = userlist.size

    override fun onBindViewHolder(holder: myviewHolder, position: Int) {
        val item1= this.userlist.get(position)

        //var layout = holder.view.findViewById<View>(R.id.chatLayout)
        var userdp = holder.view.findViewById<ImageView>(R.id.userdp)
        var username = holder.view.findViewById<TextView>(R.id.username)
        Log.i("AMITOZ1",item1.userName!!)
        username.text=item1.userName
//        if(userdp!=null){
//            Picasso.get().load(item1.userPicture).into(userdp)
//        }
//        layout.setOnClickListener{
//
//            val intent= Intent(it.context, ChatActivity::class.java)
//            intent.putExtra("chatid",item1.chatId)
//            intent.putExtra("userid",item1.userId)
//            intent.putExtra("imageurl",item1.imageUrl)
//            intent.putExtra("otheruserid",item1.otherUserId)
//
//
//
//            ContextCompat.startActivity(it.context,intent,null)
//
//        }

         Log.i("AMITOZ1",item1.userName!!)
    }
    interface UserListener {
        fun OnUserClicked(user: User)
    }
}