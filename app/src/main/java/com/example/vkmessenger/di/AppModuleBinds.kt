package com.example.vkmessenger.di

import android.app.Application
import android.content.Context
import com.example.vkmessenger.FriendsDefaultRepository
import com.example.vkmessenger.FriendsRepository
import com.example.vkmessenger.local.FriendRoomDataSource
import com.example.vkmessenger.local.FriendsLocalDataSource
import com.example.vkmessenger.network.FriendsFromVK
import com.example.vkmessenger.network.LoadFriendsRetrofit
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
abstract class AppModuleBinds {
    @Singleton
    @Binds
    @AppContext
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindFriendsFromVK(loadFriendsRetrofit: LoadFriendsRetrofit): FriendsFromVK

    @Binds
    abstract fun bindFriendsLocalDataSource(friendsRoomDataSource: FriendRoomDataSource): FriendsLocalDataSource

    @Binds
    abstract fun bindFriendsRepository(friendsDefaultRepository: FriendsDefaultRepository): FriendsRepository
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext