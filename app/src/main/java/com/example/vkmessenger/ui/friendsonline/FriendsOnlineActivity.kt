package com.example.vkmessenger.ui.friendsonline

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsOnlineBinding
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.activity_friends_online.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class FriendsOnlineActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var friendsOnlineViewModel: FriendsOnlineViewModel
    private lateinit var binding: ActivityFriendsOnlineBinding

    private val friendsOnlineAdapter = FriendsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_online)
        binding = ActivityFriendsOnlineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendsOnlineViewModel =
            ViewModelProvider(this, providerFactory).get(FriendsOnlineViewModel::class.java)
        binding.viewmodel = friendsOnlineViewModel

        binding.recyclerFriendsOnline.apply {
            adapter = friendsOnlineAdapter
            layoutManager = LinearLayoutManager(this@FriendsOnlineActivity)
        }

        with(binding.swipeRefreshLayout) {
            setColorSchemeResources(R.color.san_marino)
            setOnRefreshListener {
                friendsOnlineViewModel.refreshFriendsOnline()
                observeFriendsOnline()

                binding.swipeRefreshLayout.isRefreshing = false
                // todo learn about handler, looper, thread, runnable - coding in flow
            }
        }

        //todo know about apply, with, also, let, run

        fixedRateTimer("timer", false, 0, SECONDS_COUNT * MILLIS_IN_SECOND) {
            this@FriendsOnlineActivity.runOnUiThread {
                friendsOnlineViewModel.refreshFriendsOnline() //todo check thread
                observeFriendsOnline()
            }
        }
        observeToastMessage()
    }

    private fun observeFriendsOnline() {

        friendsOnlineViewModel.friendsOnline.observe(this@FriendsOnlineActivity, Observer {
            friendsOnlineAdapter.submitList(it)
        })
        // todo look how BindingAdapters used in sunflower and todo app
        // todo learn about BindingAdapter, InverseBindingAdapter, ListenerUtil
        //
    }

    private fun observeToastMessage() {
        friendsOnlineViewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}