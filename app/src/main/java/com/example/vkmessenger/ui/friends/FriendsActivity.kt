package com.example.vkmessenger.ui.friends

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.adapters.FriendsAdapter
import com.example.vkmessenger.databinding.ActivityFriendsBinding
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.service.StatusTrackingService
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_friends.*
import timber.log.Timber
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
        startStatusTrackingService()
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

    private fun startStatusTrackingService() {
        friendsAdapter.setOnButtonClickListener(object : FriendsAdapter.OnButtonClickListener {

            override fun onTrackStart(friend: Friend) {
                friendsViewModel.updateFriend(friend)

                if (friend.tracking == true) {
                    val componentName =
                        ComponentName(this@FriendsActivity, StatusTrackingService::class.java)
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
                        this@FriendsActivity,
                        "Служба отслеживания активна",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (friend.tracking == false) {
                    val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                    scheduler.cancel(friend.id)
                    Timber.d("Job cancelled")
                    Toast.makeText(
                        this@FriendsActivity,
                        "Служба отслеживания остановлена",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
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