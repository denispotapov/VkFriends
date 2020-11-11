package com.example.vkmessenger.ui.friends

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.vkmessenger.R
import com.example.vkmessenger.ResultFriends
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.local.Friends
import com.example.vkmessenger.local.UserInfo
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FriendsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var friendsViewModel: FriendsViewModel

    private val items: List<UserInfo> = listOf()
    private val friends: Friends = Friends(items)
    private val resultFriends: ResultFriends = ResultFriends(friends)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        friendsViewModel = ViewModelProvider(this, providerFactory).get(FriendsViewModel::class.java)
        friendsViewModel.requestFriends(resultFriends)
    }

}