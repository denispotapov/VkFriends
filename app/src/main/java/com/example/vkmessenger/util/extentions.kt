package com.example.vkmessenger.util

import androidx.appcompat.widget.SearchView
import com.example.vkmessenger.ui.friends.FriendsViewModel

fun setOnQueryTextListener(
    searchView: SearchView,
    friendsViewModel: FriendsViewModel
) {
    searchView.setOnQueryTextListener(object :
        SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { friendsViewModel.filterFriends(it) }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { friendsViewModel.filterFriends(it) }
            return false
        }
    })
}