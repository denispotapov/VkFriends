package com.example.vkmessenger

import android.app.Application
import com.example.vkmessenger.di.appModule
import com.example.vkmessenger.di.serviceModule
import com.example.vkmessenger.di.viewModelModule
import io.mockk.mockk
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class MyTest : KoinTest {

    private val mockApplication: Application = mockk()

    @Test
    fun checkDependencyGraph() {
        startKoin {
            androidContext(mockApplication)
            modules(listOf(appModule, serviceModule, viewModelModule))
        }.checkModules()
    }
}