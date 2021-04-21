package com.example.vkmessenger.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.vkmessenger.VkDefaultRepository
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.*
import com.example.vkmessenger.network.VkNetworkDataSource
import com.example.vkmessenger.network.VkRetrofitApi
import com.example.vkmessenger.network.VkRetrofitDataSource
import com.example.vkmessenger.service.StatusTrackingServiceInteractor
import com.example.vkmessenger.ui.authorization.AuthorizationViewModel
import com.example.vkmessenger.ui.friends.FriendsViewModel
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single { provideDispatcher() }
    single { provideUserDao(provideDatabase(androidContext())) }
    single { provideFriendsDao(provideDatabase(androidContext())) }
    single { provideVkApi() }

    single<VkNetworkDataSource> { VkRetrofitDataSource(get(), get()) }
    single<VkLocalDataSource> { VkRoomDataSource(get(), get(), get()) }
    single<VkRepository> { VkDefaultRepository(get(), get(), get()) }
}

fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

fun provideUserDao(vkDatabase: VkDatabase): UserDao =
    vkDatabase.userDao()

fun provideFriendsDao(vkDatabase: VkDatabase): FriendsDao =
    vkDatabase.friendsDao()

private fun provideDatabase(context: Context): VkDatabase {
    var INSTANCE: VkDatabase? = null

    INSTANCE = INSTANCE ?: Room.databaseBuilder(
        context.applicationContext,
        VkDatabase::class.java,
        "vk_base"
    ).build()

    return INSTANCE
}

fun provideVkApi(): VkRetrofitApi = Retrofit.Builder()
    .baseUrl("https://api.vk.com/method/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(VkRetrofitApi::class.java)
