package com.example.vkmessenger.di

import com.example.vkmessenger.service.StatusTrackingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    @ServiceScoped
    abstract fun contributeVkJobService(): StatusTrackingService
}