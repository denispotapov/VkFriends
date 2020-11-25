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
import com.example.vkmessenger.local.Friend
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.friends_item.view.*

class FriendsAdapter(private val context: Context) :
    ListAdapter<Friend, FriendsAdapter.FriendsHolder>(FriendsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder {
        return FriendsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.friends_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        holder.firstName.text = getItem(position).firstName
        holder.lastName.text = getItem(position).lastName
        Glide.with(context)
            .load(getItem(position).photo)
            .into(holder.photoFriend)
    }

    inner class FriendsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val firstName: TextView = itemView.text_first_name
        val lastName: TextView = itemView.text_last_name
        val photoFriend: CircleImageView = itemView.image_user
    }
}

class FriendsDiffCallback : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName &&
                oldItem.photo == newItem.photo
}