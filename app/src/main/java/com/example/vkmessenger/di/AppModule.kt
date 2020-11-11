package com.example.vkmessenger.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.vkmessenger.local.FriendsDao
import com.example.vkmessenger.local.FriendsDatabase
import com.example.vkmessenger.network.VkApi
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
    fun provideFriendsDao(friendsDatabase: FriendsDatabase): FriendsDao =
        friendsDatabase.friendsDao()

    @Singleton
    @Provides
    fun provideDatabase(@AppContext context: Context): FriendsDatabase {
        var INSTANCE: FriendsDatabase? = null

        INSTANCE = INSTANCE ?: Room.databaseBuilder(
            context.applicationContext,
            FriendsDatabase::class.java,
            "friends_base"
        ).build()

        return INSTANCE
    }

    @Singleton
    @Provides
    fun provideVkApi(): VkApi = Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VkApi::class.java)

    @Singleton
    @Provides
    fun provideSharedPreferences(@AppContext context: Context): SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideToken(sharedPreferences: SharedPreferences): String =
        sharedPreferences.getString("key", "")!!

}