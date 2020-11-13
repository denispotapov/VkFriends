package com.example.vkmessenger.ui.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.local.UserInfo
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.friends_item.view.*

class FriendsAdapter(private val context: Context) :
    ListAdapter<UserInfo, FriendsAdapter.FriendsHolder>(FriendsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder {
        return FriendsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.friends_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        holder.firstName.text = getItem(position).first_name
        holder.lastName.text = getItem(position).last_name
        Glide.with(context)
            .load(getItem(position).photo_100)
            .into(holder.photoFriend)
    }


    inner class FriendsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.first_name
        val lastName: TextView = itemView.last_name
        val photoFriend: CircleImageView = itemView.image
    }

}

class FriendsDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.first_name == newItem.first_name && oldItem.last_name == newItem.last_name &&
                oldItem.photo_100 == newItem.photo_100
    }
}