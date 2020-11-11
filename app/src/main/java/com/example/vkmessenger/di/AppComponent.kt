package com.example.vkmessenger.di

import android.app.Application
import com.example.vkmessenger.VkApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBuildersModule::class, AppModule::class, AppModuleBinds::class, ViewModelFactoryModule::class])

interface AppComponent : AndroidInjector<VkApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}