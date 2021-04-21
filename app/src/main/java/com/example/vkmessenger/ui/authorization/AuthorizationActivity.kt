package com.example.vkmessenger.ui.authorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.vkmessenger.BuildConfig
import com.example.vkmessenger.R
import com.example.vkmessenger.databinding.ActivityAuthorizationBinding
import com.example.vkmessenger.ui.friends.FriendsActivity
import com.example.vkmessenger.util.Credentials
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_authorization.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AuthorizationActivity : AppCompatActivity() {

    private val authorizationViewModel: AuthorizationViewModel by viewModel()
    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        observeToastMessage()
        loadToken()
        if (Credentials.accessToken == "") {
            binding.buttonEntry.isEnabled = false
            VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.OFFLINE))
        } else {
            observeUserInfo()
            observeToastMessage()
        }

        binding.buttonEntry.setOnClickListener {
            observeUserInfo()
            observeToastMessage()
        }

        binding.buttonOpenFriends.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeUserInfo() {
        authorizationViewModel.userInfo.observe(this@AuthorizationActivity, Observer {
            binding.textFirstLastName.text = it.firstName + " " + it.lastName
            Glide.with(this@AuthorizationActivity)
                .load(it.photo)
                .into(binding.imageUserPhoto)
            Timber.d("onCreate Auth: $it")
        })
        binding.buttonOpenFriends.visibility = View.VISIBLE
        binding.buttonEntry.visibility = View.GONE
    }

    private fun observeToastMessage() {
        authorizationViewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun deleteToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        sPref.edit().clear().apply()
    }

    private fun loadToken() {
        val sPref = getSharedPreferences("token", Context.MODE_PRIVATE)
        val savedToken = sPref.getString("key", "")
        if (savedToken != null) {
            Credentials.accessToken = savedToken
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {

                Credentials.accessToken = token.accessToken
                val sPref = getSharedPreferences("token", Context.MODE_PRIVATE)
                val editPref = sPref.edit()
                editPref.putString("key", Credentials.accessToken)
                editPref.apply()
                authorizationViewModel.onAccessTokenObtained()

                Toast.makeText(
                    this@AuthorizationActivity,
                    "Вы успешно авторизовались",
                    Toast.LENGTH_LONG
                ).show()
                binding.buttonEntry.isEnabled = true
            }

            override fun onLoginFailed(errorCode: Int) {
                Timber.d("Token error: $errorCode")
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.auth_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                authorizationViewModel.onExitItemSelected()
                deleteToken()
                VK.logout()
                VK.login(
                    this,
                    arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.OFFLINE)
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}