package com.example.vkmessenger.di

import com.example.vkmessenger.ui.AuthorizationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthorizationActivity(): AuthorizationActivity
}