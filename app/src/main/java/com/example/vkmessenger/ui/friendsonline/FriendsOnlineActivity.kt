package com.example.vkmessenger.ui.friendsonline

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.vkmessenger.R
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsOnlineBinding
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.service.StatusTrackingService
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FriendsOnlineActivity : AppCompatActivity() {

    private val friendsOnlineViewModel: FriendsOnlineViewModel by viewModel()
    private lateinit var binding: ActivityFriendsOnlineBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val friendsOnlineAdapter = FriendsAdapter(this@FriendsOnlineActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_online)
        binding = ActivityFriendsOnlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewmodel = friendsOnlineViewModel
        binding.lifecycleOwner = this

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
        friendsOnlineAdapter.setOnCheckChangedListener(object :
            FriendsAdapter.OnCheckChangedListener {

            override fun onCheckChanged(friend: Friend) {
                friendsOnlineViewModel.updateFriend(friend)

                if (friend.tracking == true) {
                    val componentName =
                        ComponentName(this@FriendsOnlineActivity, StatusTrackingService::class.java)
                    val info = JobInfo.Builder(friend.id, componentName)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                        .setPeriodic(SECONDS_COUNT * MILLIS_IN_SECOND)
                        .build()

                    val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                    val resultCode = scheduler.schedule(info)
                    if (resultCode == JobScheduler.RESULT_SUCCESS) {
                        Timber.d("Job scheduled")
                    } else Timber.d("Job scheduling failed")

                    Toast.makeText(
                        this@FriendsOnlineActivity,
                        "Служба отслеживания активна",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (friend.tracking == false) {

                    val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                    scheduler.cancel(friend.id)
                    Timber.d("Job cancelled")
                    Toast.makeText(
                        this@FriendsOnlineActivity,
                        "Служба отслеживания остановлена",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
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
