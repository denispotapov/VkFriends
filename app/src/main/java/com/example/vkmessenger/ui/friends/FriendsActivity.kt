package com.example.vkmessenger.ui.friends

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
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
import com.example.vkmessenger.util.JOB_ID
import com.example.vkmessenger.util.MILLIS_IN_SECOND
import com.example.vkmessenger.util.SECONDS_COUNT
import com.example.vkmessenger.util.showNotificationUserOnline
import com.google.gson.Gson
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
            override fun onButtonClick(friend: Friend, position: Int) {

                val bundle = PersistableBundle()
                val friendToJobService = Friend(friend.id, friend.firstName, friend.lastName, friend.photo)
                val gSon = Gson()
                val json = gSon.toJson(friendToJobService)
                bundle.putString("friend", json)

                val componentName = ComponentName(this@FriendsActivity, StatusTrackingService::class.java)
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

                Toast.makeText(this@FriendsActivity, "Служба отслеживания активирована", Toast.LENGTH_LONG).show()
                Timber.d("OnClick: ${friend.id} + $position")
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

            R.id.stop_tracking -> {
                val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                scheduler.cancel(JOB_ID)
                Timber.d("Job cancelled")
                Toast.makeText(this@FriendsActivity, "Служба отслеживания остановлена", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}