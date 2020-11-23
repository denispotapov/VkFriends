package com.example.vkmessenger.ui.friendsonline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.ui.friends.FriendsAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import javax.inject.Inject

class FriendsOnlineActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var friendsOnlineViewModel: FriendsOnlineViewModel

    private val friendsOnlineAdapter = FriendsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_online)

        friendsOnlineViewModel =
            ViewModelProvider(this, providerFactory).get(FriendsOnlineViewModel::class.java)

        friendsOnlineViewModel.onlineFriends.observe(this@FriendsOnlineActivity, Observer {
            friendsOnlineAdapter.submitList(it)
        })

        recycler_view.apply {
            adapter = friendsOnlineAdapter
            layoutManager = LinearLayoutManager(this@FriendsOnlineActivity)
        }
    }
}