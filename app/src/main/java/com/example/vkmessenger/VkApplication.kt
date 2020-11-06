package com.example.vkmessenger

import com.example.vkmessenger.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class VkApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}