package com.example.vkmessenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_authorization.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class AuthorizationActivity : AppCompatActivity() {

    private lateinit var vkApi: VkApi
    private var tokenVK = ""
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        loadToken()
        if (tokenVK == "") {
            VK.login(
                this,
                arrayListOf(VKScope.FRIENDS, VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE)
            )
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.vk.com/method/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        vkApi = retrofit.create(VkApi::class.java)

        logIn.setOnClickListener {
            getUserInfo()
            //VK.logout()
            // getFriendOnline()
            //getFriends()
        }
    }

    private fun deleteToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        sPref.edit().clear().apply()
    }

    private fun loadToken() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedText = sPref.getString("key", "")
        if (savedText != null) {
            tokenVK = savedText
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
                    .load(userInfo.response[0].photo_50)
                    .into(imageView)
            }

            override fun onFailure(call: Call<ResultUser>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    /*private fun getFriendOnline() {
        val call = vkApi.getFriendsOnline()
        call.enqueue(object : Callback<ResultFriendsOnline> {
            override fun onResponse(
                call: Call<ResultFriendsOnline>,
                response: Response<ResultFriendsOnline>
            ) {
                if (!response.isSuccessful) {
                    Log.d(TAG, "Code" + response.code())
                    return
                }
                val friendsOnline = response.body()
                Log.d(TAG, "onResponse: $friendsOnline")

            }

            override fun onFailure(call: Call<ResultFriendsOnline>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getFriends() {
        val call = vkApi.getFriends()
        call.enqueue(object : Callback<ResultFriends> {
            override fun onResponse(
                call: Call<ResultFriends>,
                response: Response<ResultFriends>
            ) {
                if (!response.isSuccessful) {
                    Log.d(TAG, "Code" + response.code())
                    return
                }
                val friendsOnline = response.body()
                Log.d(TAG, "onResponse: $friendsOnline")

            }

            override fun onFailure(call: Call<ResultFriends>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                tokenVK = token.accessToken
                val sPref = getPreferences(Context.MODE_PRIVATE)
                val ed = sPref.edit()
                ed.putString("key", tokenVK)
                ed.apply()


                Log.d(TAG, "onLogin: $tokenVK")
                //textView.text = tokenVK
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