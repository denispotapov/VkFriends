package com.example.vkmessenger.ui.authorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.ViewModelProviderFactory
import com.example.vkmessenger.network.ResponseResultUser2
import com.example.vkmessenger.network.ResponseUser
import com.example.vkmessenger.ui.friends.FriendsActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_authorization.*
import javax.inject.Inject

class AuthorizationActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var authorizationViewModel: AuthorizationViewModel

    private var tokenVK = ""
    private val TAG = "TAG"

    private val userList: List<ResponseUser> = listOf()
    private val resultUser = ResponseResultUser2(userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        authorizationViewModel =
            ViewModelProvider(this, providerFactory).get(AuthorizationViewModel::class.java)

        loadToken()
        if (tokenVK == "") {
            load.isEnabled = false
            VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.OFFLINE))
        } else loadUserInfo()

        load.setOnClickListener {
            loadUserInfo()
        }

        friendList.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserInfo() {
        authorizationViewModel.userInfo.observe(this@AuthorizationActivity, Observer {
            textView.text = it.first_name + " " + it.last_name
            Glide.with(this@AuthorizationActivity)
                .load(it.photo_100)
                .into(imageView)
            Log.d(TAG, "onResume: $it")
        })
        friendList.visibility = View.VISIBLE
        load.visibility = View.GONE
    }

    private fun deleteToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        sPref.edit().clear().apply()
    }

    private fun loadToken() {
        val sPref = getSharedPreferences("token", Context.MODE_PRIVATE)
        val savedToken = sPref.getString("key", "")
        if (savedToken != null) {
            tokenVK = savedToken
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {

                tokenVK = token.accessToken
                val sPref = getSharedPreferences("token", Context.MODE_PRIVATE)
                val editPref = sPref.edit()
                editPref.putString("key", tokenVK)
                editPref.apply()
                authorizationViewModel.requestUser(resultUser)

                Log.d(TAG, "onLogin: $tokenVK")
                Toast.makeText(
                    this@AuthorizationActivity,
                    "Вы успешно авторизовались",
                    Toast.LENGTH_LONG
                ).show()
                load.isEnabled = true
            }
            override fun onLoginFailed(errorCode: Int) {
                Log.d(TAG, "Token error: $errorCode")
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                authorizationViewModel.deleteAllFriends()
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