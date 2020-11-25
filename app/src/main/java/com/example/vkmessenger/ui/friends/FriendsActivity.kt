package com.example.vkmessenger.ui.friends

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
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

        recycler_view.apply {
            adapter = friendsAdapter
            layoutManager = LinearLayoutManager(this@FriendsActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.friends_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.friends_online -> {
                val intent = Intent(this@FriendsActivity, FriendsOnlineActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}