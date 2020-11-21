package com.example.vkmessenger.ui.friends

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import javax.inject.Inject

class FriendsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var friendsViewModel: FriendsViewModel

    private val friendsAdapter = FriendsAdapter(this@FriendsActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        friendsViewModel =
            ViewModelProvider(this, providerFactory).get(FriendsViewModel::class.java)

        friendsViewModel.allFriends.observe(this, Observer {
            friendsAdapter.submitList(it)
        })

        friendsViewModel.requestFriends()

        friendsViewModel.onlineFriends.observe(this@FriendsActivity, Observer {
            Log.d("TAG", "Online: $it")
        })

        recycler_view.apply {
            adapter = friendsAdapter
            layoutManager = LinearLayoutManager(this@FriendsActivity)
        }
    }
}