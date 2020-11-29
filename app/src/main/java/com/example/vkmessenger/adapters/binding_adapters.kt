package com.example.vkmessenger.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vkmessenger.local.Friend
import timber.log.Timber

@BindingAdapter("app:friends")
fun RecyclerView.setFriends(friendList: List<Friend>?) {
    (adapter as? FriendsAdapter)?.submitList(friendList)
    Timber.d("BindingAdapter $friendList")
}