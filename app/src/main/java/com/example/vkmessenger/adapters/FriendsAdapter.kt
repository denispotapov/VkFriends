package com.example.vkmessenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkmessenger.databinding.FriendsItemBinding
import com.example.vkmessenger.local.Friend
import de.hdodenhof.circleimageview.CircleImageView

class FriendsAdapter(private val context: Context) :
    ListAdapter<Friend, FriendsAdapter.FriendsHolder>(FriendsDiffCallback()) {

    private lateinit var binding: FriendsItemBinding
    private var listener: OnButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder {
        binding = FriendsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {

        holder.firstName.text = getItem(position).firstName
        holder.lastName.text = getItem(position).lastName
        Glide.with(context)
            .load(getItem(position).photo)
            .into(holder.photoFriend)
    }

    inner class FriendsHolder(binding: FriendsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val firstName: TextView = binding.textFirstName
        val lastName: TextView = binding.textLastName
        val photoFriend: CircleImageView = binding.imageUser
        val buttonTracking: Button = binding.buttonActivateTracking

        init {
            buttonTracking.setOnClickListener {
                val friend = getItem(adapterPosition)
                listener?.onButtonClick(friend, adapterPosition)
            }
        }
    }

    interface OnButtonClickListener {
        fun onButtonClick(friend: Friend, position: Int)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.listener = listener
    }
}

class FriendsDiffCallback : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName &&
                oldItem.photo == newItem.photo
}
