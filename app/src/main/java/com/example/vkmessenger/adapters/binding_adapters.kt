package com.example.vkmessenger.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vkmessenger.local.Friend
import timber.log.Timber

@BindingAdapter("app:friends")
fun RecyclerView.setFriends(friendList: List<Friend>?) {
    (adapter as? FriendsAdapter)?.submitList(friendList)
}

@BindingAdapter("app:isGone")
fun View.bindIsGone(loading: Boolean) {
    visibility = if (loading) {
        View.GONE
    } else {
        View.VISIBLE
    }
}