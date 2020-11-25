package com.example.vkmessenger.di

import android.app.Application
import android.content.Context
import com.example.vkmessenger.VkDefaultRepository
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.VkLocalDataSource
import com.example.vkmessenger.local.VkRoomDataSource
import com.example.vkmessenger.network.VkNetworkDataSource
import com.example.vkmessenger.network.VkRetrofitDataSource
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
    abstract fun bindFriendsFromVK(vkRetrofitDataSource: VkRetrofitDataSource): VkNetworkDataSource

    @Binds
    abstract fun bindFriendsLocalDataSource(friendsRoomDataSource: VkRoomDataSource): VkLocalDataSource

    @Binds
    abstract fun bindFriendsRepository(friendsDefaultRepository: VkDefaultRepository): VkRepository
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext