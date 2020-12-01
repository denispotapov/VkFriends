package com.example.vkmessenger.ui.friendsonline

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsOnlineBinding
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.activity_friends_online.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer

class FriendsOnlineActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var friendsOnlineViewModel: FriendsOnlineViewModel
    private lateinit var binding: ActivityFriendsOnlineBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val friendsOnlineAdapter = FriendsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_online)
        binding = ActivityFriendsOnlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        friendsOnlineViewModel =
            ViewModelProvider(this, providerFactory).get(FriendsOnlineViewModel::class.java).also {
                binding.viewmodel = it
            }

        initRecycler()
        refreshFriendsOnlineBySwipe()
        observeToastMessage()
    }

    override fun onStart() {
        super.onStart()
        refreshFriendsOnlineByTime()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    private fun initRecycler() {
        binding.recyclerFriendsOnline.apply {
            adapter = friendsOnlineAdapter
        }
    }

    private fun refreshFriendsOnlineBySwipe() {
        with(binding.swipeRefreshLayout) {
            setColorSchemeResources(R.color.san_marino)
            setOnRefreshListener {
                friendsOnlineViewModel.refreshFriendsOnline()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun observeToastMessage() {
        friendsOnlineViewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun refreshFriendsOnlineByTime() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                friendsOnlineViewModel.refreshFriendsOnline()
                handler.postDelayed(this, SECONDS_COUNT * MILLIS_IN_SECOND)
            }
        }
        handler.post(runnable)
    }
}

// todo look how BindingAdapters used in sunflower and todo app
// todo learn about BindingAdapter, InverseBindingAdapter, ListenerUtil
// todo know about apply, with, also, let, run
// todo learn about handler, looper, thread, runnable - coding in flow