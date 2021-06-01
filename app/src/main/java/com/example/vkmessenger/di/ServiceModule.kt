package com.example.vkmessenger.di

import com.example.vkmessenger.service.StatusTrackingServiceInteractor
import org.koin.dsl.module

val serviceModule = module {
    single { StatusTrackingServiceInteractor(get()) }
}