package com.example.firebasechat


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechat.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var messages: ArrayList<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view){
        var msgText = view.findViewById<TextView>(R.id.msgTv)
        var usernameText=view.findViewById<TextView>(R.id.usernameTV)
        var messgeProgress=view.findViewById<ProgressBar>(R.id.msgProgress)
        var msgStatusIm=view.findViewById<ImageView>(R.id.msgStatusIm)
        var imageFileLayout=view.findViewById<ConstraintLayout>(R.id.image_file_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        when(viewType) {
            1-> return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_msg_layout, parent, false))
            else-> return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_msg_layout_other, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.usernameText.text=messages[position].nickname+ " "
        holder.msgText.text = messages[position].msg
        when(messages[position].status){
            1 ->{
                holder.messgeProgress.visibility=View.VISIBLE
                holder.msgStatusIm.visibility=View.INVISIBLE}
            2 ->{
                holder.messgeProgress.visibility=View.INVISIBLE
                holder.msgStatusIm.visibility=View.VISIBLE
                holder.msgStatusIm.setImageResource(R.drawable.ic_baseline_check_24)
          //      holder.msgStatusIm.setBackgroundColor(Color.GREEN)
                holder.msgStatusIm.setColorFilter(Color.GREEN)
            }
            3->{
                holder.messgeProgress.visibility=View.INVISIBLE
                holder.msgStatusIm.visibility=View.VISIBLE
                holder.msgStatusIm.setImageResource(R.drawable.ic_baseline_error_outline_24)
                //      holder.msgStatusIm.setBackgroundColor(Color.GREEN)
                holder.msgStatusIm.setColorFilter(Color.RED)
            }
            4->{
                holder.messgeProgress.visibility=View.INVISIBLE
                holder.msgStatusIm.visibility=View.VISIBLE
                holder.msgStatusIm.setImageResource(R.drawable.ic_doublecheck)
                //      holder.msgStatusIm.setBackgroundColor(Color.GREEN)
                holder.msgStatusIm.setColorFilter(Color.GREEN)
            }
        }
        when(messages[position].messageType){
            1->{
                holder.imageFileLayout.visibility=View.GONE
            }
            2->{
                holder.imageFileLayout.visibility=View.VISIBLE
                var im=holder.imageFileLayout.findViewById<ImageView>(R.id.imageFileIm)
                Glide.with(holder.itemView.context).load(messages[position].imageUrl).into(im)

            }
//            3->{
//                holder.imageFileLayout.visibility=View.VISIBLE
//                var im=holder.imageFileLayout.findViewById<ImageView>(R.id.imageFileIm)
//                Glide.with(holder.itemView.context).load(messages[position].imageUrl).into(im)
//                holder.msgText.visibility=View.GONE
//            }
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        if(messages[position].username==FirebaseAuth.getInstance().currentUser?.email)
       //     if(messages[position].username=="m@m.com")
            return 1
        else
            return 2
    }
}