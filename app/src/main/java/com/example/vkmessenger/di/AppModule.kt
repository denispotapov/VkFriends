package com.example.vkmessenger.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.vkmessenger.local.FriendsDao
import com.example.vkmessenger.local.UserDao
import com.example.vkmessenger.local.VkDatabase
import com.example.vkmessenger.network.VkRetrofitApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideUserDao(vkDatabase: VkDatabase): UserDao =
        vkDatabase.userDao()

    @Singleton
    @Provides
    fun provideFriendsDao(vkDatabase: VkDatabase): FriendsDao =
        vkDatabase.friendsDao()

    @Singleton
    @Provides

    fun provideDatabase(@AppContext context: Context): VkDatabase {
        var INSTANCE: VkDatabase? = null

        INSTANCE = INSTANCE ?: Room.databaseBuilder(
            context.applicationContext,
            VkDatabase::class.java,
            "friends_base"
        ).build()

        return INSTANCE
    }

    @Singleton
    @Provides
    fun provideVkApi(): VkRetrofitApi = Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VkRetrofitApi::class.java)

    @Singleton
    @Provides
    fun provideSharedPreferences(@AppContext context: Context): SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    @Provides
    fun provideToken(sharedPreferences: SharedPreferences): String =
        sharedPreferences.getString("key", "")!!

}