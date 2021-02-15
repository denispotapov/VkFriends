package com.example.vkmessenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkmessenger.databinding.FriendsItemBinding
import com.example.vkmessenger.local.Friend
import com.google.android.material.switchmaterial.SwitchMaterial
import de.hdodenhof.circleimageview.CircleImageView

class FriendsAdapter(private val context: Context) :
    ListAdapter<Friend, FriendsAdapter.FriendsHolder>(FriendsDiffCallback()) {

    private lateinit var binding: FriendsItemBinding
    private var listener: OnCheckChangedListener? = null

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
        holder.switchTracking.isChecked = getItem(position).tracking == true
    }

    interface OnCheckChangedListener {

        fun onCheckChanged(friend: Friend)
    }

    fun setOnCheckChangedListener(listener: OnCheckChangedListener) {
        this.listener = listener
    }

    inner class FriendsHolder(binding: FriendsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val firstName: TextView = binding.textFirstName
        val lastName: TextView = binding.textLastName
        val photoFriend: CircleImageView = binding.imageUser
        val switchTracking: SwitchMaterial = binding.switchTracking

        init {
            switchTracking.setOnCheckedChangeListener { _, isChecked ->
                val friend = getItem(adapterPosition)
                friend.tracking = isChecked
                listener?.onCheckChanged(friend)
            }
        }
    }
}

class FriendsDiffCallback : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean =
        oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName &&
                oldItem.photo == newItem.photo && oldItem.tracking == newItem.tracking
}
