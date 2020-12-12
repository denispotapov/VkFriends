package com.example.vkmessenger.ui.friends

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsBinding
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import javax.inject.Inject

class FriendsActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var binding: ActivityFriendsBinding

    private val friendsAdapter = FriendsAdapter(this@FriendsActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        friendsViewModel =
            ViewModelProvider(this, providerFactory).get(FriendsViewModel::class.java).also {
                binding.viewmodel = it
            }

        initRecycler()
        friendsViewModel.requestFriends()
        observeToastMessage()
    }

    private fun initRecycler() {
        binding.recyclerFriends.apply {
            adapter = friendsAdapter
        }
    }

    private fun observeToastMessage() {
        friendsViewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
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