package com.example.vkmessenger.di

import com.example.vkmessenger.ui.authorization.AuthorizationActivity
import com.example.vkmessenger.ui.friends.FriendsActivity
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [AuthorizationViewModelModule::class])
    abstract fun contributeAuthorizationActivity(): AuthorizationActivity

    @ContributesAndroidInjector(modules = [FriendsViewModelModule::class])
    abstract fun contributeFriendsActivity(): FriendsActivity

    @ContributesAndroidInjector(modules = [FriendsOnlineViewModelModule::class])
    abstract fun contributeFriendsOnlineActivity(): FriendsOnlineActivity

}