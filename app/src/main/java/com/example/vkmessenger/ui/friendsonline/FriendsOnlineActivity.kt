package com.example.vkmessenger.ui.friendsonline

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsOnlineBinding
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.service.StatusTrackingService
import com.example.vkmessenger.util.JOB_ID
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

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
        startStatusTrackingService()
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

    private fun startStatusTrackingService() {

        friendsOnlineAdapter.setOnButtonClickListener(object :
            FriendsAdapter.OnButtonClickListener {
            override fun onButtonClick(friend: Friend, position: Int) {

                val bundle = PersistableBundle()
                val friendToJobService =
                    Friend(friend.id, friend.firstName, friend.lastName, friend.photo)
                val gSon = Gson()
                val json = gSon.toJson(friendToJobService)
                bundle.putString("friend", json)

                val componentName =
                    ComponentName(this@FriendsOnlineActivity, StatusTrackingService::class.java)
                val info = JobInfo.Builder(JOB_ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(SECONDS_COUNT * MILLIS_IN_SECOND)
                    .setExtras(bundle)
                    .build()

                val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val resultCode = scheduler.schedule(info)
                if (resultCode == JobScheduler.RESULT_SUCCESS) {
                    Timber.d("Job scheduled")
                } else Timber.d("Job scheduling failed")

                Toast.makeText(
                    this@FriendsOnlineActivity,
                    "Служба отслеживания активирована",
                    Toast.LENGTH_LONG
                ).show()
                Timber.d("OnClick: ${friend.id} + $position")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.friends_online_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.stop_tracking -> {
                val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                scheduler.cancel(JOB_ID)
                Timber.d("Job cancelled")
                Toast.makeText(
                    this@FriendsOnlineActivity,
                    "Служба отслеживания остановлена",
                    Toast.LENGTH_LONG
                ).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        refreshFriendsOnlineByTime()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
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
