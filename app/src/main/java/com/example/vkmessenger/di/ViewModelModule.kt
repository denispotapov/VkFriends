package com.example.vkmessenger.di

import com.example.vkmessenger.ui.authorization.AuthorizationViewModel
import com.example.vkmessenger.ui.friends.FriendsViewModel
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthorizationViewModel(get()) }
    viewModel { FriendsViewModel(get()) }
    viewModel { FriendsOnlineViewModel(get()) }
}