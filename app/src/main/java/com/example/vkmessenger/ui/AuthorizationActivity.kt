package com.example.vkmessenger.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.ResultFriends
import com.example.vkmessenger.ResultUser
import com.example.vkmessenger.VkApi
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_authorization.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthorizationActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var vkApi: VkApi
    private var tokenVK = ""
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        loadToken()
        loadUserInfo()
        friendList.setOnClickListener {
            getFriends()
        }
    }

    override fun onResume() {
        super.onResume()
        if (tokenVK == "") {
            VK.login(
                this,
                arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE)
            )
        } else {
            getUserInfo()
            friendList.visibility = View.VISIBLE
        }
    }

    private fun deleteToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        sPref.edit().clear().apply()
    }

    private fun loadToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedToken = sPref.getString("key", "")
        if (savedToken != null) {
            tokenVK = savedToken
        }
    }
    private fun loadUserInfo() {

        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedFirst = sPref.getString("fistName", "")
        val savedLast = sPref.getString("lastName", "")
        val savedPhoto = sPref.getString("image", "")
        if (savedFirst != null && savedLast != null && savedPhoto != null) {
            textView.text = "$savedFirst $savedLast"
            Glide.with(this@AuthorizationActivity)
                .load(savedPhoto)
                .into(imageView)
        }
    }

    private fun getUserInfo() {
        val call = vkApi.getUserInfo(tokenVK)
        call.enqueue(object : Callback<ResultUser> {
            override fun onResponse(
                call: Call<ResultUser>,
                response: Response<ResultUser>
            ) {
                if (!response.isSuccessful) {
                    Log.d(TAG, "Code" + response.code())
                    return
                }
                val userInfo = response.body()
                Log.d(TAG, "onResponse: $userInfo")
                textView.text =
                    (userInfo!!.response[0].first_name + " " + userInfo.response[0].last_name)
                Glide.with(this@AuthorizationActivity)
                    .load(userInfo.response[0].photo_100)
                    .into(imageView)

                val sPref = getPreferences(Context.MODE_PRIVATE)
                val editPref = sPref.edit()
                editPref.putString("fistName", userInfo.response[0].first_name)
                editPref.putString("lastName", userInfo.response[0].last_name)
                editPref.putString("image", userInfo.response[0].photo_100)
                editPref.apply()
            }

            override fun onFailure(call: Call<ResultUser>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getFriends() {
        val call = vkApi.getFriends(tokenVK)
        call.enqueue(object : Callback<ResultFriends> {
            override fun onResponse(
                call: Call<ResultFriends>,
                response: Response<ResultFriends>
            ) {
                if (!response.isSuccessful) {
                    Log.d(TAG, "Code" + response.code())
                    return
                }
                val friends = response.body()
                Log.d(TAG, "onResponse: $friends")

            }

            override fun onFailure(call: Call<ResultFriends>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                tokenVK = token.accessToken
                val sPref = getPreferences(Context.MODE_PRIVATE)
                val editPref = sPref.edit()
                editPref.putString("key", tokenVK)
                editPref.apply()

                Log.d(TAG, "onLogin: $tokenVK")
                Toast.makeText(
                    this@AuthorizationActivity,
                    "Вы успешно авторизовались",
                    Toast.LENGTH_LONG
                ).show()
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
                deleteToken()
                VK.logout()
                VK.login(
                    this,
                    arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE)
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}