package com.example.vkmessenger.ui.friendsonline

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.ui.friends.FriendsAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.activity_friends.recycler_view
import kotlinx.android.synthetic.main.activity_friends_online.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

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


        fixedRateTimer("timer", false, 0, 30*1000) {
           this@FriendsOnlineActivity.runOnUiThread {
               observeFriendsOnline()
           }
       }

        recycler_view.apply {
            adapter = friendsOnlineAdapter
            layoutManager = LinearLayoutManager(this@FriendsOnlineActivity)
        }
    }

    private fun observeFriendsOnline() {

        friendsOnlineViewModel.friendsOnline()

        friendsOnlineViewModel.friendsOnline.observe(this, Observer {
            friendsOnlineAdapter.submitList(it)
        })
    }
}